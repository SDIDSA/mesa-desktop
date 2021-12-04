package mesa.app.pages.login;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class LoginSubPage extends StackPane implements Styleable {

	private Rectangle clip;

	public LoginSubPage() {
		setPadding(new Insets(32));
		setEffect(new DropShadow(8, Color.gray(0, .25)));

		clip = new Rectangle();

		clip.heightProperty().bind(heightProperty());
		clip.widthProperty().bind(widthProperty());
		
		setClip(clip);
	}

	public void preTransition() {
		setCache(true);
		setCacheHint(CacheHint.SPEED);
	}

	public void postTransition() {
		setCache(false);
		setCacheHint(CacheHint.DEFAULT);
	}

	public void setRoot(Region root) {
		maxHeightProperty().bind(root.heightProperty().add(64));
		maxWidthProperty().bind(root.widthProperty().add(64));
	}

	public void destroy() {
		setCache(false);
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBackgroundMobilePrimary(), 8.0));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
