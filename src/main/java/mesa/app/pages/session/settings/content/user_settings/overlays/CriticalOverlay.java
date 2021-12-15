package mesa.app.pages.session.settings.content.user_settings.overlays;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.MultiText;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;

public class CriticalOverlay extends PasswordOverlay {
	private MultiText warning;

	public CriticalOverlay(SessionPage session, String headKey, String warningKey) {
		super(session, headKey, headKey);

		warning = new MultiText(session.getWindow(), warningKey, new Font(Font.DEFAULT_FAMILY_MEDIUM, 14.5));
		warning.setLineSpacing(5);
		warning.setPadding(new Insets(10));
		VBox.setMargin(warning, new Insets(0, 0, 20, 0));

		root.getChildren().add(2, warning);

		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		if (warning != null) {

			warning.setFill(Color.WHITE);
			warning.setBackground(Backgrounds.make(style.getTextWarning(), 5.0));
		}
	}

}
