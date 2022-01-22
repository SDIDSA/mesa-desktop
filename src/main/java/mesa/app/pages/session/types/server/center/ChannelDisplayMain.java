package mesa.app.pages.session.types.server.center;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Channel;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.image.layer_icon.CircledAdd;
import mesa.gui.controls.input.ChannelTextInput;
import mesa.gui.controls.space.FixedHSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ChannelDisplayMain extends BorderPane implements Styleable {

	private ChannelTextInput input;
	
	private CircledAdd attach;
	private ColorIcon gif;
	private ColorIcon sticker;

	private EmojiInputIcon emojis;

	public ChannelDisplayMain(SessionPage session) {
		VBox bottom = new VBox();
		bottom.setPadding(new Insets(0, 16, 0, 16));
		bottom.setMinHeight(68);

		attach = new CircledAdd(20);
		attach.setPadding(new Insets(10, 14, 10, 16));

		input = new ChannelTextInput(session.getWindow(), new Font(15), "");
		input.setMaxHeight(44);
		input.setFieldPadding(new Insets(11, 0, 11, 0));
		input.setRadius(8.0);
		input.applyStyle(session.getWindow().getStyl().get());
		input.setPadding(new Insets(0, 16, 0, 0));
		
		gif = new ColorIcon("gif", 24);
		sticker = new ColorIcon("sticker", 24);
		emojis = new EmojiInputIcon(22);
		
		input.addPreField(attach);
		
		input.addPostField(gif, new FixedHSpace(16), sticker, new FixedHSpace(13), emojis);

		bottom.getChildren().add(input);
		setBottom(bottom);

		applyStyle(session.getWindow().getStyl());
	}

	public void loadChannel(Channel channel) {
		input.setPrompt("Message #" + channel.getName());
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

}
