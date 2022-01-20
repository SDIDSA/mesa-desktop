package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import java.util.function.Consumer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import mesa.data.bean.CountryCode;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.AbstractButton;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.input.DeprecatedTextInput;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class PhoneInput extends DeprecatedTextInput {
	private AbstractButton country;
	private Button send;

	private ColorIcon showPop;

	private Text selectedCode;

	private boolean pressed;

	private CountryCodePopup countries;

	private CountryCode selectedCountry;

	private Consumer<String> onChange;

	private Timeline onShown;
	private Timeline onHidden;

	public PhoneInput(Window window) {
		super(window, new Font(16), "phone_number");
		setPadding(new Insets(2, 6, 3, 6));
		
		ignoreFocus(true);
		
		valueProperty().addListener(c -> onChange());

		send = new Button(window, "phone_send", 3.0, 16, 32);
		send.setFont(new Font(13, FontWeight.BOLD));
		setMargin(send, new Insets(6, 6, 6, 0));

		country = new AbstractButton(window, 3.0, 32);
		country.setContentPadding(new Insets(0, 12, 0, 12));
		setMargin(country, new Insets(6, 0, 6, 6));

		selectedCode = new Text("");
		selectedCode.setFont(new Font(14, FontWeight.BOLD).getFont());
		HBox.setMargin(selectedCode, new Insets(0, 8, 0, 0));

		country.prefWidthProperty().bind(Bindings.createDoubleBinding(
				() -> selectedCode.getBoundsInLocal().getWidth() + 40, selectedCode.textProperty()));

		showPop = new ColorIcon("expand", 8);
		showPop.setRotate(-90);

		onShown = new Timeline(new KeyFrame(Duration.seconds(.1), new KeyValue(showPop.rotateProperty(), 0)));
		onHidden = new Timeline(new KeyFrame(Duration.seconds(.1), new KeyValue(showPop.rotateProperty(), -90)));

		country.add(selectedCode, showPop);

		country.setOnMousePressed(e -> pressed = true);

		country.setMouseAction(() -> {
			if (!pressed)
				countries.hide();
			else
				countries.showPop(this);

			pressed = false;
		});

		country.setKeyAction(() -> {
			requestFocus();
			countries.showPop(this);
		});

		addPreField(country);
		addPostField(send);

		applyStyle(window.getStyl().get());
	}

	private void onChange() {
		if (onChange != null)
			onChange.accept(getValue());
	}

	public void setOnChange(Consumer<String> onChange) {
		this.onChange = onChange;
	}

	public void load(Window window) {
		if (countries != null) {
			return;
		}

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
			selectedCountry = code;
			selectedCode.setText(code.getCode());
			countries.hide();
			onChange();
			requestFocus();
		});
	}

	public CountryCode getSelectedCountry() {
		return selectedCountry;
	}

	public void unload() {
		countries = null;
	}

	@Override
	public String getValue() {
		return selectedCode.getText() + super.getValue();
	}

	public void setAction(Consumer<String> action) {
		send.setAction(() -> action.accept(getValue()));
	}

	public void startLoading() {
		send.startLoading();
	}

	public void stopLoading() {
		send.stopLoading();
	}

	@Override
	public void clear() {
		super.clear();
		selectedCode.setText("");
		selectedCountry = null;
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		if(country == null) {
			return;
		}

		country.setFill(style.getSecondaryButtonBack());
		selectedCode.setFill(Color.WHITE);
		showPop.setFill(Color.WHITE);

		send.setTextFill(Color.WHITE);
		send.setFill(style.getAccent());
		setEffect(new DropShadow(8, Color.gray(0, .2)));
	}

}
