package mesa.gui.controls.space;

import javafx.geometry.Orientation;
import javafx.scene.layout.Priority;

public class ExpandingHSpace extends ExpandingSpace {
	public ExpandingHSpace(Priority priority) {
		super(Orientation.HORIZONTAL);
	}
	

	public ExpandingHSpace() {
		this(Priority.ALWAYS);
	}
}
