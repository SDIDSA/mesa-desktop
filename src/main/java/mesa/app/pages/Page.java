package mesa.app.pages;

import java.awt.Dimension;

import org.json.JSONObject;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import mesa.app.utils.Dimensions;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;
import mesa.gui.window.content.AppPreRoot;

public abstract class Page extends StackPane implements Styleable {
	protected Window window;
	protected Dimension minSize;

	public Page(Window window, Dimension minSize) {
		this.window = window;
		this.minSize = minSize;

		Rectangle clipBottom = new Rectangle();
		double arc = 20;
		clipBottom.setArcHeight(arc);
		clipBottom.setArcWidth(arc);

		Rectangle clipTop = new Rectangle();

		widthProperty().addListener((obs, ov, nv) -> {
			clipBottom.setWidth(nv.doubleValue());
			clipTop.setWidth(nv.doubleValue());

			setClip(Shape.union(clipBottom, clipTop));
		});

		heightProperty().addListener((obs, ov, nv) -> {
			clipTop.setHeight(nv.doubleValue() / 2 + arc);
			clipBottom.setHeight(nv.doubleValue() / 2);
			clipBottom.setY(nv.doubleValue() / 2);

			setClip(Shape.union(clipBottom, clipTop));
		});

		window.paddedProperty().addListener((obs, ov, nv) -> {
			if (nv) {
				clipBottom.setArcHeight(arc);
				clipBottom.setArcWidth(arc);
			} else {
				clipBottom.setArcHeight(0);
				clipBottom.setArcWidth(0);
			}

			setClip(Shape.union(clipBottom, clipTop));
		});

		DoubleExpression height = window.heightProperty()
				.subtract(Bindings.when(window.getRoot().paddedProperty()).then(AppPreRoot.PADDING * 2).otherwise(0))
				.subtract(window.getAppBar().heightProperty()).subtract(window.getBorderWidth().multiply(2));

		setMinHeight(0);
		
		maxHeightProperty().bind(height);
	}

	public Page(Window window) {
		this(window, Dimensions.DEFAULT_WINDOW_MINSIZE);
	}

	public Window getWindow() {
		return window;
	}

	public JSONObject getJsonData(String key) {
		return window.getJsonData(key);
	}

	public void setup(Window window) {
		window.setMinSize(minSize);
	}

	public void destroy() {

	}
}
