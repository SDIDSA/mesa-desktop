package mesa.app.pages.session.types.server.center;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.types.server.center.right.MemberList;
import mesa.data.bean.Channel;
import mesa.data.bean.Message;
import mesa.data.bean.Server;
import mesa.data.bean.User;

public class ChannelDisplayCenter extends HBox {
	private BooleanProperty membersShown;
	
	private ChannelDisplayMain main;
	private MemberList members;

	public ChannelDisplayCenter(SessionPage session, Server server) {
		main = new ChannelDisplayMain(session);
		members = new MemberList(session, server);

		setHgrow(main, Priority.ALWAYS);
		
		membersShown = new SimpleBooleanProperty(false);
		
		members.minWidthProperty().bind(Bindings.when(membersShown).then(240).otherwise(0));
		members.maxWidthProperty().bind(members.minWidthProperty());
		members.visibleProperty().bind(membersShown);

		getChildren().addAll(main, members);
	}
	
	public void loadChannel(Channel channel) {
		main.loadChannel(channel);
	}
	
	public BooleanProperty membersShown() {
		return membersShown;
	}

	public boolean handleMessage(Message msg) {
		return main.handleMessage(msg);
	}

	public void addMember(String userId) {
		User.getForId(userId, members::preAddUser);
	}
}
