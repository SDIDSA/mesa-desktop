package mesa.app.pages.session.content.create_server.pages;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.content.create_server.MultiOverlay;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class MultiOverlayPage extends VBox implements Styleable {
	private DoubleBinding height;

	private StackPane preRoot;

	protected ColorIcon closeIcon;

	protected Label head;
	protected MultiText subHead;
	

	protected VBox root;

	protected HBox bottom;

	public MultiOverlayPage(MultiOverlay owner, String headKey, String subHeadKey, double width) {
		setAlignment(Pos.CENTER);

		root = new VBox();
		root.setPickOnBounds(false);

		preRoot = new StackPane();
		preRoot.setAlignment(Pos.TOP_RIGHT);
		preRoot.setMaxWidth(width);

		closeIcon = new ColorIcon("close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(owner::hide);
		closeIcon.setCursor(Cursor.HAND);
		closeIcon.applyStyle(owner.getWindow().getStyl());
		StackPane.setMargin(closeIcon, new Insets(16));

		VBox top = new VBox(10);
		top.setPadding(new Insets(26, 16, 20, 16));
		top.setAlignment(Pos.CENTER);
		top.setMouseTransparent(true);

		head = new Label(owner.getWindow(), headKey, new Font(24, FontWeight.BOLD));
		head.setTransform(TextTransform.CAPITALIZE_PHRASE);

		subHead = new MultiText(owner.getWindow(), subHeadKey, new Font(15));
		subHead.center();
		subHead.setLineSpacing(6);

		top.getChildren().addAll(head, subHead);

		root.getChildren().add(0, top);

		preRoot.getChildren().addAll(closeIcon, root);

		bottom = new HBox();
		bottom.setAlignment(Pos.CENTER_LEFT);
		bottom.setPadding(new Insets(16));

		height = preRoot.heightProperty().add(bottom.heightProperty());

		getChildren().addAll(preRoot, bottom);
	}

	public MultiOverlayPage(MultiOverlay owner, String headKey, String subHeadKey) {
		this(owner, headKey, subHeadKey, 440);
	}

	public DoubleBinding heightProp() {
		return height;
	}

	public double height() {
		return height.get();
	}

	public void setup(Window window) {
		// method to be called by MultiOverlay right after loading this page in its owner

		// this method is supposed to be abstract but a lot of classes are extending
		// this class as of now and i'm too lazy to implement it everywhere
	}

	@Override
	public void applyStyle(Style style) {
		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getHeaderPrimary())
				.otherwise(style.getHeaderSecondary()));

		head.setFill(style.getHeaderPrimary());
		subHead.setFill(style.getHeaderSecondary());

		bottom.setBackground(Backgrounds.make(style.getBackgroundSecondary()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
