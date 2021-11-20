package mesa.app.component.input;

import javafx.scene.Node;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.TextInput;
import mesa.gui.window.Window;

public class TextInputField extends InputField {
	private TextInput input;

	public TextInputField(Window window, String key, double width, boolean hidden) {
		super(window, key, width);

		input = new TextInput(window, new Font(16), key, hidden);

		value.bind(input.valueProperty());

		addInput(input);

		applyStyle(window.getStyl());
	}

	public TextInputField(Window window, String key, boolean hidden) {
		this(window, key, 200, hidden);
	}

	public TextInputField(Window window, String key, double width) {
		this(window, key, width, false);
	}

	public TextInputField(Window window, String key) {
		this(window, key, 200, false);
	}
	
	public void addPostField(Node...nodes) {
		input.addPostField(nodes);
	}

	@Override
	public void setValue(String value) {
		input.setValue(value);
	}

	@Override
	public void requestFocus() {
		input.requestFocus();
	}
	
}
