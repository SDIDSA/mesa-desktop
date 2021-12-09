package mesa.app.pages.session.content.create_server;

import mesa.app.pages.Page;

public class CreateServerOverlay extends MultiOverlay {

	public CreateServerOverlay(Page owner) {
		super(owner);

		addPage(new CreateServerPage(this));
		addPage(new TellUsMorePage(this));
	}

}
