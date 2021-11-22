package mesa.app.pages.session.settings.content.user_settings.overlays;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;

public class CriticalOverlay extends AbstractOverlay {

	private Label head;
	private Label warning;
	
	private TextInputField password;

	public CriticalOverlay(SessionPage session, String headKey, String warningKey) {
		super(session);

		StackPane.setMargin(closeIcon, new Insets(10));
		root.setPadding(new Insets(16));
		head = new Label(session.getWindow(), headKey, new Font(20, FontWeight.BOLD));
		VBox.setMargin(head, new Insets(0, 0, 16, 0));

		warning = new Label(session.getWindow(), warningKey, new Font(Font.DEFAULT_FAMILY_MEDIUM, 14.5));
		
		TextFlow preWarning = new TextFlow(warning);
		preWarning.setLineSpacing(5);
		preWarning.setPadding(new Insets(10));
		preWarning.setBackground(Backgrounds.make(Color.web("#faa81a"), 5.0));

		VBox.setMargin(preWarning, new Insets(0, 0, 20, 0));
		password = new TextInputField(session.getWindow(), "password", 408, true);
		
		done.setKey(headKey);

		root.getChildren().addAll(head, preWarning, password);

		applyStyle(session.getWindow().getStyl());
	}

	public String getPassword() {
		return password.getValue();
	}
	
	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		head.setFill(style.getText1());
		warning.setFill(style.getText1());
	}

}
