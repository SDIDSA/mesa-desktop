package mesa.gui.controls.image;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ColorIcon extends StackPane implements Styleable {
	private ImageView view;
	private Rectangle overlay;

	private String name;
	private double size;

	private Runnable action;

	public ColorIcon(String name, double size, boolean focusable) {
		view = new ImageView();
		overlay = new Rectangle();
		overlay.setClip(view);

		setImage(name, size);

		if (focusable) {
			setFocusTraversable(true);

			setOnMouseClicked(this::fire);
			setOnKeyPressed(this::fire);
		}

		getChildren().addAll(overlay);
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
		if (action != null) {
			action.run();
		}
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

	public ColorIcon(String name, double size) {
		this(name, size, false);
	}

	public void setName(String name) {
		setImage(name, size);
	}

	public void setSize(int size) {
		setImage(name, size);
	}

	public void setImage(String name, double size) {
		this.name = name;
		this.size = size;
		Image img = ImageProxy.load(name, size);
		double w = img.getWidth();
		double h = img.getHeight();

		view.setImage(img);

		setMinSize(w, h);
		setMaxSize(w, h);

		overlay.setWidth(w);
		overlay.setHeight(h);
	}

	public void setPadding(double val) {
		setMinSize(size + val * 2, size + val * 2);
		setMaxSize(size + val * 2, size + val * 2);
	}

	public ObjectProperty<Paint> fillProperty() {
		return overlay.fillProperty();
	}

	public void setFill(Paint fill) {
		overlay.setFill(fill);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

	@Override
	public void applyStyle(Style style) {
		Border unfocused = Borders.make(Color.TRANSPARENT);
		Border focused = Borders.make(style.getTextLink(), 4.0);
		borderProperty().bind(Bindings.when(focusedProperty()).then(focused).otherwise(unfocused));
	}
}
