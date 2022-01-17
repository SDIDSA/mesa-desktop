package mesa.gui.controls.input;

import mesa.gui.controls.Font;
import mesa.gui.controls.input.styles.DeprecatedInputStyle;
import mesa.gui.window.Window;

public class DeprecatedTextInput extends TextInput {

	public DeprecatedTextInput(Window window, Font font, String key, boolean hidden) {
		super(font, key, hidden);

		inputStyle = new DeprecatedInputStyle(this);

		applyStyle(window.getStyl());
	}

	public DeprecatedTextInput(Window window, Font f, String key) {
		this(window, f, key, false);
	}
}
