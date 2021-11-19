package mesa.app.pages.login;

import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mesa.api.Auth;
import mesa.app.component.Form;
import mesa.app.component.input.TextInputField;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.Link;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.controls.space.Separator;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class Login extends LoginSubPage {

	private Label web, etsya, needAcc;
	private Button login;

	private Runnable onRegister;
	private Consumer<JSONObject> onVerify;
	private Consumer<JSONObject> onSuccess;

	private Form form;
	
	public Login(Window window) {
		super(window);

		HBox root = new HBox(32);
		setRoot(root);

		VBox left = new VBox(0);

		login = new Button(window, "login", 414);

		web = new Label(window, "welcome_back", new Font(Font.DEFAULT_FAMILY_MEDIUM, 24));
		etsya = new Label(window, "etsya", new Font(16));
		etsya.setOpacity(.7);

		TextInputField email = new TextInputField(window, "email_phone", 414);
		TextInputField password = new TextInputField(window, "password", 414, true);

		Link recover = new Link(window, "recover", new Font(14));

		HBox bottom = new HBox(5);
		bottom.setAlignment(Pos.CENTER_LEFT);
		
		needAcc = new Label(window, "need_account", new Font(14));
		needAcc.setOpacity(.5);

		Link register = new Link(window, "register", new Font(14));

		bottom.getChildren().addAll(needAcc, register);

		left.getChildren().addAll(web, new FixedVSpace(8), etsya, new FixedVSpace(20), email, new FixedVSpace(20),
				password, new FixedVSpace(4), recover, new FixedVSpace(20), login, new FixedVSpace(12), bottom);

		VBox right = new VBox(0);
		right.setMinWidth(240);

		root.getChildren().addAll(left, new Separator(Orientation.VERTICAL), right);

		getChildren().add(root);

		form = NodeUtils.getForm(left);
		form.setField("email_phone", "+213657693679");
		form.setField("password", "a1b2.a1b2");
		form.setDefaultButton(login);
		login.setAction(() -> {
			if (form.check()) {
				login.startLoading();
				
				Auth.auth(email.getValue(), password.getValue(), result -> {
					if(result.has("err")) {
						form.applyErrors(result.getJSONArray("err"));
					}else {
						JSONObject user = result.getJSONObject("user");
						String next = result.getString("next");
						if(next.equals("verify")) {
							onVerify.accept(user);
						}else if(next.equals("success")) {
							onSuccess.accept(user);
						}
					}

					login.stopLoading();
				});
			}
		});

		register.setAction(() -> {
			if (onRegister != null) {
				onRegister.run();
			}
		});

		applyStyle(window.getStyl());
	}
	
	public void loadData(JSONArray data) {
		form.loadData(data);
	}

	public void setOnRegister(Runnable onRegister) {
		this.onRegister = onRegister;
	}
	
	public void setOnVerify(Consumer<JSONObject> onVerify) {
		this.onVerify = onVerify;
	}
	
	public void setOnSuccess(Consumer<JSONObject> onSuccess) {
		this.onSuccess = onSuccess;
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		login.setFill(style.getAccent());
		login.setTextFill(style.getText1());
		web.setFill(style.getText1());
		etsya.setFill(style.getText1());
		needAcc.setFill(style.getText1());
	}

}
