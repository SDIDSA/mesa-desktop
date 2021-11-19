package mesa.app.pages.session.settings.content.userSettings;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class UnverifiedEmail extends VBox implements Styleable {
	private HBox root;
	private Label head;
	private Label body;

	public UnverifiedEmail(Settings settings) {
		root = new HBox(20);
		root.setPadding(new Insets(20));
		
		ColorIcon icon = new ColorIcon(settings.getWindow(), "genie", 48);
		icon.setFill(Color.web("#faa61a"));
		
		VBox right = new VBox();
		
		head = new Label(settings.getWindow(), "unverified_email", new Font(12, FontWeight.BOLD));
		head.setTransform(TextTransform.UPPERCASE);
		
		body = new Label(settings.getWindow(), "email_verification_warning", new Font(13));
		TextFlow preBody = new TextFlow();
		preBody.getChildren().add(body);
		
		right.getChildren().addAll(head, new FixedVSpace(8), preBody);
		
		root.getChildren().addAll(icon, right);
		
		getChildren().addAll(root, new FixedVSpace(20));
		
		applyStyle(settings.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBack3().deriveColor(0, 1, 1, .6), 5.0));
		root.setBorder(Borders.make(style.getBack3(), 5.0));
		
		head.setFill(style.getInteractiveNormal());
		body.setFill(style.getText2());
	}
}
