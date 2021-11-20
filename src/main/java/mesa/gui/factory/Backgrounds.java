package mesa.gui.factory;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

public class Backgrounds {
	private Backgrounds() {
		
	}
	
	public static Background make(Paint fill, CornerRadii radius, Insets insets) {
		return new Background(new BackgroundFill(fill, radius, insets));
	}

	public static Background make(Paint fill, CornerRadii radius) {
		return make(fill, radius, null);
	}

	public static Background make(Paint fill, Insets insets) {
		return make(fill, null, insets);
	}

	public static Background make(Paint fill, double radius) {
		return make(fill, new CornerRadii(radius), null);
	}

	public static Background make(Paint fill, double radius, double insets) {
		return make(fill, new CornerRadii(radius), new Insets(insets));
	}

	public static Background make(Paint fill) {
		return make(fill, null, null);
	}
}
