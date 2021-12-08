package mesa.gui.controls.alert;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mesa.app.pages.Page;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.factory.Backgrounds;
import mesa.gui.window.Window;

public class Overlay extends StackPane {
	private Page owner;

	private StackPane back;
	private VBox content;

	private Timeline show;
	private Timeline hide;

	private ArrayList<Runnable> onShown;
	private ArrayList<Runnable> onShowing;
	private ArrayList<Runnable> onHidden;
	private ArrayList<Runnable> onHiding;

	private boolean autoHide = true;

	public Overlay(Page owner) {
		this.owner = owner;
		
		onShown = new ArrayList<>();
		onShowing = new ArrayList<>();
		onHidden = new ArrayList<>();
		onHiding = new ArrayList<>();
		
		back = new StackPane();
		back.setBackground(Backgrounds.make(Color.gray(0, .8)));

		content = new VBox();
		content.setAlignment(Pos.CENTER);
		content.setPickOnBounds(false);

		back.setCache(true);
		back.setCacheHint(CacheHint.SPEED);

		content.setCache(true);
		content.setCacheHint(CacheHint.SPEED);

		show = new Timeline(new KeyFrame(Duration.seconds(.15),
				new KeyValue(back.opacityProperty(), 1, SplineInterpolator.OVERSHOOT),
				new KeyValue(content.opacityProperty(), 1, SplineInterpolator.OVERSHOOT),
				new KeyValue(content.scaleXProperty(), 1, SplineInterpolator.OVERSHOOT),
				new KeyValue(content.scaleYProperty(), 1, SplineInterpolator.OVERSHOOT)));

		show.setOnFinished(e -> onShown.forEach(Runnable::run));

		hide = new Timeline(new KeyFrame(Duration.seconds(.15),
				new KeyValue(back.opacityProperty(), 0, SplineInterpolator.ANTICIPATE),
				new KeyValue(content.opacityProperty(), 0, SplineInterpolator.ANTICIPATE),
				new KeyValue(content.scaleXProperty(), .7, SplineInterpolator.ANTICIPATE),
				new KeyValue(content.scaleYProperty(), .7, SplineInterpolator.ANTICIPATE)));

		back.setOnMouseClicked(e -> {
			if (autoHide)
				hide();
		});

		getChildren().addAll(back, content);
	}

	public void setAutoHide(boolean autoHide) {
		this.autoHide = autoHide;
	}

	public void addOnShown(Runnable onShown) {
		this.onShown.add(onShown);
	}

	public void addOnShowing(Runnable onShowing) {
		this.onShowing.add(onShowing);
	}

	public void addOnHidden(Runnable onHidden) {
		this.onHidden.add(onHidden);
	}

	public void addOnHiding(Runnable onHiding) {
		this.onHiding.add(onHiding);
	}

	public void addOnShown(int index, Runnable onShown) {
		this.onShown.add(index, onShown);
	}

	public void setContent(Node... cont) {
		this.content.getChildren().setAll(cont);
	}

	public void show() {
		hide.stop();
		back.setOpacity(0);
		content.setScaleX(.7);
		content.setScaleY(.7);
		if (!owner.getChildren().contains(this)) {
			last().setDisable(true);
			owner.getChildren().add(this);
		}
		show.playFromStart();
		onShowing.forEach(Runnable::run);
	}

	public void hide() {
		show.stop();
		hide.setOnFinished(e -> {
			owner.getChildren().remove(this);
			last().setDisable(false);

			onHidden.forEach(Runnable::run);
		});
		hide.playFromStart();
		onHiding.forEach(Runnable::run);
	}

	private Node last() {
		return owner.getChildren().get(owner.getChildren().size() - 1);
	}

	public Page getOwner() {
		return owner;
	}

	public Window getWindow() {
		return owner.getWindow();
	}
}
