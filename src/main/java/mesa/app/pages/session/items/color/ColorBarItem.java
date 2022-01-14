package mesa.app.pages.session.items.color;

import javafx.scene.paint.Color;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.items.BarItem;
import mesa.gui.controls.popup.tooltip.KeyedTooltip;
import mesa.gui.controls.popup.tooltip.Tooltip;

public class ColorBarItem extends BarItem {

	private ColorItemIcon ic;
	
	public ColorBarItem(SessionPage session, Color to, String key, String icon, int size) {
		super(session);

		ic = new ColorItemIcon(session, to, icon, size);
		
		setIcon(ic);
		setTooltip(new KeyedTooltip(session.getWindow(), key, Tooltip.RIGHT));
	}

	public void setColor(Color c) {
		ic.setTo(c);
	}
	
	public ColorBarItem(SessionPage session, Color to, String key, String icon) {
		this(session, to, key, icon, 16);
	}
	
	public ColorItemIcon getIcon() {
		return ic;
	}

}
