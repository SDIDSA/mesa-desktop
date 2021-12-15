package mesa.app.pages.session.content.create_server;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mesa.app.pages.Page;
import mesa.app.pages.session.content.create_server.pages.MultiOverlayPage;
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
		selected = 0;

		clip = new Rectangle(width, 1000);
		clip.setArcHeight(10);
		clip.setArcWidth(10);

		clip.setY(15);

		root = new StackPane();
		root.setMaxWidth(width);
		root.setPadding(new Insets(15, 0, 15, 0));

		root.setClip(clip);

		pages = new ArrayList<>();

		addOnShowing(() -> {
			for (int i = 0; i < pages.size(); i++) {
				MultiOverlayPage page = pages.get(i);
				if (i != selected) {
					page.setTranslateX(width * (i > selected ? 1 : -1));
					page.setDisable(true);
				} else {
					page.setDisable(false);
					clip.heightProperty().bind(page.heightProp());
					clip.yProperty().bind(root.heightProperty().subtract(page.heightProp()).divide(2));
				}
			}
		});
		
		addOnHiding(() -> {
			clip.yProperty().unbind();
			clip.heightProperty().unbind();
		});

		setContent(root);

		applyStyle(owner.getWindow().getStyl());
	}

	public MultiOverlay(Page owner) {
		this(owner, 440);
	}

	public void addPage(MultiOverlayPage page) {
		pages.add(page);
		root.getChildren().add(page);
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

		slide(toHide, toLoad, page, direction);
	}

	private void slide(MultiOverlayPage toHide, MultiOverlayPage toLoad, int page, int direction) {
		double targetY = 0;

		toLoad.setup(getWindow());

		targetY = (root.getHeight() - toLoad.height()) / 2;

		Timeline slide = new Timeline(new KeyFrame(Duration.seconds(.4),
				new KeyValue(toLoad.translateXProperty(), 0, SplineInterpolator.OVERSHOOT),
				new KeyValue(toHide.translateXProperty(), -direction * root.getWidth(), SplineInterpolator.OVERSHOOT),
				new KeyValue(clip.heightProperty(), toLoad.height(), SplineInterpolator.OVERSHOOT),
				new KeyValue(clip.yProperty(), targetY, SplineInterpolator.OVERSHOOT)));

		slide.setOnFinished(e -> {
			toHide.setDisable(true);
			clip.heightProperty().bind(toLoad.heightProp());
			clip.yProperty().bind(root.heightProperty().subtract(toLoad.heightProp()).divide(2));
		});
		toLoad.setDisable(false);
		clip.yProperty().unbind();
		clip.heightProperty().unbind();
		slide.playFromStart();
		selected = page;
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBackgroundPrimary()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
