package mesa.app.pages.session.settings.content.user_settings.overlays;

import mesa.app.component.input.DeprecatedTextInputField;
import mesa.app.pages.session.SessionPage;

public class PasswordEditOverlay extends EditOverlay {

	private DeprecatedTextInputField currPass;
	private DeprecatedTextInputField newPass;
	private DeprecatedTextInputField confNewPass;

	public PasswordEditOverlay(SessionPage session) {
		super(session, "password");
		currPass = new DeprecatedTextInputField(session.getWindow(), "current_password", 408, true);
		newPass = new DeprecatedTextInputField(session.getWindow(), "new_password", 408, true);
		confNewPass = new DeprecatedTextInputField(session.getWindow(), "confirm_new_password", 408, true);
		center.getChildren().addAll(currPass, newPass, confNewPass);
		
		addOnShown(currPass::requestFocus);
		
		form.addAll(currPass, newPass, confNewPass);
	}
	
	public String getCurrPass() {
		return currPass.getValue();
	}
	
	public String getConfNewPass() {
		return confNewPass.getValue();
	}
	
	@Override
	public void hide() {
		form.clear();
		super.hide();
	}
}
