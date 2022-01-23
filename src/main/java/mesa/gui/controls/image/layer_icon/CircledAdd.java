package mesa.gui.controls.image.layer_icon;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Paint;

public class CircledAdd extends LayerIcon {

	public CircledAdd(double size) {
		super(size, "addc", "addp");
	}
	
	public void setCircleFill(Paint fill) {
		setFill(0, fill);
	}
	
	public void setSignFill(Paint fill) {
		setFill(1, fill);
	}
	
	public ObjectProperty<Paint> circleFillProperty() {
		return getFillProperty(0);
	}

}
