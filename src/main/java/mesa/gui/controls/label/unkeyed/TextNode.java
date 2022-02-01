package mesa.gui.controls.label.unkeyed;

import javafx.scene.Node;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.style.ColorItem;

public interface TextNode extends ColorItem {
	void setFont(Font font);
	Node getNode();
	void setTransform(TextTransform transform);
}
