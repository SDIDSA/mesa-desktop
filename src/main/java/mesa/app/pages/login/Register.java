package mesa.app.pages.login;

import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mesa.api.Auth;
import mesa.app.component.Form;
import mesa.app.component.input.DateInputField;
import mesa.app.component.input.TextInputField;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.Link;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class Register extends LoginSubPage {
	private static int width = 350;

	private Button contin;

	private Label crec;
	private Label haveAcc;

	private Consumer<JSONArray> onLogin;

	public Register(Window window) {
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);
		setRoot(root);

		crec = new Label(window, "create_account", new Font(Font.DEFAULT_FAMILY_MEDIUM, 24));

		GridPane fields = new GridPane();
		fields.setHgap(20);
		fields.setVgap(20);

		contin = new Button(window, "continue", width);

		HBox bottom = new HBox(5);
		bottom.setAlignment(Pos.CENTER_LEFT);

		haveAcc = new Label(window, "have_account", new Font(14));
		haveAcc.setOpacity(.5);

		Link login = new Link(window, "login", new Font(14));

		bottom.getChildren().addAll(haveAcc, login);

		TextInputField email = new TextInputField(window, "email", width);
		TextInputField username = new TextInputField(window, "username", width);
		TextInputField password = new TextInputField(window, "password", width, true);
		DateInputField birth = new DateInputField(window, "birth_date", width);
		fields.add(email, 0, 0);
		fields.add(username, 1, 0);
		fields.add(password, 0, 1);
		fields.add(birth, 1, 1);
		fields.add(bottom, 0, 2);
		fields.add(contin, 1, 2);

		root.getChildren().addAll(crec, fields);

		getChildren().add(root);

		login.setAction(() -> {
			if (onLogin != null) {
				onLogin.accept(null);
			}
		});

		Form form = NodeUtils.getForm(fields);
		form.setDefaultButton(contin);
		contin.setAction(() -> {
			if (form.check()) {
				contin.startLoading();

				Auth.register(email.getValue(), username.getValue(), password.getValue(), birth.getValue(), result -> {
					if (result.has("err")) {
						form.applyErrors(result.getJSONArray("err"));
					} else {
						JSONArray arr = new JSONArray();
						JSONObject em = new JSONObject();
						em.put("key", "email_phone");
						em.put("value", email.getValue());

						arr.put(em);
						onLogin.accept(arr);

						form.clear();
					}

					contin.stopLoading();
				});
			}
		});

		applyStyle(window.getStyl());
	}

	public void setOnLogin(Consumer<JSONArray> onLogin) {
		this.onLogin = onLogin;
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		contin.setFill(style.getAccent());
		contin.setTextFill(style.getText1());
		crec.setFill(style.getText1());
		haveAcc.setFill(style.getText1());
	}

}
