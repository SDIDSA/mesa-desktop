package mesa.app.pages.session.types.server.add_channel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mesa.gui.controls.Font;
import mesa.gui.controls.check.Radio;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class ChannelTypeOption extends HBox implements Styleable {

	private Radio radio;
	private ColorIcon typeIcon;

	private Label headLab;
	private Label subHeadLab;

	private String type;
	private String head;
	
	public ChannelTypeOption(Window window, String type, String head, String subHead) {
		super(10);
		this.type = type;
		this.head = head;
		setPadding(new Insets(10));
		setMinHeight(56);
		setAlignment(Pos.CENTER_LEFT);

		radio = new Radio(window, 20);
		radio.setMouseTransparent(true);

		setCursor(Cursor.HAND);
		setOnMouseClicked(e -> radio.flip());

		typeIcon = new ColorIcon("channel_type_" + type, 20);

		VBox labs = new VBox(3);
		labs.setAlignment(Pos.CENTER_LEFT);

		headLab = new Label(window, head, new Font(Font.DEFAULT_FAMILY_MEDIUM, 16));
		subHeadLab = new Label(window, subHead, new Font(Font.DEFAULT_FAMILY_MEDIUM, 12));

		labs.getChildren().addAll(headLab, subHeadLab);

		getChildren().addAll(radio, typeIcon, labs);

		applyStyle(window.getStyl());
	}

	public String getType() {
		return type;
	}
	
	public String getHead() {
		return head;
	}
	
	public Radio getRadio() {
		return radio;
	}

	@Override
	public void applyStyle(Style style) {
		backgroundProperty()
				.bind(Bindings.when(radio.checkedProperty()).then(Backgrounds.make(style.getBackgroundTertiary(), 3))
						.otherwise(Bindings.when(hoverProperty())
								.then(Backgrounds.make(style.getBackgroundModifierHover(), 3))
								.otherwise(Backgrounds.make(style.getBackgroundSecondary(), 3))));

		typeIcon.setFill(style.getHeaderSecondary());

		headLab.setFill(style.getTextNormal());
		subHeadLab.setFill(style.getHeaderSecondary());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
