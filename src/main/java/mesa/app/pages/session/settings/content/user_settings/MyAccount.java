package mesa.app.pages.session.settings.content.user_settings;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import mesa.api.Auth;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.SettingsContent;
import mesa.app.utils.Colors;
import mesa.data.User;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class MyAccount extends SettingsContent {

	private Label myAccountLab;
	private Label passAuth;
	private Label twoFactAuthHead;
	private Label twoFactAuthBody;
	private Label accountRemovalHead;
	private Label accountRemovalBody;

	private Button changePass;

	private ColorIcon twoFactAuthIcon;
	private ColorIcon twoFactAuthIconOver;
	private Rectangle twoFactAuthIconUnder;

	private Button disable;
	private Button delete;

	public MyAccount(Settings settings) {
		super(settings);
		try {
			Window window = settings.getWindow();

			User user = settings.getUser();

			UnverifiedEmail unverifiedEmail = new UnverifiedEmail(settings);

			myAccountLab = new Label(window, "my_account", header);

			passAuth = new Label(window, "pass_&_auth", header);

			PasswordEditOverlay changePassOver = new PasswordEditOverlay(settings);

			changePass = new Button(window, "change_pass", 3.0, 16, 32);
			changePass.setFont(new Font(13, FontWeight.BOLD));

			changePass.setAction(() -> changePassOver.show(settings.getSession()));

			HBox twoFactAuth = new HBox(20);
			twoFactAuth.setAlignment(Pos.CENTER);

			twoFactAuthHead = new Label(window, "2_fact_auth", new Font(12, FontWeight.BOLD));
			twoFactAuthHead.setTransform(TextTransform.UPPERCASE);

			twoFactAuthBody = new Label(window, "2_fact_auth_verify_account", new Font(14));
			TextFlow preBody = new TextFlow();
			preBody.getChildren().add(twoFactAuthBody);

			VBox twoFactAuthLeft = new VBox(12);
			HBox.setHgrow(twoFactAuthLeft, Priority.ALWAYS);

			twoFactAuthLeft.getChildren().addAll(twoFactAuthHead, preBody);

			StackPane twoFactAuthRight = new StackPane();
			twoFactAuthRight.setAlignment(Pos.BOTTOM_CENTER);
			twoFactAuthRight.setMaxHeight(46);
			twoFactAuthRight.setMinHeight(46);
			twoFactAuthRight.setMinWidth(230);

			twoFactAuthIcon = new ColorIcon("2fa", 60);
			twoFactAuthIconOver = new ColorIcon("2faover", 60);

			twoFactAuthIconUnder = new Rectangle(140, 80);
			twoFactAuthIconUnder.setArcHeight(20);
			twoFactAuthIconUnder.setArcWidth(20);
			twoFactAuthIconUnder.setTranslateY(5);

			twoFactAuthRight.getChildren().addAll(twoFactAuthIconUnder, twoFactAuthIconOver, twoFactAuthIcon);

			twoFactAuth.getChildren().addAll(twoFactAuthLeft, twoFactAuthRight);

			accountRemovalHead = new Label(window, "account_removal");
			accountRemovalHead.setTransform(TextTransform.UPPERCASE);
			accountRemovalHead.setFont(twoFactAuthHead.getFont());

			accountRemovalBody = new Label(window, "disable_note");
			accountRemovalBody.setFont(twoFactAuthBody.getFont());

			HBox accountButtons = new HBox(16);

			disable = new Button(window, "disable_account", 3.0, 16, 32);
			disable.setFont(new Font(13, FontWeight.BOLD));

			delete = new Button(window, "delete_account", 3.0, 16, 32);
			delete.setFont(new Font(13, FontWeight.BOLD));

			accountButtons.getChildren().addAll(disable, delete);

			changePassOver.setAction(() -> {
				changePassOver.startLoading();
				Auth.changePassword(user.getId(), changePassOver.getCurrPass(), changePassOver.getConfNewPass(),
						result -> {
							if (result.has("err")) {
								changePassOver.applyErrors(result.getJSONArray("err"));
							} else {
								changePassOver.hide();
							}

							changePassOver.stopLoading();
						});
			});

			getChildren().addAll(myAccountLab, new FixedVSpace(20));

			if (!user.isEmailConfirmed()) {
				getChildren().add(unverifiedEmail);
			}

			user.emailConfirmedProperty().addListener((obs, ov, nv) -> {
				if (nv.booleanValue() && getChildren().contains(unverifiedEmail)) {
					getChildren().remove(unverifiedEmail);
				} else if (!nv.booleanValue() && !getChildren().contains(unverifiedEmail)) {
					getChildren().add(2, unverifiedEmail);
				}
			});

			getChildren().addAll(new ProfileOverview(settings), new FixedVSpace(40));
			separate();
			getChildren().addAll(new FixedVSpace(40), passAuth, new FixedVSpace(20), changePass, new FixedVSpace(28));
			getChildren().addAll(twoFactAuth, new FixedVSpace(40));
			separate();
			getChildren().addAll(new FixedVSpace(40), accountRemovalHead, new FixedVSpace(12), accountRemovalBody,
					new FixedVSpace(16), accountButtons);

			applyStyle(window.getStyl());

		} catch (Exception x) {
			ErrorHandler.handle(x, "create account settings");
		}
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		myAccountLab.setFill(style.getText1());
		passAuth.setFill(style.getText1());

		changePass.setTextFill(style.getText1());
		changePass.setFill(style.getAccent());

		twoFactAuthHead.setFill(style.getInteractiveNormal());
		twoFactAuthBody.setFill(style.getInteractiveNormal());

		twoFactAuthIcon.setFill(style.getAccent());
		twoFactAuthIconOver.setFill(style.getBack3());
		twoFactAuthIconUnder.setFill(style.getBack4());

		accountRemovalHead.setFill(style.getInteractiveNormal());
		accountRemovalBody.setFill(style.getInteractiveNormal());

		disable.setFill(Colors.Error);
		disable.setTextFill(style.getText1());

		delete.setFill(Color.TRANSPARENT);
		delete.setTextFill(Colors.Error);
		delete.setStroke(Colors.Error);
	}

}
