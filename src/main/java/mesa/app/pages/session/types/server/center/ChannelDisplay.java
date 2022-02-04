package mesa.app.pages.session.types.server.center;

import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Channel;
import mesa.data.bean.Message;
import mesa.data.bean.Server;

public class ChannelDisplay {
	
	private ChannelDisplayTop top;
	private ChannelDisplayCenter center;
	
	public ChannelDisplay(SessionPage session, Server server) {
		top = new ChannelDisplayTop(session);
		center = new ChannelDisplayCenter(session, server);
		
		center.membersShown().bind(top.membersShown());
		
		top.setViewOrder(-1);
	}
	
	public void loadChannel(Channel channel) {
		top.loadChannel(channel);
		center.loadChannel(channel);
	}
	
	public ChannelDisplayTop getTop() {
		return top;
	}
	
	public ChannelDisplayCenter getCenter() {
		return center;
	}

	public boolean handleMessage(Message msg) {
		return center.handleMessage(msg);
	}
}
