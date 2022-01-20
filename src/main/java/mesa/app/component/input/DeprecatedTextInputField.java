package mesa.app.component.input;

import mesa.gui.controls.input.styles.DeprecatedInputStyle;
import mesa.gui.window.Window;

public class DeprecatedTextInputField extends TextInputField {

	public DeprecatedTextInputField(Window window, String key, double width, boolean hidden) {
		super(window, key, width);
		
		input.setInputStyle(new DeprecatedInputStyle(input));

		applyStyle(window.getStyl());
	}

	public DeprecatedTextInputField(Window window, String key, boolean hidden) {
		this(window, key, 200, hidden);
	}

	public DeprecatedTextInputField(Window window, String key, double width) {
		this(window, key, width, false);
	}

	public DeprecatedTextInputField(Window window, String key) {
		this(window, key, 200, false);
	}
	
}
