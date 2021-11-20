package mesa.gui.controls;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import mesa.app.utils.Colors;
import mesa.gui.controls.label.Label;
import mesa.gui.factory.Borders;
import mesa.gui.window.Window;

public class Button extends StackPane {
	private Rectangle back;
	private Label label;
	private double radius;
	private DoubleProperty radiusProperty;
	private Timeline enter;
	private Timeline exit;
	private Runnable action;

	private boolean ulOnHover = false;
	private Loading load;

	private BooleanProperty loading;

	public Button(Window window, String key, double radius, double width, double height) {
		this.radius = radius;
		getStyleClass().addAll("butt", key);

		getStylesheets().clear();

		loading = new SimpleBooleanProperty(false);

		setFocusTraversable(true);
		setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
		setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
		setPrefHeight(height);

		radiusProperty = new SimpleDoubleProperty(radius);

		label = new Label(window, key);

		if (width < 50) {
			prefWidthProperty()
					.bind(Bindings.createDoubleBinding(() -> label.getBoundsInLocal().getWidth() + (width * 2),
							label.textProperty(), label.fontProperty()));
		} else {
			setPrefWidth(width);
		}

		back = new Rectangle();
		back.setFill(Color.TRANSPARENT);

		back.arcWidthProperty().bind(radiusProperty.multiply(2));
		back.arcHeightProperty().bind(radiusProperty.multiply(2));

		back.widthProperty().bind(widthProperty().subtract(Bindings.when(focusedProperty()).then(6).otherwise(0)));
		back.heightProperty().bind(heightProperty().subtract(Bindings.when(focusedProperty()).then(6).otherwise(0)));

		borderProperty()
				.bind(Bindings.when(focusedProperty()).then(Borders.make(Colors.LINK, radius)).otherwise(Border.EMPTY));

		setCursor(Cursor.HAND);

		load = new Loading(height / 5);
		load.setOpacity(.7);

		setFontSize(16);
		setFontWeight(FontWeight.BOLD);

		setOnMouseEntered(e -> {
			exit.stop();
			enter.playFromStart();
			if (ulOnHover) {
				label.setUnderline(true);
			}
		});

		setOnMouseExited(e -> {
			enter.stop();
			exit.playFromStart();
			if (ulOnHover) {
				label.setUnderline(false);
			}
		});

		setOnMouseClicked(this::fire);
		setOnKeyPressed(this::fire);

		ColorAdjust bw = new ColorAdjust();
		bw.setSaturation(-.5);
		ColorAdjust col = new ColorAdjust();

		effectProperty().bind(Bindings.when(disabledProperty()).then(bw).otherwise(col));
		back.opacityProperty().bind(Bindings.when(disabledProperty()).then(.3).otherwise(1));
		label.opacityProperty().bind(back.opacityProperty());

		getChildren().addAll(back, label);
	}

	public void setUlOnHover(boolean ulOnHover) {
		this.ulOnHover = ulOnHover;
	}

	public void show() {
		Animator.show(this, 44);
	}

	public void hide() {
		Animator.hide(this);
	}

	public void startLoading() {
		loading.set(true);
		getParent().requestFocus();
		setMouseTransparent(true);
		setFocusTraversable(false);
		getChildren().setAll(back, load);
		load.play();
	}

	public void stopLoading() {
		setMouseTransparent(false);
		setFocusTraversable(true);
		getChildren().setAll(back, label);
		load.stop();
		loading.set(false);
	}

	public BooleanProperty loadingProperty() {
		return loading;
	}

	public Button(Window window, String key, int width) {
		this(window, key, 4.0, width, 44);
	}

	public Button(Window window, String string, double radius, int width) {
		this(window, string, radius, width, 44);
	}

	private void fire(MouseEvent dismiss) {
		fire();
	}

	private void fire(KeyEvent e) {
		if (e.getCode().equals(KeyCode.SPACE)) {
			fire();
		}
	}

	public void fire() {
		if (isDisabled() || loading.get()) {
			return;
		}
		if (action != null) {
			action.run();
		}
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

	public void setFont(Font font) {
		label.setFont(font);
	}

	public void setFontFamily(String family) {
		label.setFontFamily(family);
	}

	public void setFontSize(int size) {
		label.setFontSize(size);
	}

	public void setFontWeight(FontWeight weight) {
		label.setFontWeight(weight);
	}

	public void setFontPosture(FontPosture posture) {
		label.setFontPosture(posture);
	}

	public void setTextFill(Paint fill) {
		label.setFill(fill);
		load.setFill(fill);
	}

	public double getRadius() {
		return radius;
	}

	public void setFill(Color fill) {
		back.setFill(fill);
		enter = new Timeline(new KeyFrame(Duration.seconds(.15),
				new KeyValue(back.fillProperty(), fill.darker(), Interpolator.EASE_BOTH)));

		exit = new Timeline(
				new KeyFrame(Duration.seconds(.15), new KeyValue(back.fillProperty(), fill, Interpolator.EASE_BOTH)));
	}

	public void setRadius(double radius) {
		this.radius = radius;
		radiusProperty.set(radius);
	}
}
