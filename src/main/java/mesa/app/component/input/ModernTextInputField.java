package mesa.app.component.input;

import javafx.scene.Node;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.ModernTextInput;
import mesa.gui.window.Window;

public class ModernTextInputField extends InputField {
	private ModernTextInput input;

	public ModernTextInputField(Window window, String key, double width, boolean hidden) {
		super(window, key, width);

		input = new ModernTextInput(window, new Font(16), key, hidden);

		value.bind(input.valueProperty());

		addInput(input);

		applyStyle(window.getStyl());
	}

	public ModernTextInputField(Window window, String key, boolean hidden) {
		this(window, key, 200, hidden);
	}

	public ModernTextInputField(Window window, String key, double width) {
		this(window, key, width, false);
	}

	public ModernTextInputField(Window window, String key) {
		this(window, key, 200, false);
	}
	
	public void setPrompt(String prompt) {
		input.setPrompt(prompt);
	}
	
	public void addPostField(Node...nodes) {
		input.addPostField(nodes);
	}
	
	public void positionCaret(int pos) {
		input.positionCaret(pos);
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
