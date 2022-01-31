package mesa.app.pages.session.types.server;

import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.content.UserBar;
import mesa.app.pages.session.items.image.ServerBarItem;
import mesa.app.pages.session.types.server.left.ServerSideCenter;
import mesa.app.pages.session.types.server.left.ServerSideTop;
import mesa.data.bean.Server;

public class ServerContent extends Content {

	private Server server;

	public ServerContent(SessionPage session, Server server) {
		super(session);
		this.server = server;
		
		ServerBarItem item = new ServerBarItem(session, server);
		
		if(server.isUnread()) {
			item.setUnread(true);
		}
		server.unreadProperty().addListener((obs, ov, nv) -> {
			item.setUnread(nv.booleanValue());
		});
		
		setItem(item);

		ServerSideTop sideTop = new ServerSideTop(session, server);
		ServerSideCenter sideCenter = new ServerSideCenter(session, server);
		UserBar sideBot = new UserBar(session);

		getSide().setTop(sideTop);
		getSide().setCenter(sideCenter);
		getSide().setBottom(sideBot);
	}

	public Server getServer() {
		return server;
	}

}
