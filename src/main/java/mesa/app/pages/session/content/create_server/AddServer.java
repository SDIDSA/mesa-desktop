package mesa.app.pages.session.content.create_server;

import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.content.OverlayItem;
import mesa.app.pages.session.items.color.ColorBarItem;
import mesa.app.utils.Colors;

public class AddServer extends OverlayItem {

	public AddServer(SessionPage session) {
		setItem(new ColorBarItem(session, Colors.GREEN, "add_server", "plus", 16));
		setOverlay(new AddServerOverlay(session));
	}

}
