package mesa.gui.controls.popup.context.items;

import javafx.scene.paint.Color;
import mesa.gui.controls.popup.context.ContextMenu;

public class KeyedMenuItem extends MenuItem {

	public KeyedMenuItem(ContextMenu menu, String key, Color fill) {
		super(menu, key, fill, true);
	}

}
