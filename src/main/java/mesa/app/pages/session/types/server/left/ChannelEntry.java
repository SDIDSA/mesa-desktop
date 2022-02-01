package mesa.app.pages.session.types.server.left;

import java.util.HashMap;
import java.util.function.Consumer;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.settings.ChannelSettings;
import mesa.app.pages.session.types.server.ServerContent;
import mesa.data.bean.Channel;
import mesa.data.bean.ChannelGroup;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ChannelEntry extends HBox implements Styleable {
	private static HashMap<Integer, ChannelEntry> selectedChannels = new HashMap<>();
	
	public static void clearCache() {
		selectedChannels.clear();
	}

	private BooleanProperty selected;
	private BooleanProperty active;

	private ColorIcon type;
	private Text name;

	private ActionIcon invite;
	private ActionIcon edit;
	
	private HBox content;
	
	private BooleanProperty unread;
	private Circle unreadMark;

	private ChannelSettings settings;
	
	private Channel channel;
	public ChannelEntry(SessionPage session, Channel channel) {
		channel.setChannelEntry(this);
		this.channel = channel;
		unread = channel.unreadProperty();
		
		setAlignment(Pos.CENTER);
		
		content = new HBox();
		content.setMinHeight(34);
		content.setMaxHeight(34);
		content.setAlignment(Pos.CENTER);
		content.setCursor(Cursor.HAND);
		content.setOnMouseClicked(e -> select());
		content.setOnKeyPressed(e-> {
			if(e.getCode().equals(KeyCode.SPACE)) {
				select();
			}
		});
		content.setPadding(new Insets(0, 8, 0, 8));
		setHgrow(content, Priority.ALWAYS);

		content.setFocusTraversable(true);

		selected = new SimpleBooleanProperty(false);
		active = new SimpleBooleanProperty(false);

		type = new ColorIcon("channel_type_" + channel.getType(), 16);
		setMargin(type, new Insets(0, 6, 0, 0));

		name = new Text("", new Font(Font.DEFAULT_FAMILY_MEDIUM, 15));
		name.textProperty().bind(channel.nameProperty());
		name.setTranslateY(-1);
		
		Consumer<Boolean> fontCons = nv -> {
			boolean v = nv.booleanValue();
			name.setFont(new Font(v ? Font.DEFAULT_FAMILY : Font.DEFAULT_FAMILY_MEDIUM, 15, v ? FontWeight.BOLD : FontWeight.NORMAL).getFont());
		};
		unread.addListener((obs, ov, nv) -> fontCons.accept(nv));
		
		fontCons.accept(unread.getValue());

		invite = new ActionIcon(session.getWindow(), "invite", 16, 20, "create_invite");
		Invite inviteOverlay = new Invite(session, channel.getGroup().getServer());
		invite.setAction(inviteOverlay::show);

		edit = new ActionIcon(session.getWindow(), "settings", 12, 20, "edit_channel");
		
		edit.setAction(()-> {
			if(settings == null) {
				settings = new ChannelSettings(session, channel);
			}
			session.showSettings(settings);
		});

		content.getChildren().addAll(type, name, new ExpandingHSpace(), invite);
		
		getChildren().addAll(content);

		adminCheck(session, channel.getGroup());
		channel.getGroup().getServer().ownerProperty().addListener(e -> adminCheck(session, channel.getGroup()));

		active.bind(hoverProperty().or(focusedProperty()).or(invite.focusedProperty()).or(edit.focusedProperty()));

		unreadMark = new Circle(4);
		unreadMark.setTranslateX(-5);
		unreadMark.visibleProperty().bind(unread);
		
		getChildren().add(0, unreadMark);
		
		Rectangle clip = new Rectangle();
		clip.widthProperty().bind(widthProperty());
		clip.heightProperty().bind(heightProperty());
		
		setClip(clip);
		
		applyStyle(session.getWindow().getStyl());
	}
	
	public Channel getChannel() {
		return channel;
	}

	private void adminCheck(SessionPage session, ChannelGroup group) {
		if (group.getServer().getOwner().equals(session.getUser().getId())) {
			if (!content.getChildren().contains(edit))
				content.getChildren().add(edit);
		} else {
			content.getChildren().remove(edit);
		}
	}

	public void select() {
		if (channel.getType().equals("text")) {
			int serverId = channel.getGroup().getServer().getId();
			ChannelEntry old = selectedChannels.get(serverId);
			if (old != null) {
				old.unselect();
			}

			selected.set(true);
			loadChannel();
			selectedChannels.put(serverId, this);
		}
	}
	
	public static ChannelEntry getSelected(int server) {
		return selectedChannels.get(server);
	}

	public ServerContent getServerContent() {
		return channel.getServerContent();
	}
	
	public void loadChannel() {
		getServerContent().loadChannel(channel);
	}

	public void unselect() {
		selected.set(false);
	}
	
	public boolean isSelected() {
		return selected.get();
	}

	@Override
	public void applyStyle(Style style) {
		type.setFill(style.getTextMuted());

		Background hoverBack = Backgrounds.make(style.getBackgroundModifierHover(), 4.0);
		Background activeBack = Backgrounds.make(style.getBackgroundModifierActive(), 4.0);
		Background selectedBack = Backgrounds.make(style.getBackgroundModifierSelected(), 4.0);

		content.backgroundProperty().bind(Bindings.when(selected).then(selectedBack).otherwise(Bindings.when(pressedProperty())
				.then(activeBack).otherwise(Bindings.when(active).then(hoverBack).otherwise(Background.EMPTY))));

		name.fillProperty().bind(Bindings.when(selected).then(style.getInteractiveActive()).otherwise(
				Bindings.when(active).then(style.getInteractiveHover()).otherwise(Bindings.when(unread).then(style.getInteractiveActive()).otherwise(style.getChannelsDefault()))));

		Consumer<ActionIcon> styleIcon = icon -> {
			icon.applyStyle(style);
			icon.visibleProperty().bind(active.or(selected));
			icon.fillProperty().bind(Bindings.when(icon.hoverProperty()).then(style.getInteractiveHover())
					.otherwise(style.getInteractiveNormal()));
		};

		styleIcon.accept(invite);
		styleIcon.accept(edit);

		NodeUtils.focusBorder(content, style.getTextLink());
		
		unreadMark.setFill(style.getInteractiveActive());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
