package mesa.gui.controls.input;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mesa.gui.controls.Font;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public abstract class Input extends StackPane implements Styleable {

	private ObjectProperty<Color> borderProperty;

	private Timeline focus;
	private Timeline unfocus;
	private Timeline enter;
	private Timeline exit;

	protected StringProperty value;

	protected Input(String key) {
		getStyleClass().addAll("input", key);

		value = new SimpleStringProperty("");

		borderProperty = new SimpleObjectProperty<>();

		borderProperty.addListener((obs, ov, nv) -> applyBorder(nv));

		setMinHeight(40);

		hoverProperty().addListener((obs, ov, nv) -> {
			if (!isFocus()) {
				if (nv.booleanValue()) {
					hover();
				} else {
					unhover();
				}
			}
		});
	}

	protected void focus(boolean focus) {
		if (focus) {
			focus();
		} else {
			unfocus();
		}
	}

	public void hover() {
		exit.stop();
		enter.playFromStart();
	}

	public void unhover() {
		enter.stop();
		exit.playFromStart();
	}

	public void focus() {
		unfocus.stop();
		this.focus.playFromStart();
	}

	public void unfocus() {
		this.focus.stop();
		unfocus.playFromStart();
	}

	public abstract void setFont(Font font);

	protected abstract boolean isFocus();

	private void setBack(Color fill) {
		applyBack(fill);
	}

	public void setBorder(Color border, Color hover, Color foc) {
		if (enter != null) {
			enter.stop();
			exit.stop();
			focus.stop();
			unfocus.stop();
		}

		borderProperty.set(border);

		focus = new Timeline(
				new KeyFrame(Duration.seconds(.2), new KeyValue(borderProperty, foc, Interpolator.EASE_BOTH)));
		unfocus = new Timeline(
				new KeyFrame(Duration.seconds(.2), new KeyValue(borderProperty, border, Interpolator.EASE_BOTH)));

		enter = new Timeline(
				new KeyFrame(Duration.seconds(.2), new KeyValue(borderProperty, hover, Interpolator.EASE_BOTH)));
		exit = new Timeline(
				new KeyFrame(Duration.seconds(.2), new KeyValue(borderProperty, border, Interpolator.EASE_BOTH)));
	}

	private void applyBack(Color fill) {
		setBackground(Backgrounds.make(fill, 3.0));
	}

	private void applyBorder(Color border) {
		setBorder(Borders.make(border, 3.0));
	}

	@Override
	public void applyStyle(Style style) {
		setBack(style.getDeprecatedTextInputBg());
		setBorder(style.getDeprecatedTextInputBorder(), style.getDeprecatedTextInputBorderHover(), style.getTextLink());
	}

	public String getValue() {
		return value.get();
	}

	public StringProperty valueProperty() {
		return value;
	}

	public abstract void setValue(String value);

	/**
	 * Clear the value of this input, note that this method is abstract and the
	 * implementation of this method depends on the input type
	 */
	public abstract void clear();
}
