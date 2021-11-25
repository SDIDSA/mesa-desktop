package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import mesa.app.pages.Page;
import mesa.gui.controls.Font;
import mesa.gui.controls.alert.Overlay;
import mesa.gui.controls.image.IsoPhone;
import mesa.gui.controls.label.Label;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class PhoneOverlay extends Overlay implements Styleable {

	private StackPane root;
	private IsoPhone isoPhone;

	private Label head;
	private Label smsCodeNode;
	private Label phoneUsePre;
	private Label oneAccount;
	private Label phoneUsePost;

	public PhoneOverlay(Page owner) {
		super(owner);

		root = new StackPane();
		root.setMaxWidth(472);
		root.setAlignment(Pos.TOP_CENTER);

		isoPhone = new IsoPhone(160);
		isoPhone.setTranslateY(-64);

		VBox content = new VBox();
		content.setAlignment(Pos.TOP_CENTER);
		content.setPadding(new Insets(106, 16, 16, 16));

		head = new Label(owner.getWindow(), "enter_phone", new Font(20, FontWeight.BOLD));
		VBox.setMargin(head, new Insets(0, 0, 14, 0));

		smsCodeNode = new Label(owner.getWindow(), "sms_code_note", new Font(Font.DEFAULT_FAMILY_MEDIUM, 15));
		VBox.setMargin(smsCodeNode, new Insets(0, 0, 24, 0));

		phoneUsePre = new Label(owner.getWindow(), "phone_use_pre", new Font(15));
		oneAccount = new Label(owner.getWindow(), "one_account", new Font(15, FontWeight.BOLD));
		phoneUsePost = new Label(owner.getWindow(), "phone_use_post", new Font(15));

		TextFlow phoneUse = new TextFlow(phoneUsePre, oneAccount, phoneUsePost);
		VBox.setMargin(phoneUse, new Insets(0, 0, 22, 0));
		phoneUse.setTextAlignment(TextAlignment.CENTER);
		phoneUse.setLineSpacing(7);

		PhoneInput input = new PhoneInput(owner.getWindow());
		
		content.getChildren().addAll(head, smsCodeNode, phoneUse, input);

		root.getChildren().addAll(isoPhone, content);

		setContent(root);

		applyStyle(owner.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBack1(), 5.0));
		isoPhone.applyStyle(style);

		head.setFill(style.getText1());
		smsCodeNode.setFill(style.getText2());

		phoneUsePre.setFill(style.getText2());
		oneAccount.setFill(style.getText2());
		phoneUsePost.setFill(style.getText2());
	}

}
