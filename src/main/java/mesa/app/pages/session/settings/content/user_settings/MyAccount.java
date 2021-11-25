package mesa.app.pages.session.settings.content.user_settings;

import javafx.geometry.Insets;
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
import mesa.app.pages.session.settings.content.user_settings.overlays.CriticalOverlay;
import mesa.app.pages.session.settings.content.user_settings.overlays.PasswordEditOverlay;
import mesa.app.pages.session.settings.content.user_settings.overview.ProfileOverview;
import mesa.app.utils.Colors;
import mesa.data.User;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.image.LayerIcon;
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
	private Button enable2fa;

	private LayerIcon twoFactAuthIcon;
	private Rectangle twoFactAuthIconUnder;

	private Button disable;
	private Button delete;

	public MyAccount(Settings settings) {
		super(settings);
		try {
			Window window = settings.getWindow();

			User user = settings.getUser();

			myAccountLab = new Label(window, "my_account", header);
			setMargin(myAccountLab, new Insets(0, 0, 25, 0));

			ProfileOverview profileOverview = new ProfileOverview(settings);

			UnverifiedEmail unverifiedEmail = new UnverifiedEmail(settings);

			passAuth = new Label(window, "pass_&_auth", header);
			setMargin(passAuth, new Insets(0, 0, 20, 0));

			PasswordEditOverlay changePassOver = new PasswordEditOverlay(settings.getSession());

			changePass = new Button(window, "change_pass", 3.0, 16, 32);
			changePass.setFont(new Font(13, FontWeight.BOLD));
			setMargin(changePass, new Insets(0, 0, 28, 0));

			changePass.setAction(changePassOver::show);

			HBox twoFactAuth = new HBox(20);
			twoFactAuth.setAlignment(Pos.CENTER);

			twoFactAuthHead = new Label(window, "2_fact_auth", new Font(12, FontWeight.BOLD));
			twoFactAuthHead.setTransform(TextTransform.UPPERCASE);

			twoFactAuthBody = new Label(window,
					settings.getUser().isEmailConfirmed() ? "2_fact_auth_note" : "2_fact_auth_verify_account",
					new Font(13));
			TextFlow preBody = new TextFlow();
			preBody.setLineSpacing(4);
			preBody.getChildren().add(twoFactAuthBody);

			enable2fa = new Button(window, "2_fact_auth_enable", 3.0, 16, 32);
			enable2fa.setFont(new Font(13, FontWeight.BOLD));

			VBox twoFactAuthLeft = new VBox(14);
			HBox.setHgrow(twoFactAuthLeft, Priority.ALWAYS);

			twoFactAuthLeft.getChildren().addAll(twoFactAuthHead, preBody);

			if (user.isEmailConfirmed()) {
				twoFactAuthLeft.getChildren().add(enable2fa);
			}

			StackPane twoFactAuthRight = new StackPane();
			twoFactAuthRight.setAlignment(Pos.BOTTOM_CENTER);
			twoFactAuthRight.setMaxHeight(61);
			twoFactAuthRight.setMinHeight(61);
			twoFactAuthRight.setMinWidth(200);

			twoFactAuthIcon = new LayerIcon(80, "2faover", "2fa");

			twoFactAuthIconUnder = new Rectangle(160, 100);
			twoFactAuthIconUnder.setArcHeight(20);
			twoFactAuthIconUnder.setArcWidth(20);
			twoFactAuthIconUnder.setTranslateY(7);

			twoFactAuthRight.getChildren().addAll(twoFactAuthIconUnder, twoFactAuthIcon);

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

			CriticalOverlay disableOverlay = new CriticalOverlay(settings.getSession(), "disable_account", "disable_warning");
			CriticalOverlay deleteOverlay = new CriticalOverlay(settings.getSession(), "delete_account", "delete_warning");
			
			disableOverlay.setAction(() -> {
				disableOverlay.startLoading();
				//TODO disable account
			});
			
			deleteOverlay.setAction(() -> {
				deleteOverlay.startLoading();
				Auth.deleteAccount(user.getId(), deleteOverlay.getPassword(), result -> {
					if (result.has("err")) {
						deleteOverlay.applyErrors(result.getJSONArray("err"));
					} else {
						deleteOverlay.hide();
						settings.getSession().logout();
					}
					
					deleteOverlay.stopLoading();
				});
			});
			
			disable.setAction(disableOverlay::show);
			delete.setAction(deleteOverlay::show);
			
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

			getChildren().addAll(myAccountLab);

			if (!user.isEmailConfirmed()) {
				getChildren().add(unverifiedEmail);
			}

			getChildren().addAll(profileOverview);
			separate(40);
			getChildren().addAll(passAuth, changePass);
			getChildren().addAll(twoFactAuth);

			separate(40);
			getChildren().addAll(accountRemovalHead, new FixedVSpace(12), accountRemovalBody, new FixedVSpace(16),
					accountButtons);

			user.emailConfirmedProperty().addListener((obs, ov, nv) -> {
				if (nv.booleanValue()) {
					if (getChildren().contains(unverifiedEmail)) {
						getChildren().remove(unverifiedEmail);
					}

					if (!twoFactAuthLeft.getChildren().contains(enable2fa)) {
						twoFactAuthLeft.getChildren().add(enable2fa);
					}

					twoFactAuthBody.setKey("2_fact_auth_note");
				} else if (!nv.booleanValue()) {
					if (!getChildren().contains(unverifiedEmail)) {
						getChildren().add(1, unverifiedEmail);
					}

					if (twoFactAuthLeft.getChildren().contains(enable2fa)) {
						twoFactAuthLeft.getChildren().remove(enable2fa);
					}

					twoFactAuthBody.setKey("2_fact_auth_verify_account");
				}
			});

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

		enable2fa.setTextFill(style.getText1());
		enable2fa.setFill(style.getAccent());

		twoFactAuthIcon.setFill(0, style.getBack3());
		twoFactAuthIcon.setFill(1, style.getAccent());
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
