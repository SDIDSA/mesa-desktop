package mesa.app.pages.session.types.server.left;

import javafx.animation.FillTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Server;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ServerSideTop extends StackPane implements Styleable {

	private Rectangle filler;

	private ColorIcon expand;
	private Text name;
	
	private FillTransition hover;
	private FillTransition unhover;

	public ServerSideTop(SessionPage session, Server server) {
		setMinHeight(48);
		setMaxHeight(48);

		name = new Text("", new Font(15, FontWeight.BOLD));
		name.textProperty().bind(server.nameProperty());

		expand = new ColorIcon("expand", 10, true);
		expand.setPadding(4);

		setCursor(Cursor.HAND);

		HBox content = new HBox();
		content.setPadding(new Insets(0, 16, 0, 16));
		content.setAlignment(Pos.CENTER);
		content.setMinHeight(48);
		content.setMaxHeight(48);

		filler = new Rectangle();
		filler.setFill(Color.TRANSPARENT);
		content.backgroundProperty().bind(Bindings.createObjectBinding(
				() -> Backgrounds.make(filler.getFill(), new CornerRadii(8.0, 0, 0, 0, false)), filler.fillProperty()));
		
		content.getChildren().addAll(name, new ExpandingHSpace(), expand);

		getChildren().add(content);
		
		setOnMouseEntered(e -> {
			unhover.stop();
			hover.playFromStart();
		});
		
		setOnMouseExited(e -> {
			hover.stop();
			unhover.playFromStart();
		});
		
		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		expand.applyStyle(style);
		name.setFill(style.getHeaderPrimary());
		expand.setFill(style.getHeaderPrimary());
		setEffect(style.getElevationLow());

		setBackground(Backgrounds.make(style.getBackgroundSecondary(), new CornerRadii(8.0, 0, 0, 0, false)));
		
		hover = new FillTransition(Duration.seconds(.15), style.getBackgroundModifierHover().deriveColor(0, 1, 1, 0), style.getBackgroundModifierHover());
		hover.setShape(filler);
		unhover = new FillTransition(Duration.seconds(.15), style.getBackgroundModifierHover(), style.getBackgroundModifierHover().deriveColor(0, 1, 1, 0));
		unhover.setShape(filler);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
