package mesa.app.pages.session.settings.content.user_settings.overlays;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mesa.app.component.input.DeprecatedTextInputField;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.alert.AbstractOverlay;
import mesa.gui.controls.label.MultiText;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class PasswordOverlay extends AbstractOverlay {

	private MultiText head;
	private DeprecatedTextInputField password;

	public PasswordOverlay(SessionPage session, String headKey, String actionKey, double width) {
		super(session, width);

		StackPane.setMargin(closeIcon, new Insets(10));
		root.setPadding(new Insets(16));

		head = new MultiText(session.getWindow(), headKey, new Font(Font.DEFAULT_FAMILY_MEDIUM, 20));
		head.setLineSpacing(6);
		head.setMouseTransparent(true);
		VBox.setMargin(head, new Insets(0, 0, 16, 0));

		password = new DeprecatedTextInputField(session.getWindow(), "password", width - 32, true);

		form.addAll(password);

		done.setKey(actionKey);

		root.getChildren().addAll(head, password);
		
		addOnShown(password::requestFocus);
		applyStyle(session.getWindow().getStyl());
	}

	public PasswordOverlay(SessionPage session, String headKey, String actionKey) {
		this(session, headKey, actionKey, 440);
	}

	public String getPassword() {
		return password.getValue();
	}

	@Override
	public void hide() {
		password.clear();
		super.hide();
	}
	
	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		head.setFill(style.getHeaderPrimary());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
