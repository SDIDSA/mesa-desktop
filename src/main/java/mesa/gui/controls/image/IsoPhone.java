package mesa.gui.controls.image;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import mesa.app.utils.Colors;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.style.Style;

public class IsoPhone extends LayerIcon {
	private static double factor = 1.68;
	private static double duration = .3;

	private Timeline colorNormal;

	private ArrayList<Transition> transitions;
	public IsoPhone(double size) {
		super(size, "ips", "ipf", "ipe", "ipc", "ipr", "ipm", "ipt", "ipi");

		setFocusTraversable(true);

		transitions = new ArrayList<>();
		
		transitions.add(new Transition(4, size, 1));
		transitions.add(new Transition(5, size, -1));
		transitions.add(new Transition(6, size, -1));
		transitions.add(new Transition(7, size, 1));
	}

	private void stopAll() {
		colorNormal.stop();
		transitions.forEach(Transition::stop);
	}

	private void showTransition(int transition) {
		if(transitions.get(transition).isShowing()) {
			return;
		}
		
		stopAll();
		hideOthers(transition);
		transitions.get(transition).playShow();
	}
	
	private void hideOthers(int transition) {
		for(int i = 0; i < transitions.size(); i++) {
			if(i != transition) {
				transitions.get(i).playHide();
			}
		}
	}
	
	public void showError() {
		showTransition(0);
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

	public void applyStyle(Style style) {
		setFill(0, style.getBack3());
		setFill(1, style.getInteractiveNormal());
		setFill(2, style.getBack5());
		setFill(3, style.getAccent());
		setFill(4, style.getText1());
		setFill(5, style.getText1());
		setFill(6, style.getText1());
		setFill(7, style.getText1());

		colorNormal = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(getFillProperty(3), style.getAccent(), Interpolator.EASE_BOTH)));
	}

	private class Transition {
		Timeline show;
		Timeline hide;
		Timeline colorShow;

		boolean showing = false;

		Transition(int layer, double size, int by) {
			setTranslateX(layer, by * size / 4.0);
			setTranslateY(layer, by * size / (4.0 * factor));
			setOpacity(layer, 0.0);
			
			show = new Timeline(new KeyFrame(Duration.seconds(duration),
					new KeyValue(translateXProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
					new KeyValue(translateYProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
					new KeyValue(opacityProperty(layer), 1, SplineInterpolator.ANTICIPATEOVERSHOOT)));

			show.setOnFinished(e -> showing = false);

			hide = new Timeline(new KeyFrame(Duration.seconds(duration),
					new KeyValue(translateXProperty(layer), by * size / 4.0, SplineInterpolator.ANTICIPATEOVERSHOOT),
					new KeyValue(translateYProperty(layer), by * size / (4.0 * factor), SplineInterpolator.ANTICIPATEOVERSHOOT),
					new KeyValue(opacityProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT)));

			colorShow = new Timeline(new KeyFrame(Duration.seconds(duration),
					new KeyValue(getFillProperty(3), by > 0 ? Colors.Error : Colors.GREEN, Interpolator.EASE_BOTH)));
		}

		void playShow() {
			showing = true;
			show.playFromStart();
			colorShow.playFromStart();
		}

		void playHide() {
			hide.playFromStart();
		}
		
		public boolean isShowing() {
			return showing;
		}
		
		void stop() {
			showing = false;
			show.stop();
			hide.stop();
			colorShow.stop();
		}
	}
}
