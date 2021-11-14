package mesa.gui.controls.input.combo;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public abstract class ComboMenuItem extends StackPane implements Styleable {
	
	public ComboMenuItem(Window window) {
		setAlignment(Pos.CENTER_LEFT);
		
		setPadding(new Insets(15));
		
		setMinWidth(150);
		setMaxWidth(150);
		
		setCursor(Cursor.HAND);
	}
	
	@Override
	public void applyStyle(Style style) {		
		backgroundProperty().unbind();
		
		Background hover = Backgrounds.make(style.getBack5());
		backgroundProperty().bind(Bindings.when(hoverProperty()).then(hover).otherwise(Background.EMPTY));
	}
	
	public abstract String getDisplay();
	public abstract String getValue();
	
}
