package mesa.app.pages.session.settings.content.user_settings.overlays;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;

public class CriticalOverlay extends PasswordOverlay {

	private Label warning;

	public CriticalOverlay(SessionPage session, String headKey, String warningKey) {
		super(session, headKey, headKey);

		warning = new Label(session.getWindow(), warningKey, new Font(Font.DEFAULT_FAMILY_MEDIUM, 14.5));

		TextFlow preWarning = new TextFlow(warning);
		preWarning.setLineSpacing(5);
		preWarning.setPadding(new Insets(10));
		preWarning.setBackground(Backgrounds.make(Color.web("#faa81a"), 5.0));

		VBox.setMargin(preWarning, new Insets(0, 0, 20, 0));

		root.getChildren().add(2, preWarning);

		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		if (warning != null)
			warning.setFill(style.getText1());
	}

}
