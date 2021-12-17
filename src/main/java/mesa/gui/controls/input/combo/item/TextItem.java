package mesa.gui.controls.input.combo.item;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.combo.ComboMenuItem;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class TextItem extends ComboMenuItem implements Styleable {
	protected Text lab;
	
	public TextItem(Window window, String value) {
		lab = new Text(value);
		lab.setTransform(TextTransform.CAPITALIZE);
		lab.setFont(new Font(16).getFont());
		
		getChildren().add(lab);
		
		applyStyle(window.getStyl());
	}

	@Override
	public StringProperty getDisplay() {
		return lab.textProperty();
	}

	@Override
	public StringProperty getValue() {
		return lab.textProperty();
	}
	
	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		lab.setFill(style.getHeaderPrimary());
	}
	
	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
	
}
