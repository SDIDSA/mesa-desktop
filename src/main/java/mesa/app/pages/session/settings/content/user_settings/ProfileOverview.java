package mesa.app.pages.session.settings.content.user_settings;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import mesa.app.component.StatedPfp;
import mesa.app.component.StatedPfp.PfpStatus;
import mesa.app.pages.session.settings.Settings;
import mesa.data.User;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.popup.context.ContextMenu;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.controls.space.FixedHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ProfileOverview extends StackPane implements Styleable {

	private StackPane back;
	private StackPane pfpCont;
	private VBox content;
	private Text userName;
	private Text id;
	private ColorIcon more;

	private Button editUserProfile;

	public ProfileOverview(Settings settings) {
		back = new StackPane();
		back.setAlignment(Pos.TOP_CENTER);

		StackPane backTop = new StackPane();
		backTop.setMaxHeight(100);
		backTop.setMinHeight(100);

		backTop.setBackground(Backgrounds.make(Color.BLACK, new CornerRadii(8, 8, 0, 0, false)));

		back.getChildren().add(backTop);

		content = new VBox(10);
		content.setPadding(new Insets(76, 16, 16, 16));

		getChildren().addAll(back, content);

		HBox userInfo = new HBox();
		userInfo.setAlignment(Pos.CENTER_LEFT);

		pfpCont = new StackPane();
		pfpCont.setPadding(new Insets(7));
		StatedPfp pfp = new StatedPfp(settings.getSession(), null, StatedPfp.BIG);
		pfp.setStatus(PfpStatus.ONLINE);

		pfpCont.getChildren().add(pfp);

		User user = settings.getSession().getUser();

		userName = new Text();
		userName.textProperty().bind(user.usernameProperty());
		userName.setFont(new Font(Font.DEFAULT_FAMILY, 20, FontWeight.BOLD).getFont());
		userName.setTranslateY(9);

		id = new Text("#" + user.getId());
		id.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 20).getFont());
		id.setTranslateY(9);

		more = new ColorIcon("more", 24);
		more.setCursor(Cursor.HAND);
		more.setTranslateY(11);
		more.setPadding(4);

		ContextMenu menu = new ContextMenu(settings.getSession().getWindow());
		menu.addMenuItem("copy_id", () -> {
			ClipboardContent cbc = new ClipboardContent();
			cbc.putString(user.getUsername() + "#" + user.getId());

			Clipboard.getSystemClipboard().setContent(cbc);
		});

		more.setAction(() -> menu.showPop(more, 15));

		editUserProfile = new Button(settings.getSession().getWindow(), "edit_user_profile", 3.0, 16, 32);
		editUserProfile.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		editUserProfile.setTranslateY(9);

		userInfo.getChildren().addAll(pfpCont, new FixedHSpace(10), userName, id, new FixedHSpace(8), more,
				new ExpandingHSpace(), editUserProfile);
		
		content.getChildren().addAll(userInfo, new FieldList(settings));

		applyStyle(settings.getSession().getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		back.setBackground(Backgrounds.make(style.getBack3(), 8.0));

		pfpCont.setBackground(Backgrounds.make(style.getBack3(), 47.0));
		userName.setFill(style.getText1());
		id.setFill(style.getInteractiveNormal());
		more.setFill(style.getInteractiveNormal());

		editUserProfile.setFill(style.getAccent());
		editUserProfile.setTextFill(style.getText1());
	}

}