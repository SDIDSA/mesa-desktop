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
import mesa.gui.controls.image.layer_icon.LayerIcon;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class MyAccount extends SettingsContent {
	private Label myAccountLab;
	private Label passAuth;
	private Label twoFactAuthHead;
	private MultiText twoFactAuthBody;
	private Label accountRemovalHead;
	private MultiText accountRemovalBody;

	private Button changePass;
	private Button enable2fa;

	private LayerIcon twoFactAuthIcon;
	private Rectangle twoFactAuthIconUnder;

	private Button disable;
	private Button delete;

	private UnverifiedEmail unverifiedEmail;
	private VBox twoFactAuthLeft;

	public MyAccount(Settings settings) {
		Window window = settings.getWindow();

		User user = settings.getUser();

		myAccountLab = new Label(window, "my_account", header);
		setMargin(myAccountLab, new Insets(0, 0, 25, 0));

		ProfileOverview profileOverview = new ProfileOverview(settings);

		unverifiedEmail = new UnverifiedEmail(settings);

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

		twoFactAuthBody = new MultiText(window, "", new Font(13));
		twoFactAuthBody.setLineSpacing(4);

		enable2fa = new Button(window, "2_fact_auth_enable", 3.0, 16, 32);
		enable2fa.setFont(new Font(13, FontWeight.BOLD));

		twoFactAuthLeft = new VBox(14);
		HBox.setHgrow(twoFactAuthLeft, Priority.ALWAYS);

		twoFactAuthLeft.getChildren().addAll(twoFactAuthHead, twoFactAuthBody);

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

		accountRemovalBody = new MultiText(window, "disable_note", new Font(13));

		HBox accountButtons = new HBox(16);

		disable = new Button(window, "disable_account", 3.0, 16, 32);
		disable.setFont(new Font(13, FontWeight.BOLD));

		delete = new Button(window, "delete_account", 3.0, 16, 32);
		delete.setFont(new Font(13, FontWeight.BOLD));

		CriticalOverlay disableOverlay = new CriticalOverlay(settings.getSession(), "disable_account",
				"disable_warning");
		CriticalOverlay deleteOverlay = new CriticalOverlay(settings.getSession(), "delete_account", "delete_warning");

		disableOverlay.setAction(() -> {
			disableOverlay.startLoading();

			// TODO disable account
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
			Auth.changePassword(user.getId(), changePassOver.getCurrPass(), changePassOver.getConfNewPass(), result -> {
				if (result.has("err")) {
					changePassOver.applyErrors(result.getJSONArray("err"));
				} else {
					changePassOver.hide();
				}

				changePassOver.stopLoading();
			});
		});

		getChildren().addAll(myAccountLab);

		getChildren().addAll(profileOverview);
		separate(40);
		getChildren().addAll(passAuth, changePass);
		getChildren().addAll(twoFactAuth);

		separate(40);
		getChildren().addAll(accountRemovalHead, new FixedVSpace(12), accountRemovalBody, new FixedVSpace(16),
				accountButtons);

		user.emailConfirmedProperty().addListener((obs, ov, nv) -> onEmailConfirmedChange(nv.booleanValue()));

		onEmailConfirmedChange(user.isEmailConfirmed());
		
		applyStyle(window.getStyl());
	}

	private void onEmailConfirmedChange(boolean value) {
		if (value) {
			if (getChildren().contains(unverifiedEmail)) {
				getChildren().remove(unverifiedEmail);
			}

			if (!twoFactAuthLeft.getChildren().contains(enable2fa)) {
				twoFactAuthLeft.getChildren().add(enable2fa);
			}

			twoFactAuthBody.setKey("2_fact_auth_note");
		} else {
			if (!getChildren().contains(unverifiedEmail)) {
				getChildren().add(1, unverifiedEmail);
			}

			if (twoFactAuthLeft.getChildren().contains(enable2fa)) {
				twoFactAuthLeft.getChildren().remove(enable2fa);
			}

			twoFactAuthBody.setKey("2_fact_auth_verify_account");
		}
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		myAccountLab.setFill(style.getHeaderPrimary());
		passAuth.setFill(style.getHeaderPrimary());

		changePass.setTextFill(Color.WHITE);
		changePass.setFill(style.getAccent());

		twoFactAuthHead.setFill(style.getHeaderSecondary());
		twoFactAuthBody.setFill(style.getHeaderSecondary());

		enable2fa.setTextFill(Color.WHITE);
		enable2fa.setFill(style.getAccent());

		twoFactAuthIcon.setFill(0, style.getBackgroundTertiary());
		twoFactAuthIcon.setFill(1, style.getAccent());
		twoFactAuthIconUnder.setFill(style.getBackgroundSecondaryAlt());

		accountRemovalHead.setFill(style.getHeaderSecondary());
		accountRemovalBody.setFill(style.getHeaderSecondary());

		disable.setFill(Colors.Error);
		disable.setTextFill(Color.WHITE);

		delete.setFill(Color.TRANSPARENT);
		delete.setTextFill(Colors.Error);
		delete.setStroke(Colors.Error);
	}

}
