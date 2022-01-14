package mesa.gui.controls.check;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class Check extends StackPane implements Styleable {
	private BooleanProperty checked;

	public Check(Window window, double size) {
		setMinSize(size, size);
		setMaxSize(size, size);

		checked = new SimpleBooleanProperty(false);

		Pane check = new Pane();
		check.setMinSize(size / 3.5, size / 1.5);
		check.setMaxSize(size / 3.5, size / 1.5);
		check.setBorder(Borders.make(Color.WHITE, new BorderWidths(0, 2, 2, 0)));
		check.setTranslateY(-size / 12);
		check.setRotate(45);
		check.setMouseTransparent(true);

		getChildren().add(check);

		check.visibleProperty().bind(checked);

		setCursor(Cursor.HAND);
		setOnMouseClicked(e -> {
			checked.set(!checked.get());
		});

		applyStyle(window.getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		borderProperty().bind(Bindings.when(checked).then(Borders.make(style.getAccent(), 2.0, 2.0))
				.otherwise(Borders.make(style.getChannelsDefault(), 2.0, 2.0)));
		backgroundProperty().bind(
				Bindings.when(checked).then(Backgrounds.make(style.getAccent(), 2.0)).otherwise(Background.EMPTY));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

	public BooleanProperty checkedProperty() {
		return checked;
	}
}
