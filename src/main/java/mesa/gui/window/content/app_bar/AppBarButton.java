package mesa.gui.window.content.app_bar;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Cursor;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class AppBarButton extends ColorIcon implements Styleable {
	public AppBarButton(Window window, String name) {
		super("window_" + name, 14);

		setPickOnBounds(true);
		setCursor(Cursor.HAND);

		applyStyle(window.getStyl());
	}

	public void setIcon(String name) {
		super.setName("window_" + name);
	}

	@Override
	public void applyStyle(Style style) {
		fillProperty().unbind();
		fillProperty()
				.bind(Bindings.when(hoverProperty()).then(style.getInteractiveHover()).otherwise(style.getInteractiveNormal()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
