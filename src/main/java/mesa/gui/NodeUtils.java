package mesa.gui;

import javafx.scene.Parent;
import mesa.app.component.Form;
import mesa.app.component.input.InputField;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

public class NodeUtils {
	private NodeUtils() {

	}

	public static <T> List<T> getNodesOfType(Node node, Class<T> type) {
		ArrayList<T> res = new ArrayList<>();

		if (node instanceof Parent parent) {
			for (Node n : parent.getChildrenUnmodifiable()) {
				res.addAll(getNodesOfType(n, type));
			}
		}

		if (type.isInstance(node)) {
			res.add(type.cast(node));
		}

		return res;
	}

	public static Form getForm(Node node) {
		return new Form(getNodesOfType(node, InputField.class));
	}
}
