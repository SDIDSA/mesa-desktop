package mesa.app.pages.session.types.server.center;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Channel;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.Search;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ChannelDisplayTop extends HBox implements Styleable {
	private ChannelTopIcon hashTag;
	private Text name;

	private Search search;

	private ArrayList<ChannelTopIcon> icons;

	public ChannelDisplayTop(SessionPage session, Channel channel) {
		setMinHeight(48);
		setMaxHeight(48);
		setPadding(new Insets(0, 8, 0, 8));
		setAlignment(Pos.CENTER_LEFT);

		hashTag = new ChannelTopIcon("channel_type_text", 18);

		name = new Text("", new Font(16, FontWeight.BOLD));
		name.textProperty().bind(channel.nameProperty());

		StackPane namePane = new StackPane(name);
		namePane.setAlignment(Pos.CENTER_LEFT);
		namePane.setMinWidth(0);

		Rectangle nameClip = new Rectangle();
		nameClip.widthProperty().bind(namePane.widthProperty().add(5));
		nameClip.heightProperty().bind(namePane.heightProperty());

		setHgrow(namePane, Priority.ALWAYS);

		nameClip.fillProperty().bind(Bindings.createObjectBinding(() -> {
			Stop[] stops = new Stop[] {
					new Stop(0, Color.gray(0, 1)),
					new Stop(1 - (20 / nameClip.getWidth()), Color.gray(0, 1)),
					new Stop(1, Color.gray(0, 0)) };
			return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		}, nameClip.widthProperty()));

		namePane.setClip(nameClip);

		icons = new ArrayList<>();

		ChannelTopIcon threads = new ChannelTopIcon("thread", "threads", 20);
		ChannelTopIcon notification = new ChannelTopIcon("notification", "notification_settings", 18);
		ChannelTopIcon pinned = new ChannelTopIcon("pin", "pinned_messages", 19);
		ChannelTopIcon people = new ChannelTopIcon("people", "show_members", 24);

		search = new Search(session.getWindow(), "search");
		search.setMaxHeight(24);
		search.fixWidth(144);
		initMargin(search);

		ChannelTopIcon inbox = new ChannelTopIcon("inbox", "inbox", 18);
		ChannelTopIcon help = new ChannelTopIcon("help", "help", 18);

		icons.add(hashTag);

		icons.add(threads);
		icons.add(notification);
		icons.add(pinned);
		icons.add(people);
		icons.add(inbox);
		icons.add(help);

		icons.forEach(this::initMargin);

		getChildren().addAll(hashTag, namePane, threads, notification, pinned, people, search, inbox, help);

		Timeline expandSearch = new Timeline(new KeyFrame(Duration.seconds(.2),
				new KeyValue(search.fixedWidthProperty(), 240, Interpolator.EASE_BOTH)));
		Timeline collapseSearch = new Timeline(new KeyFrame(Duration.seconds(.2),
				new KeyValue(search.fixedWidthProperty(), 144, Interpolator.EASE_BOTH)));

		search.focusProperty().addListener((obs, ov, nv) -> {
			if (nv.booleanValue()) {
				collapseSearch.stop();
				expandSearch.playFromStart();
			} else {
				if (!search.isMenuShowing()) {
					expandSearch.stop();
					collapseSearch.playFromStart();
				}
			}
		});

		search.applyLocale(session.getWindow().getLocale());
		search.applyStyle(session.getWindow().getStyl());
		applyStyle(session.getWindow().getStyl());
	}

	private void initMargin(Node node) {
		HBox.setMargin(node, new Insets(0, 8, 0, 8));
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBackgroundPrimary()));
		setEffect(style.getElevationLow());

		hashTag.setFill(style.getTextMuted());
		name.setFill(style.getHeaderPrimary());

		icons.forEach(icon -> icon.applyStyle(style));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
