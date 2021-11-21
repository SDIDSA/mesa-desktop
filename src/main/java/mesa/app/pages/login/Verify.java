package mesa.app.pages.login;

import java.util.function.Consumer;

import org.json.JSONObject;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mesa.api.Auth;
import mesa.app.component.Form;
import mesa.app.component.input.ConfCode;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Animator;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.Link;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class Verify extends LoginSubPage {

	private Runnable onLogout;
	
	private Consumer<JSONObject> onSuccess;

	private Label info;
	private Button verifyNow;
	private Button later;
	private Button verifyButton;
	private ConfCode code;

	private Form form;
	
	private JSONObject user;
	public Verify(Window window) {
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);

		info = new Label(window, "email_unverified", new Font(Font.DEFAULT_FAMILY_MEDIUM, 16));

		VBox buttons = new VBox(10);
		buttons.setAlignment(Pos.CENTER_RIGHT);

		code = new ConfCode(window, "verification_code", 500);
		
		HBox bottom = new HBox();
		Link resend = new Link(window, "resend_code", new Font(14));
		Link hide = new Link(window, "hide", new Font(14));
		
		bottom.getChildren().addAll(resend, new ExpandingHSpace(), hide);
		
		verifyButton = new Button(window, "verify", 500);
		
		VBox now = new VBox(15);

		now.setMaxHeight(0);
		now.setMinHeight(0);
		now.setOpacity(0);
		now.setMouseTransparent(true);
		
		now.getChildren().addAll(code, bottom, verifyButton);
		
		verifyNow = new Button(window, "verify_now", 500);
		later = new Button(window, "verify_later", 500);

		Link logout = new Link(window, "logout", new Font(14));
		
		buttons.getChildren().addAll(verifyNow, later, logout);

		root.getChildren().addAll(info, new FixedVSpace(32), now, buttons);

		getChildren().add(root);

		setRoot(root);
		

		logout.setAction(() -> {
			if (onLogout != null) {
				onLogout.run();
			}
		});

		verifyNow.setAction(()-> {
			Animator.show(now, 163);
			Animator.show(root, 307);
			verifyNow.hide();
		});
		
		later.setAction(()-> onSuccess.accept(user));
		
		hide.setAction(()-> {
			Animator.hide(now);
			Animator.hide(root, 188);
			verifyNow.show();
		});
		
		form = NodeUtils.getForm(code);
		form.setDefaultButton(verifyButton);
		verifyButton.setAction(()-> {
			if(form.check()) {
				verifyButton.startLoading();
				
				Auth.verifyEmail(user.getString("id"), code.getValue(), result-> {
					if(result.has("err")) {
						form.applyErrors(result.getJSONArray("err"));
					}else {
						user.put("email_confirmed", true);
						onSuccess.accept(user);
					}
					
					verifyButton.stopLoading();
				});
			}
		});

		applyStyle(window.getStyl());
	}

	public void setOnLogout(Runnable onLogout) {
		this.onLogout = onLogout;
	}

	public void setOnSuccess(Consumer<JSONObject> onSuccess) {
		this.onSuccess = onSuccess;
	}
	
	public void loadData(JSONObject user) {
		this.user = user;
		info.addParam(0, hideMail(user.getString("email")));
	}

	private String hideMail(String email) {
		return TextTransform.HIDE_EMAIL.apply(email);
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		info.setFill(style.getText1());
		verifyButton.setTextFill(style.getText1());
		verifyButton.setFill(style.getAccent());
		verifyNow.setTextFill(style.getText1());
		verifyNow.setFill(style.getAccent());
		later.setTextFill(style.getText1());
		later.setFill(style.getBack4());
	}
}
