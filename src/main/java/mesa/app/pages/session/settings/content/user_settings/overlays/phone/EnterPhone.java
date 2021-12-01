package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import mesa.api.Auth;
import mesa.app.pages.session.SessionPage;
import mesa.app.utils.Colors;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class EnterPhone extends PhoneOverlayContent implements Styleable {	
	private String pending;
	
	private Label head;
	private Label smsCodeNode;
	private Label phoneUsePre;
	private Label oneAccount;
	private Label phoneUsePost;
	private Label invalid;
	
	private TextFlow phoneUse;

	private PhoneInput input;
	
	private PhoneNumberUtil phoneUtil;
	
	public EnterPhone(SessionPage owner) {
		super(owner);
		
		head = new Label(owner.getWindow(), "enter_phone", new Font(20, FontWeight.BOLD));
		VBox.setMargin(head, new Insets(0, 0, 14, 0));
		
		smsCodeNode = new Label(owner.getWindow(), "sms_code_note", new Font(Font.DEFAULT_FAMILY_MEDIUM, 15));
		VBox.setMargin(smsCodeNode, new Insets(0, 0, 24, 0));

		phoneUsePre = new Label(owner.getWindow(), "phone_use_pre", new Font(15));
		oneAccount = new Label(owner.getWindow(), "one_account", new Font(15, FontWeight.BOLD));
		phoneUsePost = new Label(owner.getWindow(), "phone_use_post", new Font(15));

		phoneUse = new TextFlow(phoneUsePre, oneAccount, phoneUsePost);
		VBox.setMargin(phoneUse, new Insets(0, 0, 22, 0));
		phoneUse.setTextAlignment(TextAlignment.CENTER);
		phoneUse.setLineSpacing(7);

		invalid = new Label(owner.getWindow(), "phone_invalid", new Font(15));
		invalid.setFill(Colors.Error);
		VBox.setMargin(invalid, new Insets(0, 0, 20, 0));

		input = new PhoneInput(owner.getWindow());
		
		getChildren().addAll(head, smsCodeNode, phoneUse, input);
		
		phoneUtil = PhoneNumberUtil.getInstance();
		
		applyStyle(owner.getWindow().getStyl());
	}
	
	public void valid() {
		if (getChildren().contains(invalid)) {
			getChildren().remove(invalid);
			getChildren().add(1, smsCodeNode);
			getChildren().add(2, phoneUse);
		}
	}
	
	public void invalid() {
		if (getChildren().contains(smsCodeNode)) {
			getChildren().removeAll(smsCodeNode, phoneUse);
			getChildren().add(1, invalid);
		}
	}
	
	public void setAction(Runnable onValid, Runnable onInvalid, Runnable next) {
		input.setAction(value -> {
			try {
				PhoneNumber number = phoneUtil.parse(value, input.getSelectedCountry().getCode());
				boolean isValid = phoneUtil.isValidNumber(number);
				if (isValid && phoneUtil.getNumberType(number).equals(PhoneNumberType.MOBILE)) {
					valid();
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
					invalid();
					onInvalid.run();
				}
			} catch (Exception e) {
				invalid();
				onInvalid.run();
			}
		});
	}
	
	public String getPending() {
		return pending;
	}

	@Override
	public void applyStyle(Style style) {
		head.setFill(style.getText1());
		smsCodeNode.setFill(style.getText2());

		phoneUsePre.setFill(style.getText2());
		oneAccount.setFill(style.getText2());
		phoneUsePost.setFill(style.getText2());

	}

	public void load() {
		input.load(owner.getWindow());
	}

	public void unload() {
		input.unload();
	}
}
