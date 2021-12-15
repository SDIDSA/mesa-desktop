package mesa.gui.controls.label;

import javafx.scene.Node;
import mesa.gui.controls.Font;

public interface TextNode {
	void setFont(Font font);
	void setKey(String key);
	Node getNode();
}
