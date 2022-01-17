package mesa.gui.controls.input;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import mesa.gui.controls.Font;
import mesa.gui.style.Style;

public abstract class Input extends StackPane implements Styleable {
	protected StringProperty value;
	
	protected Input(String key) {
		getStyleClass().addAll("input", key);
		
		value = new SimpleStringProperty("");
	}
	
	public String getValue() {
		return value.get();
	}

	public StringProperty valueProperty() {
		return value;
	}

	public abstract void setFont(Font font);


	public abstract void setValue(String value);

	/**
	 * Clear the value of this input, note that this method is abstract and the
	 * implementation of this method depends on the input type
	 */
	public abstract void clear();

	public abstract void setBorder(Color border, Color hover, Color foc);
	
	public abstract void applyStyle(Style style);
	
	public abstract boolean supportsContextMenu();
	
	public abstract Node contextMenuNode();
	
	public abstract void copy();
	
	public abstract void cut();
	
	public abstract void paste();
	
	public abstract BooleanProperty notSelected();
}
