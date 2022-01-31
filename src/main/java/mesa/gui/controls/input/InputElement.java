package mesa.gui.controls.input;

public interface InputElement {
	String getValue();
	void select(RichInput input);
	void deselect(RichInput input);
	double width();
}
