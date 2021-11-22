package mesa.app.pages.session.settings.content.user_settings;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import mesa.api.Auth;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.user_settings.overlays.VerifyEmailOverlay;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class UnverifiedEmail extends VBox implements Styleable {
	private HBox root;
	private Label head;
	private Label body;
	private Button resend;
	private Button verify;

	public UnverifiedEmail(Settings settings) {
		root = new HBox(20);
		root.setPadding(new Insets(20));

		ColorIcon icon = new ColorIcon("genie", 48);
		icon.setFill(Color.web("#faa61a"));

		VBox right = new VBox();

		head = new Label(settings.getWindow(), "unverified_email", new Font(12, FontWeight.BOLD));
		head.setTransform(TextTransform.UPPERCASE);

		body = new Label(settings.getWindow(), "email_verification_warning", new Font(13));
		TextFlow preBody = new TextFlow();
		preBody.getChildren().add(body);

		resend = new Button(settings.getWindow(), "resend_verification_email", 3.0, 16, 32);
		resend.setFont(new Font(13, FontWeight.BOLD));

		verify = new Button(settings.getWindow(), "verify_now", 3.0, 16, 32);
		verify.setFont(new Font(13, FontWeight.BOLD));

		VerifyEmailOverlay verifyOverlay = new VerifyEmailOverlay(settings.getSession());
		
		verifyOverlay.setAction(() -> {
			verifyOverlay.startLoading();
			
			Auth.verifyEmail(settings.getUser().getId(), verifyOverlay.getValue(), result-> {
				if(result.has("err")) {
					verifyOverlay.applyErrors(result.getJSONArray("err"));
				}else {
					settings.getUser().setEmailConfirmed(true);
					verifyOverlay.hide();
				}
				
				verifyOverlay.stopLoading();
			});
		});
		
		verify.setAction(verifyOverlay::show);
		
		HBox buttons = new HBox(16);
		
		buttons.getChildren().addAll(resend, verify);
		
		right.getChildren().addAll(head, new FixedVSpace(8), preBody, new FixedVSpace(18), buttons);

		root.getChildren().addAll(icon, right);

		getChildren().addAll(root, new FixedVSpace(20));

		applyStyle(settings.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBack3().deriveColor(0, 1, 1, .6), 5.0));
		root.setBorder(Borders.make(style.getBack3(), 5.0));

		head.setFill(style.getInteractiveNormal());
		body.setFill(style.getText2());

		resend.setTextFill(style.getText1());
		resend.setFill(style == Style.DARK ? Color.web("#4f545c") : Color.TRANSPARENT);
		
		verify.setTextFill(style.getText1());
		verify.setFill(style.getAccent());
	}
}
