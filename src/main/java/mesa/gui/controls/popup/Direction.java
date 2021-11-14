package mesa.gui.controls.popup;

import javafx.geometry.Pos;
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
}