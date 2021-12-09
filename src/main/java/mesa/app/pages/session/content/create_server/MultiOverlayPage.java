package mesa.app.pages.session.content.create_server;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MultiOverlayPage extends VBox implements Styleable {
	private StackPane preRoot;
	
	protected ColorIcon closeIcon;
	
	protected Label head;
	protected Label subHead;
	
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

		subHead = new Label(owner.getWindow(), subHeadKey, new Font(15));

		TextFlow preSubHead = new TextFlow(subHead);
		preSubHead.setTextAlignment(TextAlignment.CENTER);
		preSubHead.setLineSpacing(4);

		top.getChildren().addAll(head, preSubHead);

		root.getChildren().add(0, top);

		preRoot.getChildren().addAll(closeIcon, root);

		bottom = new HBox();
		bottom.setAlignment(Pos.CENTER_LEFT);
		bottom.setPadding(new Insets(16));
		
		getChildren().addAll(preRoot, bottom);
	}

	public MultiOverlayPage(MultiOverlay owner, String headKey, String subHeadKey) {
		this(owner, headKey, subHeadKey, 440);
	}

	@Override
	public void applyStyle(Style style) {
		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getHeaderPrimary())
				.otherwise(style.getHeaderSecondary()));
		
		head.setFill(style.getHeaderPrimary());
		subHead.setFill(style.getHeaderSecondary());
		
		bottom.setBackground(Backgrounds.make(style.getBackgroundSecondary(), new CornerRadii(0, 0, 5, 5, false)));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
