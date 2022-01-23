package mesa.app.pages.session.settings.content.user_settings.overlays;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import mesa.app.component.input.DeprecatedTextInputField;
import mesa.app.pages.session.SessionPage;

public class KeyValueEditOverlay extends EditOverlay {
	private DeprecatedTextInputField field;
	private DeprecatedTextInputField password;

	public KeyValueEditOverlay(SessionPage session, String key, boolean fem) {
		super(session, key, fem);
		field = new DeprecatedTextInputField(session.getWindow(), key, 408);
		password = new DeprecatedTextInputField(session.getWindow(), "current_password", 408, true);
		center.getChildren().addAll(field, password);

		form.addAll(field, password);

		addOnShown(field::requestFocus);
	}

	public KeyValueEditOverlay(SessionPage session, String key) {
		this(session, key, false);
	}

	public String getValue() {
		return field.getValue();
	}

	public String getPassword() {
		return password.getValue();
	}

	public StringProperty valueProperty() {
		return field.valueProperty();
	}

	public void addPostField(Node... nodes) {
		field.addPostField(nodes);
	}

	public void setValue(String val) {
		field.setValue(val);
	}

	@Override
	public void hide() {
		password.clear();
		super.hide();
	}
}
