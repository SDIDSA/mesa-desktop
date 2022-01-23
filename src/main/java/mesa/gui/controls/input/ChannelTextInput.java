package mesa.gui.controls.input;

import mesa.gui.controls.Font;
import mesa.gui.controls.input.styles.ChannelInputStyle;
import mesa.gui.window.Window;

public class ChannelTextInput extends TextInput {

	public ChannelTextInput(Window window, Font font, String key) {
		super(window, font, key, false);

		inputStyle = new ChannelInputStyle(this);

		applyStyle(window.getStyl());
	}
}
