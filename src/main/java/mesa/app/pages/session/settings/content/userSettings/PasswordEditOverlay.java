package mesa.app.pages.session.settings.content.userSettings;

import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.settings.Settings;

public class PasswordEditOverlay extends EditOverlay {

	private TextInputField currPass, newPass, confNewPass;

	public PasswordEditOverlay(Settings settings) {
		super(settings, "password");
		currPass = new TextInputField(settings.getWindow(), "current_password", 408, true);
		newPass = new TextInputField(settings.getWindow(), "new_pass", 408, true);
		confNewPass = new TextInputField(settings.getWindow(), "confirm_new_pass", 408, true);
		center.getChildren().addAll(currPass, newPass, confNewPass);
		
		form.addAll(currPass, newPass, confNewPass);
	}
}
