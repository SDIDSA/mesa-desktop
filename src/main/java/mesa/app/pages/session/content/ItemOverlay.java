package mesa.app.pages.session.content;

import mesa.app.pages.session.items.BarItem;
import mesa.app.pages.session.items.ColorBarItem;
import mesa.gui.controls.alert.Overlay;

public class ItemOverlay {
	private Overlay overlay;
	private ColorBarItem item;

	public void setItem(ColorBarItem item) {
		this.item = item;
		item.setSelectable(false);
		if (overlay != null)
			setup();
	}

	public void setOverlay(Overlay overlay) {
		this.overlay = overlay;

		if (item != null)
			setup();
	}

	private void setup() {
		item.setAction(overlay::show);

		overlay.addOnShowing(() -> item.getIcon().setCanUnhover(false));

		overlay.addOnHidden(() -> {
			item.getIcon().setCanUnhover(true);
			item.getIcon().unhover();
		});
	}

	public BarItem getItem() {
		return item;
	}
}
