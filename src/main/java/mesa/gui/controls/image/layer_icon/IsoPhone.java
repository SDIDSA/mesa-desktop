package mesa.gui.controls.image.layer_icon;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mesa.app.utils.Colors;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.style.Style;

public class IsoPhone extends LayerIcon {
	private static double factor = 1.68;
	private static double duration = .2;

	private Timeline colorNormal;

	private Timeline shake;

	private ArrayList<Transition> transitions;

	public IsoPhone(double size) {
		super(size, "ips", "ipf", "ipc", "ipe", "ipr", "ipm", "ipt", "ipi");

		transitions = new ArrayList<>();

		transitions.add(new Transition(4, size, 1));
		transitions.add(new Transition(5, size, -1));
		transitions.add(new Transition(6, size, -1));
		transitions.add(new Transition(7, size, 1));

		shake = new Timeline(new KeyFrame(Duration.seconds(duration / 2),
				new KeyValue(translateXProperty(), 5, Interpolator.EASE_BOTH)));
		shake.setAutoReverse(true);
		shake.setCycleCount(4);
	}

	private void stopAll() {
		colorNormal.stop();
		transitions.forEach(Transition::stop);
	}

	private void showTransition(int transition) {
		if (transitions.get(transition).isShowing()) {
			return;
		}

		stopAll();
		hideOthers(transition);
		transitions.get(transition).playShow();
	}

	private void hideOthers(int transition) {
		for (int i = 0; i < transitions.size(); i++) {
			if (i != transition) {
				transitions.get(i).playHide();
			}
		}
	}

	public void showError() {
		showTransition(0);
		shake();
	}

	public void showSms() {
		showTransition(1);
	}

	public void showCorrect() {
		showTransition(2);
	}

	public void showIncorrect() {
		showTransition(3);
	}

	public void showNormal() {
		stopAll();

		hideOthers(-1);
		colorNormal.playFromStart();
	}

	private void shake() {
		shake.stop();
		setTranslateX(0);

		shake.playFromStart();
	}

	public void applyStyle(Style style) {
		setFill(0, style.getBackgroundTertiary());
		setFill(1, style.getInteractiveNormal());
		setFill(2, style.getAccent());
		setFill(3, style.getBackgroundFloating());
		setFill(4, Color.WHITE);
		setFill(5, Color.WHITE);
		setFill(6, Color.WHITE);
		setFill(7, Color.WHITE);

		colorNormal = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(getFillProperty(2), style.getAccent(), Interpolator.EASE_BOTH)));
	}

	private class Transition {
		Timeline show;
		Timeline hide;
		Timeline colorShow;

		boolean showing = false;
		boolean hiding = false;

		int layer;

		Transition(int layer, double size, int by) {
			this.layer = layer;
			setTranslateX(layer, by * size / 4.0);
			setTranslateY(layer, by * size / (4.0 * factor));
			setOpacity(layer, 0.0);

			show = new Timeline(new KeyFrame(Duration.seconds(duration),
					new KeyValue(translateXProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
					new KeyValue(translateYProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
					new KeyValue(opacityProperty(layer), 1, SplineInterpolator.ANTICIPATEOVERSHOOT)));

			show.setOnFinished(e -> {
				postTransition(layer);
				showing = false;
			});

			hide = new Timeline(new KeyFrame(Duration.seconds(duration),
					new KeyValue(translateXProperty(layer), by * size / 4.0, SplineInterpolator.ANTICIPATEOVERSHOOT),
					new KeyValue(translateYProperty(layer), by * size / (4.0 * factor),
							SplineInterpolator.ANTICIPATEOVERSHOOT),
					new KeyValue(opacityProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT)));

			hide.setOnFinished(e -> {
				postTransition(layer);
				hiding = false;
			});

			colorShow = new Timeline(new KeyFrame(Duration.seconds(duration),
					new KeyValue(getFillProperty(2), by > 0 ? Colors.Error : Colors.GREEN, Interpolator.EASE_BOTH)));
		}

		void playShow() {
			preTransition(layer);
			showing = true;
			show.playFromStart();
			colorShow.playFromStart();
		}

		void playHide() {
			if(hiding) {
				return;
			}
			preTransition(layer);
			hiding = true;
			hide.playFromStart();
		}

		public boolean isShowing() {
			return showing;
		}

		void stop() {
			showing = false;
			show.stop();
			colorShow.stop();
		}
	}
}
