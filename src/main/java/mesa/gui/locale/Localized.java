package mesa.gui.locale;

import javafx.beans.property.ObjectProperty;

/**
 * Represents a Localized Node. <br>
 * <br>
 * Classes implementing this interface have to define the
 * {@link Localized#applyLocale(Locale) applyLocale} method and will be affected
 * by {@link mesa.gui.window.Window#setLocale(Locale) Window.setLocale(Locale)}
 * calls.
 * 
 * @author Lukas Owen
 * 
 */
public interface Localized {

	/**
	 * Applies the passed {@link Locale} on this Node, The behavior of this method
	 * is defined by subclasses. <br>
	 * <br>
	 * <b>Note :</b> do not manually call this, use {@link mesa.gui.window.Window
	 * Window}<span style= "color:
	 * #0066cc;">.</span>{@link mesa.gui.window.Window#setLocale(Locale)
	 * setLocale(Locale)} in to apply a {@link Locale} on the whole scene graph.
	 *
	 * @param locale - the {@link Locale} to be applied on this Node
	 */
	public void applyLocale(Locale locale);
	public void applyLocale(ObjectProperty<Locale> locale);

	public static void bindLocale(Localized node, ObjectProperty<Locale> locale) {
		node.applyLocale(locale.get());
		locale.addListener((obs, ov, nv) -> {
			if(nv != ov) {
				node.applyLocale(nv);
			}
		});
	}
}
