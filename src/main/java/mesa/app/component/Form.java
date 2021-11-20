package mesa.app.component;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.Interpolator;
import javafx.scene.input.KeyCode;
import mesa.app.component.input.InputField;
import mesa.gui.controls.Button;

public class Form extends ArrayList<InputField> {
	static final long serialVersionUID = 6846840;

	private Button defaultButton;
	
	public Form(ArrayList<InputField> nodesOfType) {
		super(nodesOfType);
		
		forEach(input -> {
			input.addOnKeyPressed(pressed -> {
				if(pressed.equals(KeyCode.ENTER)) {
					if(defaultButton != null) {
						defaultButton.fire();
					}
				}
			});
		});
	}

	public Form() {
		super();
	}
	
	public void addAll(InputField...fields) {
		for(InputField field:fields) {
			add(field);
		}
	}

	public boolean check() {
		boolean success = true;
		Interpolator.SPLINE(0, 0, 0, 0);
		for (InputField field : this) {

			if (field.getValue().isEmpty()) {
				field.setError("field_required", null);
				success = false;
			} else {
				if (field.getKey().contains("password") && field.getValue().length() < 6) {
					field.setError("password_short", null);
					success = false;
				} else {
					field.removeError();
				}
			}
		}

		return success;
	}

	public void applyErrors(JSONArray errors) {
		for (Object obj : errors) {
			JSONObject err = (JSONObject) obj;

			String key = err.getString("key");

			for (InputField field : this) {
				if (field.getKey().equals(key)) {
					String val = err.getString("value");
					String plus = err.has("plus") ? err.getString("plus") : null;

					if (plus != null) {
						if (val.equals("username_invalid_char")) {
							plus = " " + plus + "  " + Character.getName(plus.charAt(0));
						}
					}

					field.setError(val, plus);
				}
			}
		}
	}
	
	public void clearErrors() {
		forEach(InputField::removeError);
	}
	
	public void setDefaultButton(Button button) {
		defaultButton = button;
	}

	public void loadData(JSONArray data) {
		for (Object obj : data) {
			JSONObject item = (JSONObject) obj;
			setField(item.getString("key"), item.getString("value"));
		}
	}

	public void setField(String key, String value) {
		for (InputField field : this) {
			if (field.getKey().equals(key)) {
				field.setValue(value);
				field.removeError();
			}
		}
	}

	public void clear() {
		for (InputField field : this) {
			field.clear();
		}
	}
}
