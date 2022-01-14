package mesa.app.pages.session.types.server.left;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Channel;
import mesa.data.bean.ChannelGroup;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ChannelGroupEntry extends VBox implements Styleable {

	private HBox header;

	private ColorIcon expand;
	private Text nameDisp;

	private ActionIcon addChannel;

	public ChannelGroupEntry(SessionPage session, ChannelGroup group) {
		setPadding(new Insets(16, 0, 0, 0));

		header = new HBox();
		header.setAlignment(Pos.CENTER);
		header.setCursor(Cursor.HAND);
		VBox.setMargin(header, new Insets(0, 0, 4, 0));

		expand = new ColorIcon("expand", 8);
		expand.setPadding(6);
		nameDisp = new Text("", new Font(13, FontWeight.BOLD));
		nameDisp.textProperty().bind(Bindings.createStringBinding(() -> TextTransform.UPPERCASE.apply(group.getName()),
				group.nameProperty()));

		addChannel = new ActionIcon(session.getWindow(), "plus", 12, 24, "create_channel");

		header.getChildren().addAll(expand, nameDisp, new ExpandingHSpace());

		adminCheck(session, group);

		group.getServer().ownerProperty().addListener(e -> adminCheck(session, group));

		getChildren().add(header);

		group.getChannels().forEach(channel -> addChannel(session, channel));

		applyStyle(session.getWindow().getStyl());
	}

	private void adminCheck(SessionPage session, ChannelGroup group) {
		if (group.getServer().getOwner().equals(session.getUser().getId())) {
			if (!header.getChildren().contains(addChannel))
				header.getChildren().add(addChannel);
		} else {
			header.getChildren().remove(addChannel);
		}
	}

	public void addChannel(SessionPage session, Channel channel) {
		getChildren().add(new ChannelEntry(session, channel));
	}

	@Override
	public void applyStyle(Style style) {
		addChannel.applyStyle(style);

		expand.fillProperty().bind(Bindings.when(header.hoverProperty()).then(style.getInteractiveHover())
				.otherwise(style.getChannelsDefault()));
		nameDisp.fillProperty().bind(expand.fillProperty());

		addChannel.fillProperty().bind(Bindings.when(addChannel.hoverProperty()).then(style.getInteractiveHover())
				.otherwise(style.getChannelsDefault()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
