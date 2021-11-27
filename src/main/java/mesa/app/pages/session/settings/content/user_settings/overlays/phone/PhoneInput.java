package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import java.util.function.Consumer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.AbstractButton;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class PhoneInput extends HBox implements Styleable {
	private AbstractButton country;
	private TextField field;
	private Button send;

	private ColorIcon showPop;

	private Text selectedCode;

	private boolean pressed;
	
	private CountryCodePopup countries;

	public PhoneInput(Window window) {
		setAlignment(Pos.CENTER);

		field = new TextField();
		field.setBackground(Background.EMPTY);
		field.setBorder(Border.EMPTY);
		field.setPadding(new Insets(12));
		field.setFont(new Font(16).getFont());

		setHgrow(field, Priority.ALWAYS);

		send = new Button(window, "phone_send", 3.0, 16, 32);
		send.setFont(new Font(13, FontWeight.BOLD));
		setMargin(send, new Insets(6, 6, 6, 0));

		country = new AbstractButton(3.0, 32);
		country.setContentPadding(new Insets(0, 12, 0, 12));
		setMargin(country, new Insets(6, 0, 6, 6));

		selectedCode = new Text("+1");
		selectedCode.setFont(new Font(14, FontWeight.BOLD).getFont());
		HBox.setMargin(selectedCode, new Insets(0, 8, 0, 0));

		country.prefWidthProperty().bind(Bindings.createDoubleBinding(
				() -> selectedCode.getBoundsInLocal().getWidth() + 40, selectedCode.textProperty()));

		showPop = new ColorIcon("expand", 8);
		showPop.setRotate(-90);

		country.add(selectedCode, showPop);

		country.setOnMousePressed(e -> pressed = true);

		country.setAction(() -> {
			if (!pressed)
				countries.hide();
			else
				countries.showPop(this);

			pressed = false;
		});

		getChildren().addAll(country, field, send);
		
		applyStyle(window.getStyl());
	}
	
	public void load(Window window) {
		if(countries != null) {
			return;
		}
		
		Timeline onShown = new Timeline(new KeyFrame(Duration.seconds(.1), new KeyValue(showPop.rotateProperty(), 0)));
		Timeline onHidden = new Timeline(
				new KeyFrame(Duration.seconds(.1), new KeyValue(showPop.rotateProperty(), -90)));

		countries = new CountryCodePopup(window);

		countries.setOnShowing(e -> {
			onHidden.stop();
			onShown.playFromStart();
		});

		countries.setOnHiding(e -> {
			onShown.stop();
			onHidden.playFromStart();
		});

		countries.setOnSelect(code -> {
			selectedCode.setText(code.getCode());
			countries.hide();
		});
	}
	
	public void unload() {
		countries = null;
	}

	private String getValue() {
		return selectedCode.getText() + field.getText();
	}

	public void setAction(Consumer<String> action) {
		send.setAction(() -> action.accept(getValue()));
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBack2(), 5.0));
		setBorder(Borders.make(style.getTextBorder1(), 5.0));

		country.setFill(style == Style.DARK ? Color.web("#4f545c") : null);
		selectedCode.setFill(style.getText1());
		showPop.setFill(style.getText1());

		field.setStyle("-fx-text-fill: " + Styleable.colorToCss(style.getText1())
				+ ";-fx-background-color:transparent;-fx-text-box-border: transparent;");

		send.setTextFill(style.getText1());
		send.setFill(style.getAccent());
		setEffect(new DropShadow(8, Color.gray(0, .2)));
	}

}
