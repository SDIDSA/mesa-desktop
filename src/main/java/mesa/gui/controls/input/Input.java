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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import mesa.app.utils.Colors;
import mesa.gui.controls.Font;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public abstract class Input extends StackPane implements Styleable {

	protected Font font;

	private ObjectProperty<Color> borderProperty;

	private Timeline focus, unfocus, enter, exit;

	protected StringProperty value;

	public Input(Window window, Font font, String key) {
		getStyleClass().addAll("input", key);

		value = new SimpleStringProperty("");

		borderProperty = new SimpleObjectProperty<>();

		borderProperty.addListener((obs, ov, nv) -> {
			applyBorder(nv);
		});

		setMinHeight(40);

		hoverProperty().addListener((obs, ov, nv) -> {
			if (!isFocus()) {
				if (nv) {
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

	public void setFontFamily(String family) {
		setFont(font.setFamily(family));
	}

	public void setFontSize(int size) {
		setFont(font.setSize(size));
	}

	public void setFontWeight(FontWeight weight) {
		setFont(font.setWeight(weight));
	}

	public void setFontPosture(FontPosture posture) {
		setFont(font.setPosture(posture));
	}

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
		setBack(style.getTextBack1());
		setBorder(style.getTextBorder1(), style.getTextBorderHover1(), Colors.LINK);
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
