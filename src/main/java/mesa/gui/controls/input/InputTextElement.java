package mesa.gui.controls.input;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class InputTextElement extends Text implements InputElement {

	public InputTextElement(String t) {
		super(t);
	}

	@Override
	public String getValue() {
		return getText();
	}

	@Override
	public void select(RichInput input) {
		setFill(Color.WHITE);
	}

	@Override
	public void deselect(RichInput input) {
		setFill(input.getTextFill());
	}

	@Override
	public double width() {
		return (int) (getLayoutBounds().getWidth() + 1);
	}

}
