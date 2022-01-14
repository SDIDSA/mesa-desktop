package mesa.app.pages.session.types.server.left;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.ChannelGroup;
import mesa.data.bean.Server;

public class ServerSideCenter extends VBox {

	public ServerSideCenter(SessionPage session, Server server) {
		setPadding(new Insets(0, 8, 0, 0));

		server.getGroups().forEach(group -> addGroup(session, group));
	}

	public void addGroup(SessionPage session, ChannelGroup group) {
		getChildren().add(new ChannelGroupEntry(session, group));
	}
}
