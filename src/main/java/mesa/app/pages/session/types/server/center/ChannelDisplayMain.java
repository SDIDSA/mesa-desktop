package mesa.app.pages.session.types.server.center;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONObject;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import mesa.api.Session;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Channel;
import mesa.data.bean.Message;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.image.layer_icon.CircledAdd;
import mesa.gui.controls.input.ChannelTextInput;
import mesa.gui.controls.scroll.ScrollBar;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ChannelDisplayMain extends BorderPane implements Styleable {
	private SessionPage session;

	private ChannelTextInput input;

	private CircledAdd attach;
	private ColorIcon gif;
	private ColorIcon sticker;

	private EmojiInputIcon emojis;

	private Channel channel;

	public ChannelDisplayMain(SessionPage session) {
		setMinHeight(0);
		setMaxHeight(-1);

		this.session = session;

		VBox bottom = new VBox();
		bottom.setPadding(new Insets(0, 16, 23, 16));

		attach = new CircledAdd(20);
		attach.setPadding(new Insets(12, 14, 12, 16));

		input = new ChannelTextInput(session.getWindow(), new Font(16), "");
		input.setMinHeight(44);
		input.setFieldPadding(new Insets(12, 0, 12, 0));
		input.setRadius(8.0);
		input.applyStyle(session.getWindow().getStyl().get());

		input.setAction(() -> {
			String text = input.getValue();
			if (!text.isBlank()) {
				Message message = new Message(-1, channel.getId(), session.getUser().getId(), text.trim(), "");
				message.setType("text");
				MessageDisp disp = message.getDisplay(session);
				disp.setOpacity(.3);

				loadedChannel.addMessage(session, message);

				input.clear();
				Session.sendMessage(message.getContent(), channel.getId(), channel.getGroup().getServer().getId(),
						result -> {
							message.setId(result.getInt("id"));
							message.setTime(result.getString("time"));
							disp.setOpacity(1);
						});
			}
		});

		input.setPadding(new Insets(0, 5, 0, 0));

		gif = new ColorIcon("gif", 24);
		gif.setPadding(8);
		gif.setTranslateY(2);
		sticker = new ColorIcon("sticker", 24);
		sticker.setPadding(8);
		sticker.setTranslateY(2);

		emojis = new EmojiInputIcon(22);
		emojis.setPadding(new Insets(8));
		emojis.setTranslateY(3);
		emojis.setMaxHeight(38);

		input.addPreField(attach);

		input.addPostField(gif, sticker, emojis);

		bottom.getChildren().add(input);

		setBottom(bottom);
		bottom.setViewOrder(-1);

		applyStyle(session.getWindow().getStyl());
	}

	private static HashMap<Integer, ChannelMessages> displayCache = new HashMap<>();
	private static ChannelMessages loadedChannel = null;

	public void loadChannel(Channel channel) {
		this.channel = channel;
		input.setPrompt("Message #" + channel.getName());

		ChannelMessages found = displayCache.get(channel.getId());
		if (found == null) {
			final ChannelMessages created = new ChannelMessages(session);
			displayCache.put(channel.getId(), created);
			Session.getMessages(channel.getId(), result -> {
				ArrayList<Message> msgs = new ArrayList<>();
				result.getJSONArray("data").forEach(obj -> msgs.add(Message.get((JSONObject) obj)));

				Collections.sort(msgs);

				msgs.forEach(msg -> created.addMessage(session, msg));
			});
			found = created;
			found.sceneProperty().addListener((obs, ov, nv) -> {
				if (nv != null && channel.isUnread()) {
					Session.seen(channel.getId(), result -> channel.setUnread(false));
				}
			});

		}
		setCenter(found);
		loadedChannel = found;
		found.scrollToBottom();
	}

	public boolean handleMessage(Message msg) {
		ChannelMessages found = displayCache.get(msg.getChannel());

		if (found != null) {
			found.addMessage(session, msg);
			return found == loadedChannel;
		} else {
			return false;
		}
	}

	public static void clearCache() {
		displayCache.clear();
		loadedChannel = null;
	}

	@Override
	public void applyStyle(Style style) {
		initInputIconFill(attach, attach.circleFillProperty(), style);
		initInputIconFill(gif, gif.fillProperty(), style);
		initInputIconFill(sticker, sticker.fillProperty(), style);

		attach.setSignFill(style.getChanneltextareaBackground());
	}

	private void initInputIconFill(Node node, ObjectProperty<Paint> fill, Style style) {
		fill.bind(Bindings.when(node.hoverProperty()).then(style.getInteractiveHover())
				.otherwise(style.getInteractiveNormal()));
		node.setCursor(Cursor.HAND);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

	private static class ChannelMessages extends StackPane implements Styleable {
		private VBox list;
		private ScrollBar sb;

		public ChannelMessages(SessionPage session) {
			setAlignment(Pos.TOP_CENTER);
			setMinHeight(0);
			setMaxHeight(-1);

			list = new VBox(20);
			list.setAlignment(Pos.BOTTOM_CENTER);
			list.setPadding(new Insets(16));

			sb = new ScrollBar(16, 4);
			sb.install(this, list);

			Rectangle clip = new Rectangle();
			clip.widthProperty().bind(widthProperty());
			clip.heightProperty().bind(heightProperty().add(10));
			setClip(clip);

			getChildren().addAll(list, sb);

			applyStyle(session.getWindow().getStyl());
		}

		public void addMessage(SessionPage session, Message msg) {
			MessageDisp disp = msg.getDisplay(session);

			if (msg.getType().equals("text") && !list.getChildren().contains(disp)) {
				list.getChildren().add(disp);
			}
		}

		public void scrollToBottom() {
			sb.scrollToBottom();
		}

		@Override
		public void applyStyle(Style style) {
			sb.setTrackFill(style.getScrollbarAutoTrack());
			sb.setThumbFill(style.getScrollbarAutoThumb());
		}

		@Override
		public void applyStyle(ObjectProperty<Style> style) {
			Styleable.bindStyle(this, style);
		}
	}
}
