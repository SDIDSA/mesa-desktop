package mesa.app.pages.session.types.server;

import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.content.UserBar;
import mesa.app.pages.session.items.image.ServerBarItem;
import mesa.app.pages.session.types.server.center.ChannelDisplay;
import mesa.app.pages.session.types.server.center.NoTextChannel;
import mesa.app.pages.session.types.server.left.ChannelEntry;
import mesa.app.pages.session.types.server.left.ServerSideCenter;
import mesa.app.pages.session.types.server.left.ServerSideTop;
import mesa.data.bean.Channel;
import mesa.data.bean.ChannelGroup;
import mesa.data.bean.Message;
import mesa.data.bean.Server;

public class ServerContent extends Content {

	private Server server;

	private ServerSideCenter sideCenter;
	private ChannelDisplay channelDisplay;
	
	private NoTextChannel noTextChannel;

	public ServerContent(SessionPage session, Server server) {
		super(session);
		server.setServerContent(this);
		this.server = server;

		ServerBarItem item = new ServerBarItem(session, server);

		if (server.isUnread()) {
			item.setUnread(true);
		}
		server.unreadProperty().addListener((obs, ov, nv) -> item.setUnread(nv.booleanValue()));

		setItem(item);

		ServerSideTop sideTop = new ServerSideTop(session, server);
		sideCenter = new ServerSideCenter(session, server);
		UserBar sideBot = new UserBar(session);

		channelDisplay = new ChannelDisplay(session, server);

		getSide().setTop(sideTop);
		getSide().setCenter(sideCenter);
		getSide().setBottom(sideBot);
		
		noTextChannel = new NoTextChannel(session);
		
		addOnLoad(()-> {		
			if(ChannelEntry.getSelected(server.getId()) == null) {
				loadFirst();
			}
		});
	}
	
	public void loadFirst() {
		for(ChannelGroup group : server.getGroups()) {
			for(Channel channel : group.getChannels()) {
				if(channel.isTextChannel()) {
					channel.getChannelEntry().select();
					return;
				}
			}
		}
		ChannelEntry.clearSelected(server.getId());
		getMain().setCenter(noTextChannel);
	}

	public ChannelDisplay getChannelDisplay() {
		return channelDisplay;
	}

	public Server getServer() {
		return server;
	}

	public void loadChannel(Channel channel) {
		channelDisplay.loadChannel(channel);

		getMain().setTop(channelDisplay.getTop());
		getMain().setCenter(channelDisplay.getCenter());
	}

	public void removeChannel(int channelId) {
		sideCenter.removeChannel(channelId);
	}

	public void addMember(String userId) {
		channelDisplay.addMember(userId);
	}

	public void removeSelectedChannel() {
		getMain().getChildren().clear();
		loadFirst();
	}
	
	public boolean handleMessage(Message msg) {
		return channelDisplay.handleMessage(msg);
	}

}
