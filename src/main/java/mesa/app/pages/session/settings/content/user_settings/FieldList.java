package mesa.app.pages.session.settings.content.user_settings;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import mesa.api.Auth;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.user_settings.overlays.KeyValueEditOverlay;
import mesa.app.pages.session.settings.content.user_settings.overview.HideableOverviewField;
import mesa.app.pages.session.settings.content.user_settings.overview.OverviewField;
import mesa.app.utils.Colors;
import mesa.data.User;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.popup.tooltip.Tooltip;
import mesa.gui.controls.space.FixedHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class FieldList extends VBox implements Styleable {
	private Text usernameLab;
	private Text idLab;

	private Rectangle editTagSeparate;
	private StackPane helpEditTag;
	private ColorIcon editTagIcon;

	private Text hash;
	private Text tag;

	private Button removePhone;

	public FieldList(Settings settings) {
		super(25);
		setPadding(new Insets(16));
		setMinHeight(200);

		User user = settings.getUser();

		OverviewField username = new OverviewField(settings, "username");
		HideableOverviewField email = new HideableOverviewField(settings, "email_address", user.emailProperty(),
				TextTransform.HIDE_EMAIL);
		HideableOverviewField phone = new HideableOverviewField(settings, "phone", user.phoneProperty(),
				TextTransform.HIDE_PHONE);

		KeyValueEditOverlay editUsername = new KeyValueEditOverlay(settings.getSession(), "username");
		KeyValueEditOverlay editEmail = new KeyValueEditOverlay(settings.getSession(), "email_address");

		editUsername.addOnShown(0, () -> editUsername.setValue(user.getUsername()));

		editTagSeparate = new Rectangle(1, 30);
		editTagSeparate.setOpacity(.1);

		hash = new Text("#");
		hash.setFont(new Font(15).getFont());
		tag = new Text(user.getId());
		tag.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 15).getFont());
		tag.setOpacity(.5);

		helpEditTag = new StackPane();
		helpEditTag.setCursor(Cursor.HAND);
		helpEditTag.setMaxSize(32, 31);
		helpEditTag.setMinSize(32, 31);

		helpEditTag.setFocusTraversable(true);
		helpEditTag.borderProperty().bind(Bindings.when(helpEditTag.focusedProperty())
				.then(Borders.make(Colors.LINK, 4.0)).otherwise(Border.EMPTY));

		Tooltip tip = new Tooltip(settings.getWindow(), "Get Black Mesa to modify your tag!", Direction.LEFT);
		tip.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		Tooltip.install(helpEditTag, tip);

		editTagIcon = new ColorIcon("question", 16);

		helpEditTag.getChildren().add(editTagIcon);

		editUsername.addPostField(editTagSeparate, new FixedHSpace(20), hash, tag, new FixedHSpace(20), helpEditTag,
				new FixedHSpace(4));

		editUsername.doneDisabled().bind(editUsername.valueProperty().isEqualTo(user.usernameProperty()));
		editUsername.setAction(() -> {
			editUsername.startLoading();
			String newUsername = editUsername.getValue();
			Auth.editUsername(user.getId(), newUsername, editUsername.getPassword(), result -> {
				if (result.has("err")) {
					editUsername.applyErrors(result.getJSONArray("err"));
				} else {
					user.setUsername(newUsername);
					editUsername.hide();
				}
				editUsername.stopLoading();
			});
		});

		editEmail.doneDisabled().bind(editEmail.valueProperty().isEqualTo(user.emailProperty()));

		editEmail.setAction(() -> {
			editEmail.startLoading();
			String newEmail = editEmail.getValue();
			Auth.editEmail(user.getId(), newEmail, editEmail.getPassword(), result -> {
				if (result.has("err")) {
					editEmail.applyErrors(result.getJSONArray("err"));
				} else {
					user.setEmail(newEmail);
					user.setEmailConfirmed(false);
					editEmail.hide();
				}
				editEmail.stopLoading();
			});
		});

		username.setEditOver(editUsername);
		email.setEditOver(editEmail);

		removePhone = new Button(settings.getWindow(), "overview_remove", 3, 16, 32);
		removePhone.setFont(new Font(14, FontWeight.BOLD));
		removePhone.setUlOnHover(true);
		phone.addToPreEdit(removePhone, new FixedHSpace(8));

		Font font = new Font(16);

		usernameLab = new Text();
		usernameLab.textProperty().bind(user.usernameProperty());
		idLab = new Text('#' + user.getId());

		usernameLab.setFont(font.getFont());
		idLab.setFont(font.getFont());

		username.addToValue(usernameLab, idLab);

		getChildren().addAll(username, email, phone);

		applyStyle(settings.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBack2(), 8.0));

		usernameLab.setFill(style.getText1());

		idLab.setFill(style.getInteractiveNormal());

		removePhone.setFill(Color.TRANSPARENT);
		removePhone.setTextFill(style.getText1());

		editTagSeparate.setFill(style.getInteractiveNormal());

		hash.setFill(style.getTextMuted());
		tag.setFill(style.getText1());

		helpEditTag.backgroundProperty()
				.bind(Bindings.when(helpEditTag.focusedProperty()).then(Backgrounds.make(style.getAccent(), 2.0, 4.0))
						.otherwise(Backgrounds.make(style.getAccent(), 2.0)));
		editTagIcon.setFill(style.getText1());
	}
}
