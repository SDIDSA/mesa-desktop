package mesa.gui.controls.input.styles;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mesa.gui.controls.input.Input;
import mesa.gui.style.Style;

public class DeprecatedInputStyle extends InputStyle {
	private ObjectProperty<Color> borderProperty;

	private Timeline focus;
	private Timeline unfocus;
	private Timeline enter;
	private Timeline exit;

	public DeprecatedInputStyle(Input input) {
		super(input);

		borderProperty = new SimpleObjectProperty<>();

		borderProperty.addListener((obs, ov, nv) -> applyBorder(nv));

		input.hoverProperty().addListener((obs, ov, nv) -> {
			if (!input.isFocus()) {
				if (nv.booleanValue()) {
					hover();
				} else {
					unhover();
				}
			}
		});
	}

	@Override
	public void focus(boolean focus) {
		if (focus) {
			focus();
		} else {
			unfocus();
		}
	}

	@Override
	public void hover() {
		exit.stop();
		enter.playFromStart();
	}

	@Override
	public void unhover() {
		enter.stop();
		exit.playFromStart();
	}

	@Override
	public void focus() {
		unfocus.stop();
		this.focus.playFromStart();
	}

	@Override
	public void unfocus() {
		this.focus.stop();
		unfocus.playFromStart();
	}

	@Override
	public void setBorder(Color border, Color hover, Color foc) {
		if (enter != null) {
			enter.stop();
			exit.stop();
			focus.stop();
			unfocus.stop();
		}

		if (input.isFocus()) {
			borderProperty.set(foc);
		} else {
			borderProperty.set(border);
		}

		focus = new Timeline(
				new KeyFrame(Duration.seconds(.2), new KeyValue(borderProperty, foc, Interpolator.EASE_BOTH)));
		unfocus = new Timeline(
				new KeyFrame(Duration.seconds(.2), new KeyValue(borderProperty, border, Interpolator.EASE_BOTH)));

		enter = new Timeline(
				new KeyFrame(Duration.seconds(.2), new KeyValue(borderProperty, hover, Interpolator.EASE_BOTH)));
		exit = new Timeline(
				new KeyFrame(Duration.seconds(.2), new KeyValue(borderProperty, border, Interpolator.EASE_BOTH)));
	}

	@Override
	public void applyStyle(Style style) {
		applyBack(style.getDeprecatedTextInputBg());
		setBorder(style.getDeprecatedTextInputBorder(), style.getDeprecatedTextInputBorderHover(), style.getTextLink());
	}

}
