package mesa.gui.controls;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class Animator {
	private Animator() {

	}

	public static void show(Region r, double to) {
		r.setMouseTransparent(false);
		double from = r.getHeight();

		DoubleProperty v = new SimpleDoubleProperty(from);
		r.minHeightProperty().bind(v);
		r.maxHeightProperty().bind(v);

		Timeline an = new Timeline(60.0, new KeyFrame(Duration.seconds(.2), new KeyValue(v, to, Interpolator.EASE_BOTH),
				new KeyValue(r.opacityProperty(), 1, Interpolator.EASE_BOTH)));
		an.setOnFinished(e -> {
			r.setDisable(false);
			r.maxHeightProperty().unbind();
			r.minHeightProperty().unbind();
		});

		an.playFromStart();
	}

	public static void hide(Region r, double to) {
		double from = r.getHeight();

		DoubleProperty v = new SimpleDoubleProperty(from);
		r.minHeightProperty().bind(v);
		r.maxHeightProperty().bind(v);

		Timeline an = new Timeline(60.0, new KeyFrame(Duration.seconds(.2), new KeyValue(v, to, Interpolator.EASE_BOTH),
				new KeyValue(r.opacityProperty(), to == 0 ? 0 : 1, Interpolator.EASE_BOTH)));
		an.setOnFinished(e -> {
			r.setDisable(to == 0);
			r.maxHeightProperty().unbind();
			r.minHeightProperty().unbind();
		});

		an.playFromStart();
	}

	public static void hide(Region r) {
		hide(r, 0);
	}
}
