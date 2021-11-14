package mesa.gui.controls.shape;

import javafx.scene.shape.Polygon;

public class Triangle extends Polygon {
	public Triangle(double size) {
		getPoints().addAll(new Double[]{
					size / 2, size / 2,
		            size, 0.0,
		            size, size });
	}
}
