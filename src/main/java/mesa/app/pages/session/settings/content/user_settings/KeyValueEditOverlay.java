package mesa.app.pages.session.settings.content.user_settings;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.settings.Settings;

public class KeyValueEditOverlay extends EditOverlay {
	private TextInputField field, password;

	public KeyValueEditOverlay(Settings settings, String edit_what) {
		super(settings, edit_what);
		field = new TextInputField(settings.getWindow(), edit_what, 408);
		password = new TextInputField(settings.getWindow(), "current_password", 408, true);
		center.getChildren().addAll(field, password);
		
		form.addAll(field, password);
		
		addOnShown(field::requestFocus);
		
		applyStyle(settings.getWindow().getStyl());
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
