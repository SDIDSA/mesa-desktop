package mesa.app.pages.session.content.create_server;

import mesa.app.pages.Page;
import mesa.app.pages.session.content.create_server.pages.CreateServerPage;
import mesa.app.pages.session.content.create_server.pages.CustomizeServerPage;
import mesa.app.pages.session.content.create_server.pages.JoinServerPage;
import mesa.app.pages.session.content.create_server.pages.TellUsMorePage;

public class AddServerOverlay extends MultiOverlay {

	public AddServerOverlay(Page owner) {
		super(owner);

		addPage(new CreateServerPage(this));
		addPage(new TellUsMorePage(this));
		addPage(new CustomizeServerPage(this));
		addPage(new JoinServerPage(this));
	}

}
