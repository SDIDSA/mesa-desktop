package mesa.app.pages.login;

import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mesa.api.Auth;
import mesa.app.component.Form;
import mesa.app.component.input.TextInputField;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.label.keyed.KeyedLink;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.space.Separator;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class Login extends LoginSubPage {

	private Label web;
	private Label etsya;
	private Label needAcc;
	private Button loginButton;
	private Separator sep;

	private Runnable onRegister;
	private Consumer<JSONObject> onVerify;
	private Consumer<JSONObject> onSuccess;

	private Form form;

	private Text placeholder;
	
	public Login(Window window) {
		HBox root = new HBox(32);
		setRoot(root);

		VBox left = new VBox(0);

		loginButton = new Button(window, "login", 414);
		VBox.setMargin(loginButton, new Insets(0, 0, 12, 0));

		web = new Label(window, "welcome_back", new Font(Font.DEFAULT_FAMILY_MEDIUM, 24));
		VBox.setMargin(web, new Insets(0, 0, 8, 0));
		etsya = new Label(window, "etsya", new Font(16));
		etsya.setOpacity(.7);
		VBox.setMargin(etsya, new Insets(0, 0, 20, 0));

		TextInputField email = new TextInputField(window, "email_phone", 414);
		VBox.setMargin(email, new Insets(0, 0, 20, 0));
		TextInputField password = new TextInputField(window, "password", 414, true);
		VBox.setMargin(password, new Insets(0, 0, 4, 0));

		KeyedLink recover = new KeyedLink(window, "recover", new Font(14));
		VBox.setMargin(recover, new Insets(0, 0, 20, 0));

		HBox bottom = new HBox(5);
		bottom.setAlignment(Pos.CENTER_LEFT);

		needAcc = new Label(window, "need_account", new Font(14));
		needAcc.setOpacity(.5);

		KeyedLink register = new KeyedLink(window, "register", new Font(14));

		bottom.getChildren().addAll(needAcc, register);

		left.getChildren().addAll(web, etsya, email, password, recover, loginButton, bottom);

		VBox right = new VBox(0);
		right.setAlignment(Pos.CENTER);
		right.setMinWidth(240);
		
		placeholder = new Text("TODO : QR code Login");
	
		right.getChildren().add(placeholder);

		sep = new Separator(Orientation.VERTICAL);
		
		root.getChildren().addAll(left,sep, right);

		getChildren().add(root);

		form = NodeUtils.getForm(left);
		form.setField("email_phone", "zinou.teyar@gmail.com");
		form.setField("password", "a1b2.a1b2");
		form.setDefaultButton(loginButton);
		loginButton.setAction(() -> {
			if (form.check()) {
				loginButton.startLoading();

				Auth.auth(email.getValue(), password.getValue(), window.getMainSocket().id(), result -> {
					if (result.has("err")) {
						form.applyErrors(result.getJSONArray("err"));
					} else {
						JSONObject user = result.getJSONObject("user");
						String next = result.getString("next");
						if (next.equals("verify")) {
							onVerify.accept(user);
						} else if (next.equals("success")) {
							onSuccess.accept(user);
						}
					}

					loginButton.stopLoading();
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

		loginButton.setFill(style.getAccent());
		loginButton.setTextFill(Color.WHITE);
		web.setFill(style.getHeaderPrimary());
		etsya.setFill(style.getHeaderSecondary());
		needAcc.setFill(style.getTextMuted());
		sep.setFill(style.getBackgroundModifierAccent());
		
		placeholder.setFill(style.getHeaderSecondary());
	}

}
