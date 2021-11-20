package mesa.gui.controls.input.combo;

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.Input;
import mesa.gui.controls.shape.Triangle;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class ComboInput extends Input implements Localized {
	private String key;

	private Text prompt;
	private Text base;

	private Triangle arrow;

	private ArrayList<ComboMenuItem> items;
	private ComboMenu popup;

	private ComboMenuItem selected;

	private long lastKey = 0;
	private String acc = "";

	public ComboInput(Window window, Font font, String key) {
		super(key);

		this.key = key;

		setFocusTraversable(true);
		setAlignment(Pos.CENTER_LEFT);

		base = new Text();

		prompt = new Text();

		prompt.opacityProperty().bind(Bindings.when(base.textProperty().isEmpty()).then(.5).otherwise(0));

		setPadding(new Insets(10));

		StackPane over = new StackPane();
		over.setAlignment(Pos.CENTER_RIGHT);

		arrow = new Triangle(12);
		arrow.setOpacity(.7);

		over.getChildren().add(arrow);

		getChildren().addAll(base, prompt, over);

		setFont(font);

		items = new ArrayList<>();
		popup = new ComboMenu(window, this);
		setOnMouseClicked(e -> {
			requestFocus();
			popup.showPop();
		});

		focusedProperty().addListener((obs, ov, nv) -> {
			focus(nv);
			if (!nv.booleanValue()) {
				popup.hide();
			}
		});

		setOnKeyPressed(e -> {
			int i = items.indexOf(selected);
			switch (e.getCode()) {
			case UP:
				setValue(items.get((i + 1) % items.size()));
				e.consume();
				break;
			case DOWN:
				setValue(items.get(i > 0 ? (i - 1) : items.size() - 1));
				e.consume();
				break;
			default:
				break;
			}
		});

		setOnKeyTyped(e -> {
			char c = e.getCharacter().charAt(0);
			if (Character.isAlphabetic(c) || Character.isDigit(c)) {
				long now = System.currentTimeMillis();

				if (now - lastKey > 500) {
					acc = "";
				}

				acc += c;
				lastKey = now;

				search(acc);
			}
		});

		setCursor(Cursor.HAND);

		applyStyle(window.getStyl());
		applyLocale(window.getLocale());
	}

	public void addItem(ComboMenuItem item) {
		if (items.contains(item)) {
			return;
		}
		items.add(item);
		popup.addItem(item);
	}

	public void addItems(ComboMenuItem... items) {
		for (ComboMenuItem item : items) {
			addItem(item);
		}
	}

	@Override
	public void setFont(Font font) {
		this.font = font;
		base.setFont(font.getFont());
		prompt.setFont(font.getFont());
	}

	@Override
	protected boolean isFocus() {
		return isFocused();
	}

	@Override
	public String getValue() {
		return base.getText();
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		popup.applyStyle(style);

		base.setFill(style.getText1());
		prompt.setFill(style.getText1());
		arrow.setFill(style.getText1());
	}

	@Override
	public void applyLocale(Locale locale) {
		popup.applyLocale(locale);

		prompt.setText(locale.get(key));
	}

	public void setValue(ComboMenuItem value) {
		selected = value;

		base.setText(value.getDisplay());
		this.value.set(value.getValue());
	}

	@Override
	public void setValue(String value) {
		if (value.isEmpty()) {
			selected = null;
			base.setText(value);
			this.value.set(value);
		} else
			for (ComboMenuItem item : items) {
				if (item.getValue().equals(value)) {
					setValue(item);
					return;
				}
			}
	}

	@Override
	public void clear() {
		setValue("");
	}

	private void search(String value) {
		for (ComboMenuItem item : items) {
			if (item.getValue().toLowerCase().contains(value.toLowerCase())
					|| item.getDisplay().toLowerCase().contains(value.toLowerCase())) {
				setValue(item);
				return;
			}
		}
	}

}
