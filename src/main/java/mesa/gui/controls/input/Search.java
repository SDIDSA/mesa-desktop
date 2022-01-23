package mesa.gui.controls.input;

import java.util.function.Consumer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.layer_icon.SearchClearIcon;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class Search extends ModernTextInput implements Localized {
	private SearchClearIcon icon;

	private Consumer<String> onSearch;
	private Runnable reset;

	private String prompt;
	
	private BooleanProperty focused;

	private DoubleProperty width;

	public Search(Window window, String prompt) {
		super(window, new Font(Font.DEFAULT_FAMILY_MEDIUM, 13.5), "search_query", false);
		this.prompt = prompt;

		focused = new SimpleBooleanProperty(false);
		
		setFieldPadding(new Insets(3, 4, 3, 4));
		setAlignment(Pos.CENTER_RIGHT);
		valueProperty().addListener((obs, ov, nv) -> {
			if (nv.isEmpty()) {
				icon.search();

				if (reset != null) {
					reset.run();
				}
			} else {
				icon.clear();

				if (onSearch != null) {
					onSearch.accept(nv);
				}
			}
		});

		icon = new SearchClearIcon(12);
		icon.setTranslateX(-3);
		icon.setOnClear(()-> {
			clear();
			requestFocus();
		});

		setMinHeight(22);

		width = new SimpleDoubleProperty(-1);

		minWidthProperty().bind(width);
		maxWidthProperty().bind(width);

		addPostField(icon);
		
		focused.bind(super.focusProperty().or(icon.focusProperty()));

		applyStyle(window.getStyl().get());
	}

	public void fixWidth(double value) {
		width.set(value);
	}
	
	public ReadOnlyBooleanProperty focusProperty() {
		return focused;
	}

	public DoubleProperty fixedWidthProperty() {
		return width;
	}

	public void setReset(Runnable reset) {
		this.reset = reset;
	}

	public void setSearch(Consumer<String> search) {
		this.onSearch = search;
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		if (icon == null) {
			return;
		}

		icon.setFill(style.getTextMuted());
		icon.applyStyle(style);
	}

	@Override
	public void applyLocale(Locale locale) {
		setPrompt(locale.get(prompt));
	}

	@Override
	public void applyLocale(ObjectProperty<Locale> locale) {
		Localized.bindLocale(this, locale);
	}

}
