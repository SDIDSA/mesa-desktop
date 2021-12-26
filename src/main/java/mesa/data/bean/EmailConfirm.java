package mesa.data.bean;

import org.json.JSONObject;
import javafx.beans.property.StringProperty;

public class EmailConfirm extends Bean {
	private StringProperty userId;
	private StringProperty code;

	public EmailConfirm(JSONObject obj) {
		init(obj);
	}

	public StringProperty userIdProperty() {
		return userId;
	}

	public String getUserId() {
		return userId.get();
	}

	public void setUserId(String val) {
		userId.set(val);
	}

	public StringProperty codeProperty() {
		return code;
	}

	public String getCode() {
		return code.get();
	}

	public void setCode(String val) {
		code.set(val);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {"
			+ "\tuserId : " + userId.get()
			+ "\tcode : " + code.get()
		+ "}";
	}
}