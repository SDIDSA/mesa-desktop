package mesa.app.pages.session.items.image;

import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.items.BarItem;
import mesa.data.bean.Server;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.popup.tooltip.Tooltip;

public class ServerBarItem extends BarItem {
	private Server server;
	private ServerItemIcon ic;

	public ServerBarItem(SessionPage session, Server server) {
		super(session);
		this.server = server;
		ic = new ServerItemIcon(session, server.getIcon());

		setIcon(ic);
		setTooltip(new Tooltip(session.getWindow(), server.getName(), Direction.RIGHT));
	}

	public Server getServer() {
		return server;
	}

}
