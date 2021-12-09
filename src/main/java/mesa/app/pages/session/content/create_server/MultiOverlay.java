package mesa.app.pages.session.content.create_server;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mesa.app.pages.Page;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.controls.alert.Overlay;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MultiOverlay extends Overlay implements Styleable {
	private StackPane root;

	private ArrayList<MultiOverlayPage> pages;
	private int selected;

	private Rectangle clip;

	public MultiOverlay(Page owner, double width) {
		super(owner);

		clip = new Rectangle(width, 1000);
		clip.setArcHeight(10);
		clip.setArcWidth(10);

		root = new StackPane();
		root.setMaxWidth(width);

		root.setClip(clip);

		pages = new ArrayList<>();

		setContent(root);

		applyStyle(owner.getWindow().getStyl());
	}

	public MultiOverlay(Page owner) {
		this(owner, 440);
	}

	public void addPage(MultiOverlayPage page) {
		pages.add(page);
		if (pages.size() == 1) {
			selected = 0;
			root.getChildren().setAll(page);
		}
	}

	public void next() {
		load(selected + 1);
	}

	public void back() {
		load(selected - 1);
	}

	private void load(int page) {
		int direction = page > selected ? 1 : -1;

		MultiOverlayPage toHide = pages.get(selected);

		MultiOverlayPage toLoad = pages.get(page);
		toLoad.setTranslateX(root.getWidth() * direction);

		root.getChildren().add(toLoad);

		if (toLoad.getHeight() == 0) {
			toLoad.heightProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					toLoad.heightProperty().removeListener(this);

					slide(toHide, toLoad, page, direction);
				}
			});
		} else {
			slide(toHide, toLoad, page, direction);
		}
	}

	private void slide(MultiOverlayPage toHide, MultiOverlayPage toLoad, int page, int direction) {
		clip.setHeight(toHide.getHeight());

		System.out.println(toHide.getHeight());
		System.out.println(toLoad.getHeight());
		
		double targetY = 0;

		if (toLoad.getHeight() > toHide.getHeight()) {
			clip.setY((toLoad.getHeight() - toHide.getHeight()) / 2);
			targetY = 0;
		}else {
			targetY = (toHide.getHeight() - toLoad.getHeight()) / 2;
		}

		Timeline slide = new Timeline(new KeyFrame(Duration.seconds(.4),
				new KeyValue(toLoad.translateXProperty(), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(toHide.translateXProperty(), -direction * root.getWidth(),
						SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(clip.heightProperty(), toLoad.getHeight(), SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(clip.yProperty(), targetY, SplineInterpolator.ANTICIPATEOVERSHOOT)));

		slide.setOnFinished(e -> {
			root.getChildren().remove(toHide);
			clip.setY(0);
		});

		slide.playFromStart();
		selected = page;
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBackgroundPrimary(), 5.0));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
