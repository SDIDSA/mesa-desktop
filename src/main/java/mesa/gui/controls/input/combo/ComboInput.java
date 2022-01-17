package mesa.gui.controls.input.combo;

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.DeprecatedInput;
import mesa.gui.controls.shape.Triangle;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class ComboInput extends DeprecatedInput implements Localized {
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

	public void setValue(ComboMenuItem value) {
		selected = value;

		base.textProperty().bind(value.getDisplay());
		this.value.bind(value.getValue());
	}

	private void search(String value) {
		for (ComboMenuItem item : items) {
			if (item.match(value)) {
				setValue(item);
				return;
			}
		}
	}

	@Override
	public void setFont(Font font) {
		base.setFont(font.getFont());
		prompt.setFont(font.getFont());
	}

	@Override
	protected boolean isFocus() {
		return isFocused();
	}

	@Override
	public String getValue() {
		return selected != null ? selected.getValue().get() : "";
	}

	@Override
	public void setValue(String value) {
		if (value.isEmpty()) {
			selected = null;
			base.textProperty().unbind();
			base.setText(value);
			this.value.set(value);
		} else
			for (ComboMenuItem item : items) {
				if (item.getValue().get().equals(value)) {
					setValue(item);
					return;
				}
			}
	}

	@Override
	public void clear() {
		setValue("");
	}

	@Override
	public boolean supportsContextMenu() {
		return false;
	}

	@Override
	public Node contextMenuNode() {
		return null;
	}
	
	@Override
	public void copy() {
		//DO NOTHING
	}
	
	@Override
	public void cut() {
		//DO NOTHING
	}
	
	@Override
	public void paste() {
		//DO NOTHING
	}
	
	@Override
	public BooleanProperty notSelected() {
		return null;
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		popup.applyStyle(style);

		base.setFill(style.getTextNormal());
		prompt.setFill(style.getHeaderSecondary());
		arrow.setFill(style.getTextNormal());
	}

	@Override
	public void applyLocale(Locale locale) {
		prompt.setText(locale.get(key));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
	
	@Override
	public void applyLocale(ObjectProperty<Locale> locale) {
		Localized.bindLocale(this, locale);
	}

}
