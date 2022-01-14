package mesa.gui.controls.input;

import javafx.scene.paint.Color;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public abstract class ModernInput extends Input implements Styleable {

	protected ModernInput(String key) {
		super(key);

		setMinHeight(40);
	}

	public void setBorder(Color border, Color hover, Color foc) {
		//ignore
	}

	private void applyBack(Color fill) {
		setBackground(Backgrounds.make(fill, 3.0));
	}
	
	protected abstract boolean isFocus();

	@Override
	public void applyStyle(Style style) {
		applyBack(style.getBackgroundTertiary());
	}
}
