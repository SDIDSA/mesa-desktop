package mesa.gui.style;

/**
 * Represents a Styleable Node. <br>
 * <br>
 * Classes implementing this interface have to define the
 * {@link Styleable#applyStyle(Style) applyStyle} method and will be affected by
 * {@link mesa.gui.window.Window#setStyle(Style) Window.setStyle(Style)} calls.
 * 
 * @author Lukas Owen
 * 
 */
public interface Styleable {

	/**
	 * Applies the passed {@link Style} on this Node, The behavior of this method is
	 * defined by subclasses. <br>
	 * <br>
	 * <b>Note :</b> do not manually call this, use {@link mesa.gui.window.Window
	 * Window}<span style= "color:
	 * #0066cc;">.</span>{@link mesa.gui.window.Window#setStyle(Style)
	 * setStyle(Style)} in to apply a {@link Style} on the whole scene graph.
	 * 
	 * @param style - the {@link Style} to be applied on this Node
	 */

	void applyStyle(Style style);
}
