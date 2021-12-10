package mesa.app.pages.session.content.create_server;

import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.content.OverlayItem;
import mesa.app.pages.session.items.ColorBarItem;
import mesa.app.utils.Colors;

public class CreateServer extends OverlayItem {

	public CreateServer(SessionPage session) {
		setItem(new ColorBarItem(session, Colors.GREEN, "add_server", "plus", 16));
		setOverlay(new CreateServerOverlay(session));
	}

}
