package mesa.gui.controls.input.styles;

import javafx.scene.paint.Color;
import mesa.gui.controls.input.Input;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;

public abstract class InputStyle {
	protected Input input;

	protected boolean ignoreHover = false;
	protected boolean ignoreFocus = false;
	
	protected InputStyle(Input input) {
		this.input = input;
	}
	
	public void setIgnoreFocus(boolean ignoreFocus) {
		this.ignoreFocus = ignoreFocus;
	}
	
	public void setIgnoreHover(boolean ignoreHover) {
		this.ignoreHover = ignoreHover;
	}

	protected void applyBack(Color fill) {
		input.setBackground(Backgrounds.make(fill, 3.0));
	}

	protected void applyBorder(Color border) {
		input.setBorder(Borders.make(border, 3.0));
	}
	
	public abstract void focus(boolean focus);

	public abstract void hover();

	public abstract void unhover();

	public abstract void focus();

	public abstract void unfocus();

	public abstract void setBorder(Color border, Color hover, Color foc);
	
	public abstract void applyStyle(Style style);
}
