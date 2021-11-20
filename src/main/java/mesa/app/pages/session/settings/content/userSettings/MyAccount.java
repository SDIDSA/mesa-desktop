package mesa.app.pages.session.settings.content.userSettings;

import javafx.scene.text.FontWeight;
import mesa.api.Auth;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.SettingsContent;
import mesa.data.User;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class MyAccount extends SettingsContent {

	private Label myAccount, passAuth;

	private Button changePass;

	public MyAccount(Settings settings) {
		super(settings);
		Window window = settings.getWindow();

		User user = settings.getUser();

		UnverifiedEmail unverifiedEmail = new UnverifiedEmail(settings);

		myAccount = new Label(window, "my_account", header);

		passAuth = new Label(window, "pass_&_auth", header);

		PasswordEditOverlay changePassOver = new PasswordEditOverlay(settings);

		changePass = new Button(window, "change_pass", 3.0, 16, 32);
		changePass.setFont(new Font(13, FontWeight.BOLD));

		changePass.setAction(() -> changePassOver.show(settings.getSession()));

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

		getChildren().addAll(myAccount, new FixedVSpace(20));

		if (!user.isEmailConfirmed()) {
			getChildren().add(unverifiedEmail);
		}

		user.emailConfirmedProperty().addListener((obs, ov, nv) -> {
			if (nv && getChildren().contains(unverifiedEmail)) {
				getChildren().remove(unverifiedEmail);
			} else if (!nv && !getChildren().contains(unverifiedEmail)) {
				getChildren().add(2, unverifiedEmail);
			}
		});

		getChildren().addAll(new ProfileOverview(settings), new FixedVSpace(40));
		separate();
		getChildren().addAll(new FixedVSpace(40), passAuth, new FixedVSpace(20), changePass, new FixedVSpace(28));

		applyStyle(window.getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		myAccount.setFill(style.getText1());
		passAuth.setFill(style.getText1());

		changePass.setTextFill(style.getText1());
		changePass.setFill(style.getAccent());
	}

}
