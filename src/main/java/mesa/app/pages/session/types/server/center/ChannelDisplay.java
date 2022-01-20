package mesa.app.pages.session.types.server.center;

import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Channel;

public class ChannelDisplay {
	
	private ChannelDisplayTop top;
	
	public ChannelDisplay(SessionPage session, Channel channel) {
		top = new ChannelDisplayTop(session, channel);
	}
	
	public ChannelDisplayTop getTop() {
		return top;
	}
}
