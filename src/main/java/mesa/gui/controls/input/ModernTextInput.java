package mesa.gui.controls.input;

import mesa.gui.controls.Font;
import mesa.gui.controls.input.styles.ModernInputStyle;
import mesa.gui.window.Window;

public class ModernTextInput extends TextInput {

	public ModernTextInput(Window window, Font font, String key, boolean hidden) {
		super(window, font, key, hidden);

		inputStyle = new ModernInputStyle(this);

		applyStyle(window.getStyl());
	}
}
