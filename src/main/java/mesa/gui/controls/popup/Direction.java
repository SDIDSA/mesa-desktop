package mesa.gui.controls.popup;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public enum Direction {
	UP(), RIGHT(), DOWN(), LEFT();

	public boolean isHorizontal() {
		return this == RIGHT || this == LEFT;
	}

	public boolean isVertical() {
		return !isHorizontal();
	}

	public boolean isArrowFirst() {
		return this == DOWN || this == RIGHT;
	}

	public Pane toPane() {
		if (isHorizontal()) {
			HBox res = new HBox();
			res.setAlignment(Pos.CENTER);
			return res;
		} else {
			VBox res = new VBox();
			res.setAlignment(Pos.CENTER);
			return res;
		}
	}
	
	public double[] calcPos(PopupControl popup, Node node, double offset) {
		Bounds bounds = node.getBoundsInLocal();
		Bounds screenBounds = node.localToScreen(bounds);
		double[] res = new double[2];
		
		double xHor = (isArrowFirst() ? (screenBounds.getMaxX() + offset) : (screenBounds.getMinX() - offset));
		double x = isHorizontal()
				? xHor
				: (screenBounds.getMinX() + screenBounds.getMaxX()) / 2;

		double yVer = (isArrowFirst() ? (screenBounds.getMaxY() + offset) : (screenBounds.getMinY() - offset));
		double y = isVertical()
				? yVer
				:  (screenBounds.getMinY() + screenBounds.getMaxY()) / 2;

		double pxHor = (isArrowFirst() ? 0 : popup.getWidth());
		double pyVer = (isArrowFirst() ? 0 : popup.getHeight());
		
		res[0] = x - (isHorizontal() ? pxHor : popup.getWidth() / 2);
		res[1] = y - (isVertical() ? pyVer : popup.getHeight() / 2);

		return res;
	}
}