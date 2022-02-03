package mesa.app.pages.session.types.server.add_channel;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.api.Session;
import mesa.app.component.input.DeprecatedTextInputField;
import mesa.app.pages.Page;
import mesa.data.bean.ChannelGroup;
import mesa.gui.controls.Font;
import mesa.gui.controls.alert.AbstractOverlay;
import mesa.gui.controls.check.RadioGroup;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class AddChannel extends AbstractOverlay {

	private MultiText head;
	private Label subHead;

	private Label typeHead;

	private ColorIcon typeIcon;

	public AddChannel(Page session, ChannelGroup group, double width) {
		super(session, width);

		StackPane.setMargin(closeIcon, new Insets(16));

		VBox top = new VBox(4);
		top.setPadding(new Insets(32, 16, 32, 16));
		top.setAlignment(Pos.CENTER);
		top.setMouseTransparent(true);

		head = new MultiText(session.getWindow(), "Create Text Channel", new Font(24, FontWeight.BOLD));
		head.center();

		subHead = new Label(session.getWindow(), "in " + group.getName(), new Font(12));

		top.getChildren().addAll(head, subHead);

		VBox typeOptions = new VBox(8);
		typeOptions.setPadding(new Insets(0, 16, 0, 16));
		VBox.setMargin(typeOptions, new Insets(0, 0, 20, 0));

		typeHead = new Label(getWindow(), "Channel Type", new Font(12, FontWeight.BOLD));
		typeHead.setTransform(TextTransform.UPPERCASE);

		ChannelTypeOption text = new ChannelTypeOption(getWindow(), "text", "Text Channel",
				"Post images, GIFs, stickers, opinions and puns");
		ChannelTypeOption voice = new ChannelTypeOption(getWindow(), "audio", "Voice Channel",
				"Hang out with voice, video and screen sharing");

		RadioGroup typeRadio = new RadioGroup(text.getRadio(), voice.getRadio());

		typeOptions.getChildren().addAll(typeHead, text, voice);

		DeprecatedTextInputField channelName = new DeprecatedTextInputField(getWindow(), "Channel name", 408);
		channelName.setPrompt("new-channel");
		VBox.setMargin(channelName, new Insets(0, 16, 24, 16));

		typeIcon = new ColorIcon(null, 12);
		HBox.setMargin(typeIcon, new Insets(0, 0, 0, 10));

		typeRadio.valueProperty().addListener((obs, ov, nv) -> {
			Parent parent = nv.getParent();
			if(parent != null && parent instanceof ChannelTypeOption typeOption) {
				typeIcon.setImage("channel_type_" + typeOption.getType(), 12);
				head.setKey("Create " + typeOption.getHead());
			}
		});
		
		channelName.addPreField(typeIcon);

		text.getRadio().flip();
		
		root.getChildren().addAll(top, typeOptions, channelName);

		form.addAll(channelName);
		
		done.setKey("Create Channel");
		done.disableProperty().bind(channelName.valueProperty().isEmpty());
		done.setAction(()-> {
			done.startLoading();
			Session.createChannel(group.getServer().getId(), group.getId(), channelName.getValue(), ((ChannelTypeOption) typeRadio.getValue().getParent()).getType(), result-> {
				hide();
				done.stopLoading();
			});
		});
		
		addOnShowing(()-> {
			text.getRadio().flip();
			channelName.clear();
		});

		applyStyle(session.getWindow().getStyl());
	}

	public AddChannel(Page session, ChannelGroup group) {
		this(session, group, 440);
	}

	@Override
	public void applyStyle(Style style) {
		head.setFill(style.getHeaderPrimary());
		subHead.setFill(style.getHeaderSecondary());

		typeHead.setFill(style.getHeaderSecondary());
		
		typeIcon.setFill(style.getTextNormal());
		
		super.applyStyle(style);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
