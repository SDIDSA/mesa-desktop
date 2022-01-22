package mesa.app.pages.session.types.server.center;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Channel;

public class ChannelDisplayCenter extends HBox {
	private BooleanProperty membersShown;
	
	private ChannelDisplayMain main;
	private MemberList members;

	public ChannelDisplayCenter(SessionPage session) {
		main = new ChannelDisplayMain(session);
		members = new MemberList(session);

		setHgrow(main, Priority.ALWAYS);
		
		membersShown = new SimpleBooleanProperty(false);
		
		members.minWidthProperty().bind(Bindings.when(membersShown).then(240).otherwise(0));

		getChildren().addAll(main, members);
	}
	
	public void loadChannel(Channel channel) {
		main.loadChannel(channel);
	}
	
	public BooleanProperty membersShown() {
		return membersShown;
	}
}
