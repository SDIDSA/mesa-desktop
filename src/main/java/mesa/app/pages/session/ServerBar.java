package mesa.app.pages.session;

import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.content.OverlayItem;
import mesa.app.pages.session.content.create_server.AddServer;
import mesa.app.pages.session.items.BarItem;
import mesa.app.pages.session.items.color.ColorBarItem;
import mesa.app.pages.session.items.image.ServerBarItem;
import mesa.app.pages.session.types.home.Home;
import mesa.app.pages.session.types.server.ServerContent;
import mesa.app.pages.session.types.server.left.ChannelEntry;
import mesa.app.utils.Colors;
import mesa.data.bean.Channel;
import mesa.data.bean.Message;
import mesa.data.bean.Server;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Animator;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ServerBar extends VBox implements Styleable {
	private SessionPage session;

	private ArrayList<Rectangle> seps;

	private ArrayList<ServerContent> servers;

	public ServerBar(SessionPage session) {
		super(8);
		this.session = session;

		setAlignment(Pos.TOP_CENTER);
		setPadding(new Insets(8, 0, 0, 0));
		setMinWidth(72);

		NodeUtils.nestedFocus(this);

		seps = new ArrayList<>();

		Home home = new Home(session);

		addContent(home);
		separate();
		addOverlay(new AddServer(session));

		addItem(new ColorBarItem(session, Colors.GREEN, "discover", "compass", 20));

		servers = new ArrayList<>();
		applyStyle(session.getWindow().getStyl());
	}

	public void addServer(ServerContent content) {
		Server thisServer = ((ServerBarItem) content.getItem()).getServer();
		servers.add(content);
		for (int i = 2; i < getChildren().size(); i++) {
			Node nodeAt = getChildren().get(i);

			if (nodeAt instanceof ServerBarItem serverAt) {
				Server otherServer = serverAt.getServer();

				if (thisServer.getOrder() > otherServer.getOrder()) {

					addContent(i, content, true);
					break;
				}
			} else {
				addContent(i, content, true);
				break;
			}
		}
	}

	private void addContent(Content content) {
		addItem(content.getItem());
	}

	private void addContent(int index, Content content, boolean animate) {
		addItem(index, content.getItem(), animate);
	}

	private void addOverlay(OverlayItem overlay) {
		addItem(overlay.getItem());
	}

	private void addItem(BarItem item) {
		getChildren().add(item);
	}

	private void addItem(int index, BarItem item, boolean animate) {
		if (animate) {
			item.setOpacity(0);
			item.setMaxHeight(0);
			item.setTranslateX(-48);
			item.setTranslateY(24);
			Timeline show = new Timeline(new KeyFrame(Duration.seconds(.2),
					new KeyValue(item.translateXProperty(), 0, Interpolator.EASE_BOTH),
					new KeyValue(item.translateYProperty(), 0, Interpolator.EASE_BOTH)));

			Animator.show(item, 48);
			show.playFromStart();
		}
		getChildren().add(index, item);
	}

	public void separate() {
		Rectangle sep = new Rectangle(32, 2);
		seps.add(sep);

		getChildren().add(sep);
	}

	public void handleMessage(Message msg) {
		ServerContent found = findServerContent(content -> content.getServer().hasChannel(msg.getChannel()));
		Channel ch = found.getServer().getChannel(msg.getChannel());
		if(msg.getSender().equals(session.getUser().getId())) {
			ch.setUnread(false);
		}else {
			boolean handled = found.handleMessage(msg) && session.isLoaded(found);
			if (!handled) {
				ch.setUnread(true);
			}
		}
		
	}

	public void removeChannel(int serverId, int channelId) {
		ServerContent found = findServerContent(serverId);
		Server server = found.getServer();
		server.removeChannel(channelId);
		found.removeChannel(channelId);
	}

	public void addChannel(int serverId, int groupId, Channel channel) {
		ServerContent found = findServerContent(serverId);
		Server server = found.getServer();
		server.addChannel(groupId, channel);
		channel.getChannelGroupEntry().addChannel(session, channel);
		if (ChannelEntry.getSelected(server.getId()) == null) {
			found.loadFirst();
		}
	}

	public void addMember(int serverId, String userId) {
		if (session.getUser().getId().equals(userId)) {
			return;
		}

		ServerContent found = findServerContent(serverId);
		Server server = found.getServer();
		server.addMember(userId);
		found.addMember(userId);
	}

	private ServerContent findServerContent(int serverId) {
		return findServerContent(content -> content.getServer().getId().equals(serverId));
	}

	private ServerContent findServerContent(Predicate<ServerContent> condition) {
		for (ServerContent serverContent : servers) {
			if (condition.test(serverContent)) {
				return serverContent;
			}
		}
		throw new IllegalStateException("Can't find a server that meets condition");
	}

	@Override
	public void applyStyle(Style style) {
		seps.forEach(sep -> sep.setFill(style.getBackgroundModifierAccent()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
