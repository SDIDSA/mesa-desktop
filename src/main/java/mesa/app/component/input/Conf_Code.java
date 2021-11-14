package mesa.app.component.input;

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.Clipboard;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.TextInput;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.window.Window;

public class Conf_Code extends InputField {

	private ArrayList<TextInput> inputs;

	public Conf_Code(Window window, String key, double width) {
		super(window, key, width);
		inputs = new ArrayList<TextInput>();

		StringProperty[] values = new StringProperty[8];
		Font f = new Font(20);
		for (int i = 0; i < 8; i++) {
			if (i == 4) {
				addNode(new ExpandingHSpace());
			}
			TextInput inp = new TextInput(window, f, key);
			inp.setMinSize(45, 45);
			inp.setMaxSize(45, 45);
			inp.align(Pos.CENTER);
			inp.setFocusable(false);
			values[i] = inp.valueProperty();
			addInput(inp);
			inputs.add(inp);
		}
		
		BooleanExpression empty = values[0].isEmpty();
		StringExpression val = values[0];
		
		for(int i = 1; i < values.length; i++) {
			empty = empty.or(values[i].isEmpty());
			val = val.concat(values[i]);
		}
		
		value.bind(Bindings.when(empty).then("").otherwise(val));

		setOnMouseEntered(e -> {
			if (!isFocused())
				for (TextInput inp : inputs) {
					inp.hover();
				}
		});

		setOnMouseExited(e -> {
			if (!isFocused())
				for (TextInput inp : inputs) {
					inp.unhover();
				}
		});

		setFocusTraversable(true);

		focusedProperty().addListener((obs, ov, nv) -> {
			if (nv) {
				for (TextInput inp : inputs) {
					inp.focus();
				}
			} else {
				for (TextInput inp : inputs) {
					inp.unfocus();
				}
			}
		});

		setOnMousePressed(e -> {
			requestFocus();
		});

		setOnKeyTyped(e -> {
			char c = e.getCharacter().charAt(0);

			if (Character.isDigit(c)) {
				append(c);
			} else if (c == 8) {
				delete();
			} else if (c == 22) {
				paste();
			}
		});

		setCursor(Cursor.TEXT);

		align(Pos.CENTER);
		
		applyStyle(window.getStyl());
	}
	
	private void append(char c) {
		for (int i = 0; i < inputs.size(); i++) {
			TextInput inp = inputs.get(i);

			if (inp.getValue().isEmpty()) {
				inp.setValue(Character.toString(c));
				return;
			}
		}
	}

	public void delete() {
		for (int i = inputs.size() - 1; i >= 0; i--) {
			TextInput inp = inputs.get(i);

			if (!inp.getValue().isEmpty()) {
				inp.clear();
				return;
			}
		}
	}

	public void paste() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		if (clipboard.hasString()) {
			clear();
			for (char c : clipboard.getString().toCharArray()) {
				if (Character.isDigit(c)) {
					append(c);
				}
			}
		}
		getParent().requestFocus();
		requestFocus();
	}

	public Conf_Code(Window window, String key) {
		this(window, key, 200);
	}

	@Override
	public void setValue(String string) {

	}

}
