package mesa.app.pages.session.settings.content.userSettings;

import org.json.JSONArray;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import mesa.app.component.Form;
import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.NodeUtils;

public class KeyValueEditOverlay extends EditOverlay {
	private TextInputField field, password;
	private Form form;

	public KeyValueEditOverlay(Settings settings, String edit_what) {
		super(settings, edit_what);

		field = new TextInputField(settings.getWindow(), edit_what, 408);
		password = new TextInputField(settings.getWindow(), "current_password", 408, true);

		center.getChildren().addAll(field, password);

		form = NodeUtils.getForm(center);
		
		form.setDefaultButton(done);

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

	public BooleanProperty doneDisabled() {
		return done.disableProperty();
	}

	public void setAction(Runnable run) {
		done.setAction(run);
	}

	public boolean checkForm() {
		return form.check();
	}

	public void applyErrors(JSONArray errors) {
		form.applyErrors(errors);
	}

	public void startLoading() {
		done.startLoading();
	}

	public void stopLoading() {
		done.stopLoading();
	}

	public void addPostField(Node... nodes) {
		field.addPostField(nodes);
	}

	public void setValue(String val) {
		field.setValue(val);
	}

	@Override
	public void hide() {
		form.clearErrors();
		password.clear();
		super.hide();
	}
}
