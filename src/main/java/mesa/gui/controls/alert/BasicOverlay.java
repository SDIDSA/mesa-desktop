package mesa.gui.controls.alert;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.Page;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public abstract class BasicOverlay extends AbstractOverlay {

	protected Label head;
	protected Label subHead;

	protected BasicOverlay(Page session, double width) {
		super(session, width);

		StackPane.setMargin(closeIcon, new Insets(16));
		
		VBox top = new VBox(8);
		top.setPadding(new Insets(26, 16, 26, 16));
		top.setAlignment(Pos.CENTER);
		top.setMouseTransparent(true);

		head = new Label(session.getWindow(), "", new Font(24, FontWeight.BOLD));
		head.setTransform(TextTransform.CAPITALIZE_PHRASE);

		subHead = new Label(session.getWindow(), "", new Font(15));
		subHead.setTransform(TextTransform.CAPITALIZE_PHRASE);

		top.getChildren().addAll(head, subHead);

		root.getChildren().add(0, top);
		
		applyStyle(session.getWindow().getStyl());
	}

	protected BasicOverlay(Page session) {
		this(session, 440);
	}

	@Override
	public void hide() {
		form.clearErrors();
		super.hide();
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		
		head.setFill(style.getHeaderPrimary());
		subHead.setFill(style.getHeaderSecondary());
	}
	
	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
