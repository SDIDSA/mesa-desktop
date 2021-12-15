package mesa.gui.controls.label;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.keyed.KeyedLink;
import mesa.gui.controls.label.keyed.KeyedTextNode;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.label.unkeyed.Link;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.label.unkeyed.TextNode;
import mesa.gui.window.Window;

public class MultiText extends TextFlow {
	private Window window;

	private ArrayList<TextNode> nodes;

	private Color fill;
	
	public MultiText(Window window) {
		this.window = window;
		nodes = new ArrayList<>();
	}

	public MultiText(Window window, String key, Font font) {
		this(window);
		
		addLabel(key, font);
	}
	
	public void setFill(Color fill) {
		this.fill = fill;
		nodes.forEach(node -> {
			if(node instanceof Text label) {
				label.setFill(fill);
			}
		});
	}

	public void setKey(int index, String key) {
		if(nodes.get(index) instanceof KeyedTextNode node) {
			node.setKey(key);
		}else {
			throw new IllegalArgumentException("the TextNode at " + index + " is not a KeyedTextNode");
		}
	}

	public void setAction(int index, Runnable action) {
		if(nodes.get(index) instanceof Link link) {
			link.setAction(action);
		}else {
			throw new IllegalArgumentException("the TextNode at " + index + " is not a Link");
		}
	}

	public void setKey(String key) {
		setKey(0, key);
	}

	public void addLabel(String key, Font font) {
		Label lab = new Label(window, key, font);
		if(fill != null) {
			lab.setFill(fill);
		}
		addNode(lab);
	}

	public void addLabel(String key) {
		addLabel(key, Font.DEFAULT);
	}

	public void addLink(String key, Font font) {
		addNode(new Link(window, key, font));
	}

	public void addKeyedLink(String key, Font font) {
		addNode(new KeyedLink(window, key, font));
	}

	public void addLink(String key) {
		addLabel(key, Font.DEFAULT);
	}

	private void addNode(TextNode node) {
		nodes.add(node);
		getChildren().add(node.getNode());
	}
	
	public void center() {
		setTextAlignment(TextAlignment.CENTER);
	}
}
