package mesa.gui.controls.image;

import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;
import javafx.beans.property.DoubleProperty;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

public class Icon extends ImageView implements Styleable {
	private Window window;

	private String name;
	private int size;
	private boolean fullPath;

	private ColorAdjust ca;

	public Icon(Window window, String name, int size, boolean fullPath) {
		setPreserveRatio(true);
		this.window = window;
		this.name = name;
		this.size = size;
		this.fullPath = fullPath;

		ca = new ColorAdjust();

		setEffect(ca);

		applyStyle(window.getStyl());
	}

	public Icon(Window window, String name, int size) {
		this(window, name, size, false);
	}

	public void setIcon(String name) {
		this.name = name;
		applyStyle(window.getStyl());
	}

	public void setBrightness(double brightness) {
		ca.setBrightness(brightness);
	}

	public DoubleProperty brightnessProperty() {
		return ca.brightnessProperty();
	}

	@Override
	public void applyStyle(Style style) {
		StringBuilder path = new StringBuilder();
		path.append("/images/icons/").append(name).append('_').append(size).append(".png");
		setImage(ImageProxy.load(name, size, fullPath));
	}

}
