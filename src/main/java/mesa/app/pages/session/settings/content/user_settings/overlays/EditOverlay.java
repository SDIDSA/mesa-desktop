package mesa.app.pages.session.settings.content.user_settings.overlays;

import mesa.app.pages.session.SessionPage;

public abstract class EditOverlay extends BasicOverlay {

	protected EditOverlay(SessionPage session, String editWhat) {
		super(session);
		
		head.setKey("change_attr");
		head.addParam(0, "&" + editWhat);

		subHead.setKey("enter_attr");
		subHead.addParam(0, "&" + editWhat);
	}
}
