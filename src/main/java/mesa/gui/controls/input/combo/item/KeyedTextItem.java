package mesa.gui.controls.input.combo.item;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public abstract class KeyedTextItem extends TextItem implements Localized {
	protected String key;
	
	protected KeyedTextItem(Window window, String key) {
		super(window, key);
		this.key = key;
		
		applyLocale(window.getLocale());
	}

	@Override
	public StringProperty getDisplay() {
		return lab.textProperty();
	}
	
	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		lab.setFill(style.getHeaderPrimary());
	}
	
	@Override
	public void applyLocale(Locale locale) {
		lab.set(locale.get(key));
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
