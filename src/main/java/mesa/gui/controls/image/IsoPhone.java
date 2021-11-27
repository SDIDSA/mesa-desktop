package mesa.gui.controls.image;

import java.util.HashMap;

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
	private Timeline colorError;
	private Timeline colorSms;
	private Timeline colorCorrect;
	
	private Timeline showError;
	private Timeline hideError;

	private Timeline showSms;
	private Timeline hideSms;

	private Timeline showCorrect;
	private Timeline hideCorrect;

	private Timeline showIncorrect;
	private Timeline hideIncorrect;
	
	public IsoPhone(double size) {
		super(size, "ips", "ipf", "ipe", "ipc", "ipr", "ipm", "ipt", "ipi");
		
		setFocusTraversable(true);
		
		init(4, size, 1);
		init(5, size, -1);
		init(6, size, -1);
		init(7, size, 1);

		colorError =  new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(getFillProperty(3), Colors.Error, Interpolator.EASE_BOTH)));
		
		colorSms = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(getFillProperty(3), Colors.GREEN, Interpolator.EASE_BOTH)));
		
		colorCorrect = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(getFillProperty(3), Colors.GREEN, Interpolator.EASE_BOTH)));
		
		showError = showLayer(4);
		hideError = hideLayer(4, size, 1);
		
		showSms = showLayer(5);
		hideSms = hideLayer(5, size, -1);
		
		showCorrect = showLayer(6);
		hideCorrect = hideLayer(6, size, -1);
		
		showIncorrect = showLayer(7);
		hideIncorrect = hideLayer(7, size, 1);
	}
	
	private void init(int layer, double size, int by) {
		setTranslateX(layer, by * size / 4.0);
		setTranslateY(layer, by * size / (4.0 * factor));
		setOpacity(layer, 0.0);
	}
	
	private HashMap<Integer, Boolean> mutex = new HashMap<>();
	
	private Timeline showLayer(int layer) {
		Timeline res = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(translateXProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(translateYProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(opacityProperty(layer), 1, SplineInterpolator.ANTICIPATEOVERSHOOT)
			));
		res.setOnFinished(e-> mutex.put(layer, false));
		return res;
	}
	
	private Timeline hideLayer(int layer, double size, int by) {
		Timeline res = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(translateXProperty(layer), by * size / 4.0, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(translateYProperty(layer), by * size / (4.0 * factor), SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(opacityProperty(layer), 0, SplineInterpolator.ANTICIPATEOVERSHOOT)
			));
		res.setOnFinished(e-> mutex.put(layer, false));
		return res;
	}

	private void stopAll() {
		colorNormal.stop();
		colorError.stop();
		colorCorrect.stop();
		
		showError.stop();
		hideError.stop();
		showSms.stop();
		hideSms.stop();
		showCorrect.stop();
		hideCorrect.stop();
		showIncorrect.stop();
		hideIncorrect.stop();
	}
	
	public void showError() {
		if(Boolean.TRUE.equals(mutex.get(4))) {
			return;
		}
		mutex.put(4, true);
		
		stopAll();

		hideSms.playFromStart();
		hideCorrect.playFromStart();
		hideIncorrect.playFromStart();
		
		colorError.playFromStart();
		showError.playFromStart();
	}
	
	public void showSms() {
		if(Boolean.TRUE.equals(mutex.get(5))) {
			return;
		}
		mutex.put(5, true);
		
		stopAll();

		hideError.playFromStart();
		hideCorrect.playFromStart();
		hideIncorrect.playFromStart();
		
		colorSms.playFromStart();
		showSms.playFromStart();
	}
	
	public void showCorrect() {
		if(Boolean.TRUE.equals(mutex.get(6))) {
			return;
		}
		mutex.put(6, true);
		
		stopAll();

		hideError.playFromStart();
		hideSms.playFromStart();
		hideIncorrect.playFromStart();
		
		colorCorrect.playFromStart();
		showCorrect.playFromStart();
	}
	
	public void showIncorrect() {
		if(Boolean.TRUE.equals(mutex.get(7))) {
			return;
		}
		mutex.put(7, true);
		
		stopAll();

		hideError.playFromStart();
		hideSms.playFromStart();
		hideCorrect.playFromStart();
		
		colorError.playFromStart();
		showIncorrect.playFromStart();
	}

	public void showNormal() {
		if(Boolean.TRUE.equals(mutex.get(0))) {
			return;
		}
		mutex.put(0, true);
		
		stopAll();

		hideError.playFromStart();
		hideSms.playFromStart();
		hideCorrect.playFromStart();
		hideIncorrect.playFromStart();
		
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
		colorNormal.setOnFinished(e-> mutex.put(0, false));
	}
}
