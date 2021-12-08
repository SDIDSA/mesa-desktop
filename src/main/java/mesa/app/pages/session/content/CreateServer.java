package mesa.app.pages.session.content;

import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.items.ColorBarItem;
import mesa.app.utils.Colors;

public class CreateServer extends ItemOverlay {

	public CreateServer(SessionPage session) {
		setItem(new ColorBarItem(session, Colors.GREEN, "add_server", "plus", 16));
		setOverlay(new CreateServerOverlay(session));
	}

}
