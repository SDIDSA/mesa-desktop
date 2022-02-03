package mesa.gui.controls.check;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class Radio extends StackPane implements Styleable {
	private BooleanProperty checked;
	private BooleanProperty inverted;

	private Circle outer;
	private Circle inner;

	public Radio(Window window, double size) {
		setMinSize(size, size);
		setMaxSize(size, size);

		inverted = new SimpleBooleanProperty(false);
		checked = new SimpleBooleanProperty(false);

		outer = new Circle(size / 2);
		outer.setFill(Color.TRANSPARENT);
		outer.setStrokeWidth((int) (size / 10));
		outer.setStrokeType(StrokeType.INSIDE);
		
		inner = new Circle((int) ((size / 2) - (size / 4.5)));
		
		getChildren().addAll(outer, inner);

		inner.visibleProperty().bind(checked);

		setCursor(Cursor.HAND);
		setOnMouseClicked(e -> flip());

		applyStyle(window.getStyl());
	}
	
	public void flip() {
		setChecked(true);
	}
	
	public BooleanProperty checkedProperty() {
		return checked;
	}
	
	public void setChecked(boolean val) {
		checked.set(val);
	}

	@Override
	public void applyStyle(Style style) {
		inner.fillProperty().bind(Bindings.when(inverted).then(style.getInteractiveActive()).otherwise(style.getControlBrandForeground()));
		outer.strokeProperty().bind(Bindings.when(checked.or(inverted)).then(style.getInteractiveActive()).otherwise(style.getInteractiveNormal()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
