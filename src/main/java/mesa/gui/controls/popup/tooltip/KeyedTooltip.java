package mesa.gui.controls.popup.tooltip;

import javafx.beans.property.ObjectProperty;
import mesa.gui.controls.popup.Direction;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.window.Window;

public class KeyedTooltip extends Tooltip implements Localized {

	private String key;

	public KeyedTooltip(Window window, String key, Direction direction, double offset) {
		super(window, "", direction, offset);

		this.key = key;
		applyLocale(window.getLocale());
	}

	public KeyedTooltip(Window window, String key, Direction direction) {
		this(window, key, direction, 0);
	}

	@Override
	public void applyLocale(Locale locale) {
		setText(locale.get(key));
	}
	
	@Override
	public void applyLocale(ObjectProperty<Locale> locale) {
		Localized.bindLocale(this, locale);	
	}

}
