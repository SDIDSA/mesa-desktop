package mesa.app.pages.session.settings.content.user_settings.overlays;

import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.alert.BasicOverlay;

public abstract class EditOverlay extends BasicOverlay {

	protected EditOverlay(SessionPage session, String editWhat, boolean fem) {
		super(session);
		
		head.setKey("change_attr");
		head.addParam(0, "&" + editWhat);

		subHead.setKey("enter_attr");
		subHead.addParam(0, "&" + editWhat);
		subHead.addParam(1, "&a_new_" + (fem ? "fem" : "mal"));
	}
	
	protected EditOverlay(SessionPage session, String editWhat) {
		this(session, editWhat, false);
	}
}
