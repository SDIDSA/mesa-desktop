package mesa.app.pages.session.types.server;

import javafx.beans.property.ObjectProperty;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.content.UserBar;
import mesa.app.pages.session.items.image.ServerBarItem;
import mesa.app.pages.session.types.server.left.ServerSideCenter;
import mesa.app.pages.session.types.server.left.ServerSideTop;
import mesa.data.bean.Server;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ServerContent extends Content implements Styleable {

	public ServerContent(SessionPage session, Server server) {
		super(session);
		
		setItem(new ServerBarItem(session, server));
		
		ServerSideTop sideTop = new ServerSideTop(session, server);
		ServerSideCenter sideCenter = new ServerSideCenter(session, server);
		UserBar sideBot = new UserBar(session);
		
		getSide().setTop(sideTop);
		getSide().setCenter(sideCenter);
		getSide().setBottom(sideBot);
	}

	@Override
	public void applyStyle(Style style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
