package mesa.gui.controls.input.combo.item;

import javafx.beans.property.ObjectProperty;
import javafx.scene.text.Text;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.combo.ComboMenuItem;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public abstract class KeyedTextItem extends ComboMenuItem implements Styleable, Localized {
	protected String key;
	private Text lab;
	
	protected KeyedTextItem(Window window, String key) {
		this.key = key;
		
		lab = new Text();
		lab.setFont(new Font(16).getFont());
		
		getChildren().add(lab);
		
		applyStyle(window.getStyl());
		applyLocale(window.getLocale());
	}

	@Override
	public String getDisplay() {
		return lab.getText();
	}
	
	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		lab.setFill(style.getHeaderPrimary());
	}
	
	@Override
	public void applyLocale(Locale locale) {
		lab.setText(locale.get(key));
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
