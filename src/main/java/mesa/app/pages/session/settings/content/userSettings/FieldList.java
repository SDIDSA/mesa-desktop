package mesa.app.pages.session.settings.content.userSettings;

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
import mesa.app.utils.Colors;
import mesa.data.User;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.overlay.EditOverlay;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.popup.tooltip.Tooltip;
import mesa.gui.controls.space.FixedHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class FieldList extends VBox implements Styleable {
	private Text usernameLab, idLab;

	private Rectangle edit_tag_separate;
	private StackPane help_edit_tag;
	private ColorIcon help_edit_tag_icon;

	private Text hash, tag;

	private Button removePhone;

	public FieldList(Settings settings) {
		super(25);
		setPadding(new Insets(16));
		setMinHeight(200);

		User user = settings.getUser();

		OverviewField username = new OverviewField(settings, "username");
		HideableOverviewField email = new HideableOverviewField(settings, "email_address", user.getEmail(),
				TextTransform.HIDE_EMAIL);
		OverviewField phone = new HideableOverviewField(settings, "phone", user.getPhone(),
				TextTransform.HIDE_PHONE);

		EditOverlay editUsername = new EditOverlay(settings, "username");

		editUsername.addOnShown(0, ()->editUsername.setValue(user.getUsername()));

		edit_tag_separate = new Rectangle(1, 30);
		edit_tag_separate.setOpacity(.1);

		hash = new Text("#");
		hash.setFont(new Font(15).getFont());
		tag = new Text(user.getId());
		tag.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 15).getFont());
		tag.setOpacity(.5);

		help_edit_tag = new StackPane();
		help_edit_tag.setCursor(Cursor.HAND);
		help_edit_tag.setMaxSize(32, 31);
		help_edit_tag.setMinSize(32, 31);
		
		help_edit_tag.setFocusTraversable(true);
		help_edit_tag.borderProperty().bind(Bindings.when(help_edit_tag.focusedProperty()).then(Borders.make(Colors.LINK, 4.0)).otherwise(Border.EMPTY));

		Tooltip tip = new Tooltip(settings.getWindow(), "Get Black Mesa to modify your tag!",
				Direction.LEFT);
		tip.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		Tooltip.install(help_edit_tag, tip);

		help_edit_tag_icon = new ColorIcon(settings.getWindow(), "question", 16);

		help_edit_tag.getChildren().add(help_edit_tag_icon);

		editUsername.addPostField(edit_tag_separate, new FixedHSpace(20), hash, tag, new FixedHSpace(20), help_edit_tag,
				new FixedHSpace(4));
		
		editUsername.doneDisabled().bind(editUsername.valueProperty().isEqualTo(user.usernameProperty()));
		editUsername.setAction(()-> {
			if(editUsername.checkForm()) {
				editUsername.startLoading();

				String newUsername = editUsername.getValue();
				Auth.editUsername(user.getId(), newUsername, editUsername.getPassword(), result -> {
					if(result.has("err")) {
						editUsername.applyErrors(result.getJSONArray("err"));
					}else {
						user.setUsername(newUsername);
						editUsername.hide();
					}
					
					editUsername.stopLoading();
				});
			}
		});
		
		EditOverlay editEmail = new EditOverlay(settings, "email_address");

		editEmail.doneDisabled().bind(editEmail.valueProperty().isEqualTo(user.emailProperty()));
		
		editEmail.setAction(()-> {
			if(editEmail.checkForm()) {
				editEmail.startLoading();
				
				String newEmail = editEmail.getValue();
				Auth.editEmail(user.getId(), newEmail, editEmail.getPassword(), result -> {
					if(result.has("err")) {
						editEmail.applyErrors(result.getJSONArray("err"));
					}else {
						user.setEmail(newEmail);
						user.setEmailConfirmed(false);
						email.setFull(newEmail);
						editEmail.hide();
					}
					
					editEmail.stopLoading();
				});
			}
		});

		username.setEditOver(editUsername);
		email.setEditOver(editEmail);

		removePhone = new Button(settings.getWindow(), "overview_remove", 3, 80, 32);
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

		edit_tag_separate.setFill(style.getInteractiveNormal());

		hash.setFill(style.getTextMuted());
		tag.setFill(style.getText1());

		help_edit_tag.backgroundProperty().bind(Bindings.when(help_edit_tag.focusedProperty()).then(Backgrounds.make(style.getAccent(), 2.0, 4.0)).otherwise(Backgrounds.make(style.getAccent(), 2.0)));
		help_edit_tag_icon.setFill(style.getText1());
	}
}
