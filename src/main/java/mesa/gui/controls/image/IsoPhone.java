package mesa.gui.controls.image;

import mesa.gui.style.Style;

public class IsoPhone extends LayerIcon {

	public IsoPhone(double size) {
		super(size, "ips", "ipf", "ipe", "ipc");
	}

	public void applyStyle(Style style) {
		setFill(0, style.getBack3());
		setFill(1, style.getInteractiveNormal());
		setFill(2, style.getBack5());
		setFill(3, style.getAccent());
	}
}
