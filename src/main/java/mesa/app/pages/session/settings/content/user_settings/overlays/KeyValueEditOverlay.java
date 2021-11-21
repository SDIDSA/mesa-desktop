package mesa.app.pages.session.settings.content.user_settings.overlays;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.SessionPage;

public class KeyValueEditOverlay extends EditOverlay {
	private TextInputField field;
	private TextInputField password;

	public KeyValueEditOverlay(SessionPage session, String key) {
		super(session, key);
		field = new TextInputField(session.getWindow(), key, 408);
		password = new TextInputField(session.getWindow(), "current_password", 408, true);
		center.getChildren().addAll(field, password);
		
		form.addAll(field, password);
		
		addOnShown(field::requestFocus);
		
		applyStyle(session.getWindow().getStyl());
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
