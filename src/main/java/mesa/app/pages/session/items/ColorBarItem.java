package mesa.app.pages.session.items;

import javafx.scene.paint.Color;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.popup.tooltip.KeyedTooltip;
import mesa.gui.controls.popup.tooltip.Tooltip;

public class ColorBarItem extends BarItem {

	private ColorItemIcon ic;
	
	private KeyedTooltip tip;
	
	public ColorBarItem(SessionPage session, Color to, String key, String icon, int size) {
		super(session);

		ic = new ColorItemIcon(session, to, icon, size);
		tip = new KeyedTooltip(session.getWindow(), key, Tooltip.RIGHT);
		
		setIcon(ic);
		setTooltip(tip);
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
