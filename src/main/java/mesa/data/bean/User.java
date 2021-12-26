package mesa.data.bean;

import org.json.JSONObject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends Bean {
	private StringProperty id;
	private StringProperty email;
	private StringProperty username;
	private StringProperty password;
	private StringProperty phone;
	private StringProperty avatar;
	private StringProperty birthDate;
	private BooleanProperty emailConfirmed;

	public User(JSONObject obj) {
		id = new SimpleStringProperty();
		email = new SimpleStringProperty();
		username = new SimpleStringProperty();
		password = new SimpleStringProperty();
		phone = new SimpleStringProperty();
		avatar = new SimpleStringProperty();
		birthDate = new SimpleStringProperty();
		emailConfirmed = new SimpleBooleanProperty();
		init(obj);
	}

	public StringProperty idProperty() {
		return id;
	}

	public String getId() {
		return id.get();
	}

	public void setId(String val) {
		id.set(val);
	}

	public StringProperty emailProperty() {
		return email;
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String val) {
		email.set(val);
	}

	public StringProperty usernameProperty() {
		return username;
	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(String val) {
		username.set(val);
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String val) {
		password.set(val);
	}

	public StringProperty phoneProperty() {
		return phone;
	}

	public String getPhone() {
		return phone.get();
	}

	public void setPhone(String val) {
		phone.set(val);
	}

	public StringProperty avatarProperty() {
		return avatar;
	}

	public String getAvatar() {
		return avatar.get();
	}

	public void setAvatar(String val) {
		avatar.set(val);
	}

	public StringProperty birthDateProperty() {
		return birthDate;
	}

	public String getBirthDate() {
		return birthDate.get();
	}

	public void setBirthDate(String val) {
		birthDate.set(val);
	}
	
	public BooleanProperty emailConfirmedProperty() {
		return emailConfirmed;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed.get();
	}

	public void setEmailConfirmed(Boolean val) {
		emailConfirmed.set(val);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {"
			+ "\tid : " + id.get()
			+ "\temail : " + email.get()
			+ "\tusername : " + username.get()
			+ "\tpassword : " + password.get()
			+ "\tphone : " + phone.get()
			+ "\tavatar : " + avatar.get()
			+ "\tbirthDate : " + birthDate.get()
			+ "\temailConfirmed : " + emailConfirmed.get()
		+ "}";
	}
}