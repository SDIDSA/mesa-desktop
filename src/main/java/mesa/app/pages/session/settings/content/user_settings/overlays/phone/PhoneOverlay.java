package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import mesa.api.Auth;
import mesa.app.component.input.ConfCode;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.settings.content.user_settings.overlays.PasswordOverlay;
import mesa.app.utils.Colors;
import mesa.gui.controls.Font;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.controls.alert.Overlay;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.image.IsoPhone;
import mesa.gui.controls.label.Label;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class PhoneOverlay extends Overlay implements Styleable {

	private String pending;

	private StackPane root;
	private IsoPhone isoPhone;

	private Label head;
	private Label smsCodeNode;
	private Label phoneUsePre;
	private Label oneAccount;
	private Label phoneUsePost;
	private Label invalid;

	private Label verifyHead;
	private Label enterCode;
	private Button resend;

	private PhoneInput input;

	public PhoneOverlay(SessionPage owner) {
		super(owner);

		root = new StackPane();
		root.setMaxWidth(472);
		root.setAlignment(Pos.TOP_CENTER);

		root.setClip(new Rectangle(0, -100, 472, 800));

		isoPhone = new IsoPhone(160);
		isoPhone.setTranslateY(-64);

		VBox content = new VBox();
		content.setPickOnBounds(false);
		content.setAlignment(Pos.BOTTOM_CENTER);
		content.setPadding(new Insets(106, 16, 16, 16));
		content.setMinHeight(310);

		VBox verify = new VBox();
		verify.setPickOnBounds(false);
		verify.setAlignment(Pos.BOTTOM_CENTER);
		verify.setPadding(new Insets(106, 16, 16, 16));
		verify.setMinHeight(310);

		head = new Label(owner.getWindow(), "enter_phone", new Font(20, FontWeight.BOLD));
		VBox.setMargin(head, new Insets(0, 0, 14, 0));

		verifyHead = new Label(owner.getWindow(), "verify_number", new Font(20, FontWeight.BOLD));
		VBox.setMargin(verifyHead, new Insets(0, 0, 12, 0));

		smsCodeNode = new Label(owner.getWindow(), "sms_code_note", new Font(Font.DEFAULT_FAMILY_MEDIUM, 15));
		VBox.setMargin(smsCodeNode, new Insets(0, 0, 24, 0));

		enterCode = new Label(owner.getWindow(), "phone_enter_code", new Font(Font.DEFAULT_FAMILY_MEDIUM, 15));
		VBox.setMargin(enterCode, new Insets(0, 0, 12, 0));

		phoneUsePre = new Label(owner.getWindow(), "phone_use_pre", new Font(15));
		oneAccount = new Label(owner.getWindow(), "one_account", new Font(15, FontWeight.BOLD));
		phoneUsePost = new Label(owner.getWindow(), "phone_use_post", new Font(15));

		TextFlow phoneUse = new TextFlow(phoneUsePre, oneAccount, phoneUsePost);
		VBox.setMargin(phoneUse, new Insets(0, 0, 22, 0));
		phoneUse.setTextAlignment(TextAlignment.CENTER);
		phoneUse.setLineSpacing(7);

		invalid = new Label(owner.getWindow(), "phone_invalid", new Font(15));
		invalid.setFill(Colors.Error);
		VBox.setMargin(invalid, new Insets(0, 0, 20, 0));

		input = new PhoneInput(owner.getWindow());

		ConfCode confCode = new ConfCode(owner.getWindow(), "", 6, 360);

		resend = new Button(owner.getWindow(), "Back", 4.0, 16, 32);
		resend.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		resend.setUlOnHover(true);
		VBox.setMargin(resend, new Insets(30, 0, 0, 0));

		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

		Runnable onInvalid = () -> {
			isoPhone.showError();
			if (content.getChildren().contains(smsCodeNode)) {
				content.getChildren().removeAll(smsCodeNode, phoneUse);
				content.getChildren().add(1, invalid);
			}
		};

		Runnable onValid = () -> {
			isoPhone.showNormal();
			if (content.getChildren().contains(invalid)) {
				content.getChildren().remove(invalid);
				content.getChildren().add(1, smsCodeNode);
				content.getChildren().add(2, phoneUse);
			}
		};

		Timeline showNext = new Timeline(new KeyFrame(Duration.seconds(.2),
				new KeyValue(content.opacityProperty(), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(content.scaleXProperty(), .5, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(content.scaleYProperty(), .5, SplineInterpolator.ANTICIPATEOVERSHOOT),

				new KeyValue(verify.opacityProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(verify.scaleXProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(verify.scaleYProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT)));

		Timeline showPrevious = new Timeline(new KeyFrame(Duration.seconds(.2),
				new KeyValue(content.opacityProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(content.scaleXProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(content.scaleYProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),

				new KeyValue(verify.opacityProperty(), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(verify.scaleXProperty(), .5, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(verify.scaleYProperty(), .5, SplineInterpolator.ANTICIPATEOVERSHOOT)));

		showNext.setOnFinished(e -> {
			content.setCache(false);
			content.setCacheHint(CacheHint.DEFAULT);

			verify.setCache(false);
			verify.setCacheHint(CacheHint.DEFAULT);
		});

		showPrevious.setOnFinished(e -> {
			content.setCache(false);
			content.setCacheHint(CacheHint.DEFAULT);

			verify.setCache(false);
			verify.setCacheHint(CacheHint.DEFAULT);
		});

		verify.setScaleX(.5);
		verify.setScaleY(.5);
		verify.setOpacity(0);
		verify.setMouseTransparent(true);

		Runnable next = () -> {
			confCode.clear();

			isoPhone.showSms();
			content.setMouseTransparent(true);
			verify.setMouseTransparent(false);

			content.setCache(true);
			content.setCacheHint(CacheHint.SPEED);
			verify.setCache(true);
			verify.setCacheHint(CacheHint.SPEED);

			showNext.playFromStart();
		};

		Runnable previous = () -> {
			isoPhone.showNormal();
			content.setMouseTransparent(false);
			verify.setMouseTransparent(true);

			content.setCache(true);
			content.setCacheHint(CacheHint.SPEED);
			verify.setCache(true);
			verify.setCacheHint(CacheHint.SPEED);

			showPrevious.playFromStart();
		};

		resend.setAction(previous);

		input.setAction(value -> {
			try {
				PhoneNumber number = phoneUtil.parse(value, input.getSelectedCountry().getCode());
				boolean isValid = phoneUtil.isValidNumber(number);
				if (isValid && phoneUtil.getNumberType(number).equals(PhoneNumberType.MOBILE)) {
					onValid.run();
					pending = phoneUtil.format(number, PhoneNumberFormat.INTERNATIONAL);

					input.startLoading();
					Auth.sendPhoneCode(owner.getUser().getId(), pending, result -> {
						if (!result.has("err")) {
							next.run();
						}

						input.stopLoading();
					});
				} else {
					onInvalid.run();
				}
			} catch (Exception e) {
				onInvalid.run();
			}
		});

		PasswordOverlay finalize = new PasswordOverlay(owner, "confirm_changes_password", "confirm", 472);
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
					resend.setAction(this::hide);
					
					verify.getChildren().remove(confCode);
					enterCode.setKey("");
					verifyHead.setKey("phone_changed");
					isoPhone.showCorrect();
					finalize.hide();
					
					owner.getUser().setPhone(result.getString("phone"));
				}
				
				finalize.stopLoading();
			});
		});

		confCode.valueProperty().addListener((obs, ov, nv) -> {
			if (!nv.isEmpty()) {
				confCode.setDisable(true);
				resend.startLoading();

				Auth.verifyPhone(owner.getUser().getId(), pending, nv, result -> {
					if (result.has("err")) {
						isoPhone.showIncorrect();
						confCode.setError("verification_code_incorrect");

						confCode.setDisable(false);
						resend.stopLoading();
					} else {
						finalize.show();
					}
				});
			}
		});

		content.getChildren().addAll(head, smsCodeNode, phoneUse, input);
		verify.getChildren().addAll(verifyHead, enterCode, confCode, resend);

		root.getChildren().addAll(isoPhone, content, verify);

		VBox.setMargin(root, new Insets(40, 0, 0, 0));
		setContent(root);

		applyStyle(owner.getWindow().getStyl());
	}

	@Override
	public void show() {
		input.load(getWindow());
		super.show();
	}

	@Override
	public void hide() {
		input.unload();
		super.hide();
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBack1(), 5.0));
		isoPhone.applyStyle(style);

		head.setFill(style.getText1());
		smsCodeNode.setFill(style.getText2());

		verifyHead.setFill(style.getText1());
		enterCode.setFill(style.getText2());

		phoneUsePre.setFill(style.getText2());
		oneAccount.setFill(style.getText2());
		phoneUsePost.setFill(style.getText2());

		resend.setFill(Color.TRANSPARENT);
		resend.setTextFill(style.getText1());
	}

}
