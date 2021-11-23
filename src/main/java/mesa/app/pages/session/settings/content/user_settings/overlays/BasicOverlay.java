package mesa.app.pages.session.settings.content.user_settings.overlays;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.style.Style;

public class BasicOverlay extends AbstractOverlay {

	protected Label head;
	protected Label subHead;

	public BasicOverlay(SessionPage session, double width) {
		super(session, width);

		StackPane.setMargin(closeIcon, new Insets(16));
		
		VBox top = new VBox(8);
		top.setPadding(new Insets(26, 16, 26, 16));
		top.setAlignment(Pos.CENTER);

		head = new Label(session.getWindow(), "", new Font(24, FontWeight.BOLD));
		head.setTransform(TextTransform.CAPITALIZE_PHRASE);

		subHead = new Label(session.getWindow(), "", new Font(15));
		subHead.setTransform(TextTransform.CAPITALIZE_PHRASE);

		top.getChildren().addAll(head, subHead);

		root.getChildren().add(0, top);
		
		applyStyle(session.getWindow().getStyl());
	}

	public BasicOverlay(SessionPage session) {
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
		
		head.setFill(style.getText1());
		subHead.setFill(style.getInteractiveNormal());
	}
}
