package mesa.app.pages.login;

import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class LoginSubPage extends StackPane implements Styleable {

	public LoginSubPage(Window window) {
		setPadding(new Insets(32));
		setEffect(new DropShadow(8, Color.gray(0, .25)));
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

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBack1(), 8.0));
	}
	
	public void destroy() {
		setCache(false);
	}
}
