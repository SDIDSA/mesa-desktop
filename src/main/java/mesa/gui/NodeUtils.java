package mesa.gui;

import javafx.scene.Parent;
import mesa.app.component.Form;
import mesa.app.component.input.InputField;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

public class NodeUtils {
	private NodeUtils() {

	}

	public static void applyStyle(Node node, Style style) {
		if (node instanceof Parent) {
			for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
				applyStyle(child, style);
			}
		}

		if (node instanceof Styleable) {
			((Styleable) node).applyStyle(style);
		}
	}

	public static void applyLocale(Node node, Locale locale) {
		if (node instanceof Parent) {
			for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
				applyLocale(child, locale);
			}
		}

		if (node instanceof Localized) {
			((Localized) node).applyLocale(locale);
		}
	}

	public static <T> List<T> getNodesOfType(Node node, Class<T> type) {
		ArrayList<T> res = new ArrayList<>();

		if (node instanceof Parent) {
			for (Node n : ((Parent) node).getChildrenUnmodifiable()) {
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
