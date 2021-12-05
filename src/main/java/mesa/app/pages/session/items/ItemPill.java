package mesa.app.pages.session.items;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mesa.app.pages.session.SessionPage;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ItemPill extends Rectangle implements Styleable {
	private Timeline enter;
	private Timeline exit;
	private Timeline select;

	public ItemPill(SessionPage session) {
		setWidth(8);
		setHeight(0);
		setOpacity(0);

		setTranslateX(-5);
		setArcWidth(8);
		setArcHeight(8);

		enter = new Timeline(
				new KeyFrame(Duration.seconds(.1),
						new KeyValue(heightProperty(), 20, Interpolator.EASE_BOTH),
						new KeyValue(opacityProperty(), 1, Interpolator.EASE_BOTH)));
		exit = new Timeline(
				new KeyFrame(Duration.seconds(.1),
						new KeyValue(heightProperty(), 0, Interpolator.EASE_BOTH),
						new KeyValue(opacityProperty(), 0, Interpolator.EASE_BOTH)));
		select = new Timeline(
				new KeyFrame(Duration.seconds(.1),
						new KeyValue(heightProperty(), 40, Interpolator.EASE_BOTH),
						new KeyValue(opacityProperty(), 1, Interpolator.EASE_BOTH)));

		applyStyle(session.getWindow().getStyl());
	}

	public void enter() {
		exit.stop();
		select.stop();

		enter.playFromStart();
	}

	public void exit() {
		enter.stop();
		select.stop();

		exit.playFromStart();
	}

	public void select() {
		exit.stop();
		enter.stop();

		select.playFromStart();
	}

	@Override
	public void applyStyle(Style style) {
		setFill(style.getHeaderPrimary());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
