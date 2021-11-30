package mesa.app.pages.session.settings.content.user_settings.overlays;

import mesa.app.component.input.ConfCode;
import mesa.app.pages.session.SessionPage;

public class VerifyEmailOverlay extends BasicOverlay {
	private SessionPage session;
	
	private ConfCode confCode;

	public VerifyEmailOverlay(SessionPage session) {
		super(session, 512);
		
		this.session = session;

		head.setKey("verify_now");

		subHead.setKey("email_unverified");

		confCode = new ConfCode(session.getWindow(), "verification_code");

		center.getChildren().add(confCode);

		form.addAll(confCode);
	}

	public String getValue() {
		return confCode.getValue();
	}

	@Override
	public void hide() {
		confCode.clear();
		super.hide();
	}
	
	@Override
	public void show() {
		subHead.addParam(0, session.getUser().getEmail());
		super.show();
	}

}
