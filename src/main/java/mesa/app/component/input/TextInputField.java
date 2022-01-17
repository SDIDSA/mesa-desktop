package mesa.app.component.input;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.DeprecatedTextInput;
import mesa.gui.window.Window;

public class TextInputField extends InputField {
	private DeprecatedTextInput input;

	public TextInputField(Window window, String key, double width, boolean hidden) {
		super(window, key, width);

		input = new DeprecatedTextInput(window, new Font(16), key, hidden);

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
	
	@Override
	public boolean supportsContextMenu() {
		return true;
	}

	@Override
	public void copy() {
		input.copy();
	}

	@Override
	public void cut() {
		input.cut();
	}

	@Override
	public void paste() {
		input.paste();
	}
	
	@Override
	public BooleanProperty notSelected() {
		return input.notSelected();
	}
	
}
