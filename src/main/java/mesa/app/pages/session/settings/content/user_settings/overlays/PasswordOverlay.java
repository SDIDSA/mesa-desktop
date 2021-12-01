package mesa.app.pages.session.settings.content.user_settings.overlays;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.style.Style;

public class PasswordOverlay extends AbstractOverlay {

	private Label head;
	private TextInputField password;

	public PasswordOverlay(SessionPage session, String headKey, String actionKey, double width) {
		super(session, width);

		StackPane.setMargin(closeIcon, new Insets(10));
		root.setPadding(new Insets(16));

		head = new Label(session.getWindow(), headKey, new Font(20, FontWeight.BOLD));
		VBox.setMargin(head, new Insets(0, 0, 16, 0));

		password = new TextInputField(session.getWindow(), "password", width - 32, true);

		form.addAll(password);

		done.setKey(actionKey);

		root.getChildren().addAll(head, password);

		applyStyle(session.getWindow().getStyl());
	}

	public PasswordOverlay(SessionPage session, String headKey, String actionKey) {
		this(session, headKey, actionKey, 440);
	}

	public String getPassword() {
		return password.getValue();
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		head.setFill(style.getText1());
	}

}
