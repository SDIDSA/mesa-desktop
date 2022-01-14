package mesa.app.pages.session.types.server.left;

import javafx.scene.Cursor;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.popup.tooltip.KeyedTooltip;
import mesa.gui.controls.popup.tooltip.Tooltip;
import mesa.gui.window.Window;

public class ActionIcon extends ColorIcon {
	
	private KeyedTooltip ktp;
	
	public ActionIcon(Window window, String name, double size, int postSize, String hint, Direction direction) {
		super(name, size, true);
		setCursor(Cursor.HAND);
		
		setPadding((postSize - size) / 2);
		
		ktp = new KeyedTooltip(window, hint, direction);
		ktp.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		Tooltip.install(this, ktp);
	}
	
	public void setTipOffset(double offset) {
		ktp.setOffset(offset);
	}

	public ActionIcon(Window window, String name, double size, int postSize, String hint) {
		this(window, name, size, postSize, hint, Direction.UP);
	}
}
