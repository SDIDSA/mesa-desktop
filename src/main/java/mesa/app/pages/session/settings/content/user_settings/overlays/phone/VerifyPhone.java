package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import java.util.function.Supplier;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.api.Auth;
import mesa.app.component.input.ConfCode;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.settings.content.user_settings.overlays.PasswordOverlay;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.label.Label;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class VerifyPhone extends PhoneOverlayContent implements Styleable {
	private Label verifyHead;
	private Label enterCode;
	private Button resend;

	private ConfCode confCode;

	private PasswordOverlay finalize;

	private Runnable previous;
	private Runnable hide;

	public VerifyPhone(SessionPage owner) {
		super(owner);

		verifyHead = new Label(owner.getWindow(), "verify_number", new Font(20, FontWeight.BOLD));
		VBox.setMargin(verifyHead, new Insets(0, 0, 12, 0));

		enterCode = new Label(owner.getWindow(), "phone_enter_code", new Font(Font.DEFAULT_FAMILY_MEDIUM, 15));
		VBox.setMargin(enterCode, new Insets(0, 0, 12, 0));

		confCode = new ConfCode(owner.getWindow(), "", 6, 360);

		resend = new Button(owner.getWindow(), "Back", 4.0, 16, 32);
		resend.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		resend.setUlOnHover(true);
		VBox.setMargin(resend, new Insets(30, 0, 0, 0));

		resend.setAction(() -> previous.run());

		finalize = new PasswordOverlay(owner, "confirm_changes_password", "confirm", 472);
		finalize.setAutoHide(false);
		finalize.setOnCancel(() -> {
			confCode.setDisable(false);
			resend.stopLoading();
			previous.run();
		});

		finalize.setAction(() -> {
			finalize.startLoading();
			Auth.finalizePhone(owner.getUser().getId(), finalize.getPassword(), result -> {
				if (result.has("err")) {
					finalize.applyErrors(result.getJSONArray("err"));
				} else {
					resend.stopLoading();
					resend.setKey("close");
					resend.setAction(hide);

					getChildren().remove(confCode);
					enterCode.setKey("");
					verifyHead.setKey("phone_changed");
					finalize.hide();

					owner.getUser().setPhone(result.getString("phone"));
				}

				finalize.stopLoading();
			});
		});
		
		getChildren().addAll(verifyHead, enterCode, confCode, resend);
		
		applyStyle(owner.getWindow().getStyl());
	}

	public void setHide(Runnable hide) {
		this.hide = hide;
	}

	public void setPrevious(Runnable previous) {
		this.previous = previous;
	}

	public void setAction(Runnable correct, Runnable incorrect, Supplier<String> getPending) {
		confCode.valueProperty().addListener((obs, ov, nv) -> {
			if (!nv.isEmpty()) {
				confCode.setDisable(true);
				resend.startLoading();

				Auth.verifyPhone(owner.getUser().getId(), getPending.get(), nv, result -> {
					if (result.has("err")) {
						incorrect.run();
						confCode.setError("verification_code_incorrect");

						confCode.setDisable(false);
						resend.stopLoading();
					} else {
						correct.run();
						finalize.show();
					}
				});
			}
		});
	}

	public void clear() {
		confCode.clear();
	}

	@Override
	public void applyStyle(Style style) {
		verifyHead.setFill(style.getText1());
		enterCode.setFill(style.getText2());

		resend.setFill(Color.TRANSPARENT);
		resend.setTextFill(style.getText1());
	}

}
