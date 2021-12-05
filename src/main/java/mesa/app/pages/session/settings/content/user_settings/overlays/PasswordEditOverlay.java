package mesa.app.pages.session.settings.content.user_settings.overlays;

import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.SessionPage;

public class PasswordEditOverlay extends EditOverlay {

	private TextInputField currPass;
	private TextInputField newPass;
	private TextInputField confNewPass;

	public PasswordEditOverlay(SessionPage session) {
		super(session, "password");
		currPass = new TextInputField(session.getWindow(), "current_password", 408, true);
		newPass = new TextInputField(session.getWindow(), "new_password", 408, true);
		confNewPass = new TextInputField(session.getWindow(), "confirm_new_password", 408, true);
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
