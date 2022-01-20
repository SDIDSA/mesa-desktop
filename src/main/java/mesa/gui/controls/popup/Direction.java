package mesa.gui.controls.popup;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.PopupWindow;
import mesa.gui.controls.popup.tooltip.Tooltip;

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
	
	public double[] calcPos(Tooltip tooltip, Node node, double offset) {
		Bounds bounds = node.getBoundsInLocal();
		Bounds screenBounds = node.localToScene(bounds);
		double[]res = new double[2];
		
		switch(this) {
		case DOWN:
			res[0] = screenBounds.getCenterX() - (tooltip.width() / 2);
			res[1] = screenBounds.getMaxY() + offset;
			break;
		case LEFT:
			res[0] = screenBounds.getMinX() - tooltip.width() - offset;
			res[1] = screenBounds.getCenterY() - (tooltip.getHeight() / 2);
			break;
		case RIGHT:
			res[0] = screenBounds.getMaxX() + offset;
			res[1] = screenBounds.getCenterY() - (tooltip.getHeight() / 2);
			break;
		case UP:
			res[0] = screenBounds.getCenterX() - (tooltip.width() / 2);
			res[1] = screenBounds.getMinY() - tooltip.getHeight() - offset;
			break;		
		}
		
		res[0] -= tooltip.getOwner().getRoot().getPadding().getLeft();
		res[1] -= tooltip.getOwner().getRoot().getPadding().getTop();
		
		return res;
	}
	
	public double[] calcPos(PopupWindow popup, Node node, double offset) {
		Bounds bounds = node.getBoundsInLocal();
		Bounds screenBounds = node.localToScreen(bounds);
		double[]res = new double[2];
		
		switch(this) {
		case DOWN:
			res[0] = screenBounds.getCenterX() - (popup.getWidth() / 2);
			res[1] = screenBounds.getMaxY() + offset;
			break;
		case LEFT:
			res[0] = screenBounds.getMinX() - popup.getWidth() - offset;
			res[1] = screenBounds.getCenterY() - (popup.getHeight() / 2);
			break;
		case RIGHT:
			res[0] = screenBounds.getMaxX() + offset;
			res[1] = screenBounds.getCenterY() - (popup.getHeight() / 2);
			break;
		case UP:
			res[0] = screenBounds.getCenterX() - (popup.getWidth() / 2);
			res[1] = screenBounds.getMinY() - popup.getHeight() - offset;
			break;		
		}
		
		return res;
	}
	
	public double pivotX() {
		return (this == UP || this == DOWN) ? .5 : this == RIGHT ? 0 : 1;
	}
	
	public double pivotY() {
		return (this == RIGHT || this == LEFT) ? .5 : this == DOWN ? 0 : 1;
	}
}