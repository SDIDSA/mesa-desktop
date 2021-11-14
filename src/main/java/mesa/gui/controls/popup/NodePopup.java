package mesa.gui.controls.popup;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mesa.gui.NodeUtils;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class NodePopup extends PopupControl implements Styleable, Localized {
	protected Window owner;

	protected VBox root;

	public NodePopup(Window window) {
		this.owner = window;

		root = new VBox();

		setAutoHide(true);

		StackPane preroot = new StackPane();
		preroot.setPadding(new Insets(10));

		DropShadow ds = new DropShadow(15, Color.gray(0, .25));

		StackPane clipped = new StackPane();
		clipped.setEffect(ds);

		Rectangle clip = new Rectangle();
		clip.setArcHeight(20);
		clip.setArcWidth(20);
		clip.widthProperty().bind(root.widthProperty());
		clip.heightProperty().bind(root.heightProperty());

		root.setClip(clip);
		clipped.getChildren().add(root);

		preroot.getChildren().add(clipped);
		getScene().setRoot(preroot);

		applyStyle(window.getStyl());
	}

	public void showPop(Node node) {
		setOnShown(e -> {
			Bounds bounds = node.getBoundsInLocal();
			Bounds screenBounds = node.localToScreen(bounds);
			int x = (int) screenBounds.getMinX();
			int y = (int) screenBounds.getMinY();

			double px = x - getWidth() / 2 + node.getBoundsInLocal().getCenterX();

			setX(px);
			setY(y - getHeight());
		});
		this.show(owner);
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBack1(), 11.0));
		root.setBorder(Borders.make(style.getBack5(), 10.0));

		NodeUtils.applyStyle(root, style);
	}

	@Override
	public void applyLocale(Locale locale) {
		NodeUtils.applyLocale(root, locale);
	}
}
