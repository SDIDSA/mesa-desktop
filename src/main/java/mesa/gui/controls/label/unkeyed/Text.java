package mesa.gui.controls.label.unkeyed;

import javafx.scene.Node;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.TextTransform;

public class Text extends javafx.scene.text.Text implements TextNode{

	private TextTransform transform = TextTransform.NONE;
	
	public Text(String val, Font font) {
		if(val != null) {
			setText(val);
		}
		setFont(font);
	}

	public Text(String val) {
		this(val, Font.DEFAULT);
	}
	
	public void set(String text) {
		setText(transform.apply(text));
	}

	public void setTransform(TextTransform transform) {
		this.transform = transform;
		setText(transform.apply(getText()));
	}
	
	@Override
	public void setFont(Font font) {
		setFont(font.getFont());
	}

	@Override
	public Node getNode() {
		return this;
	}

}
