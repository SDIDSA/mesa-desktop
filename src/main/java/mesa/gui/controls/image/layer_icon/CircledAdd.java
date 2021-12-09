package mesa.gui.controls.image.layer_icon;

import javafx.scene.paint.Color;

public class CircledAdd extends LayerIcon {

	public CircledAdd(double size) {
		super(size, "addc", "addp");
	}
	
	public void setCircleFill(Color fill) {
		setFill(0, fill);
	}
	
	public void setSignFill(Color fill) {
		setFill(1, fill);
	}

}
