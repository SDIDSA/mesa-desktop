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
import javafx.scene.layout.VBox;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.types.server.center.ChannelDisplay;
import mesa.data.bean.Channel;
import mesa.data.bean.ChannelGroup;
import mesa.data.bean.Server;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ChannelEntry extends HBox implements Styleable {
	private static HashMap<Server, ChannelDisplay> displayCache = new HashMap<>();
	private static HashMap<Integer, ChannelEntry> selectedChannels = new HashMap<>();

	private BooleanProperty selected;
	private BooleanProperty active;

	private ColorIcon type;
	private Text name;

	private ActionIcon invite;
	private ActionIcon edit;

	public ChannelEntry(SessionPage session, Channel channel) {
		setMinHeight(34);
		setMaxHeight(34);
		setAlignment(Pos.CENTER);
		setCursor(Cursor.HAND);
		setOnMouseClicked(e -> select(session, channel));
		setOnKeyPressed(e-> {
			if(e.getCode().equals(KeyCode.SPACE)) {
				select(session, channel);
			}
		});

		setFocusTraversable(true);

		setPadding(new Insets(0, 8, 0, 8));

		selected = new SimpleBooleanProperty(false);
		active = new SimpleBooleanProperty(false);

		type = new ColorIcon("channel_type_" + channel.getType(), 16);
		setMargin(type, new Insets(0, 6, 0, 0));

		name = new Text("", new Font(Font.DEFAULT_FAMILY_MEDIUM, 15));
		name.textProperty().bind(channel.nameProperty());

		invite = new ActionIcon(session.getWindow(), "invite", 16, 20, "create_invite");
		Invite inviteOverlay = new Invite(session, channel.getGroup().getServer());
		invite.setAction(inviteOverlay::show);

		edit = new ActionIcon(session.getWindow(), "settings", 12, 20, "edit_channel");

		VBox.setMargin(this, new Insets(0, 0, 0, 8));

		getChildren().addAll(type, name, new ExpandingHSpace(), invite);

		adminCheck(session, channel.getGroup());
		channel.getGroup().getServer().ownerProperty().addListener(e -> adminCheck(session, channel.getGroup()));

		active.bind(hoverProperty().or(focusedProperty()).or(invite.focusedProperty()).or(edit.focusedProperty()));

		applyStyle(session.getWindow().getStyl());
	}

	private void adminCheck(SessionPage session, ChannelGroup group) {
		if (group.getServer().getOwner().equals(session.getUser().getId())) {
			if (!getChildren().contains(edit))
				getChildren().add(edit);
		} else {
			getChildren().remove(edit);
		}
	}

	public void select(SessionPage session, Channel channel) {
		if (channel.getType().equals("text")) {
			int serverId = channel.getGroup().getServer().getId();
			ChannelEntry old = selectedChannels.get(serverId);
			if (old != null) {
				old.unselect();
			}

			selected.set(true);
			loadChannel(session, channel);
			selectedChannels.put(serverId, this);
		}
	}

	public void loadChannel(SessionPage session, Channel channel) {
		Server server = channel.getGroup().getServer();
		ChannelDisplay display = displayCache.get(server);

		if (display == null) {
			display = new ChannelDisplay(session);

			displayCache.put(server, display);
		}

		display.loadChannel(channel);
		
		session.getLoaded().getMain().setTop(display.getTop());
		session.getLoaded().getMain().setCenter(display.getCenter());
	}

	public void unselect() {
		selected.set(false);
	}

	@Override
	public void applyStyle(Style style) {
		type.setFill(style.getTextMuted());

		Background hoverBack = Backgrounds.make(style.getBackgroundModifierHover(), 4.0);
		Background activeBack = Backgrounds.make(style.getBackgroundModifierActive(), 4.0);
		Background selectedBack = Backgrounds.make(style.getBackgroundModifierSelected(), 4.0);

		backgroundProperty().bind(Bindings.when(selected).then(selectedBack).otherwise(Bindings.when(pressedProperty())
				.then(activeBack).otherwise(Bindings.when(active).then(hoverBack).otherwise(Background.EMPTY))));

		name.fillProperty().bind(Bindings.when(selected).then(style.getInteractiveActive()).otherwise(
				Bindings.when(active).then(style.getInteractiveHover()).otherwise(style.getChannelsDefault())));

		Consumer<ActionIcon> styleIcon = icon -> {
			icon.applyStyle(style);
			icon.visibleProperty().bind(active.or(selected));
			icon.fillProperty().bind(Bindings.when(icon.hoverProperty()).then(style.getInteractiveHover())
					.otherwise(style.getInteractiveNormal()));
		};

		styleIcon.accept(invite);
		styleIcon.accept(edit);

		NodeUtils.focusBorder(this, style.getTextLink());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
