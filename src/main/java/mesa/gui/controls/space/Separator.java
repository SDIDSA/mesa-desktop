package mesa.gui.controls.space;

import javafx.geometry.Orientation;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import mesa.gui.factory.Backgrounds;

public class Separator extends Region {
	public Separator(Orientation or) {
		switch (or) {
		case HORIZONTAL:
			setMaxHeight(1);
			setMinHeight(1);
			break;
		case VERTICAL:
			setMinWidth(1);
			setMaxWidth(1);
			break;
		default:
			break;
		}
		
		setBackground(Backgrounds.make(Color.GRAY));
	}
}
