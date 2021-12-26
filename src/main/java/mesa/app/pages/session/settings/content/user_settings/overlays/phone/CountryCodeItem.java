package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import java.util.function.Consumer;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import mesa.data.bean.CountryCode;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class CountryCodeItem extends HBox implements Styleable {

	private Label name;
	private Text code;

	public CountryCodeItem(CountryCode data, Consumer<CountryCode> onAction) {
		setAlignment(Pos.CENTER);
		setPadding(new Insets(8, 10, 8, 10));
		setMinHeight(34);
		setMinWidth(198);

		setFocusTraversable(true);
		
		name = new Label(data.getName());
		name.setFont(new Font(12).getFont());
		name.setTextOverrun(OverrunStyle.ELLIPSIS);

		code = new Text(data.getCode());
		code.setFont(new Font(13, FontWeight.BOLD).getFont());

		setCursor(Cursor.HAND);
		
		setOnMouseClicked(e-> onAction.accept(data));
		setOnKeyPressed(e-> {
			if(e.getCode().equals(KeyCode.SPACE))
				onAction.accept(data);
		});

		getChildren().addAll(name, new ExpandingHSpace(), code);
	}

	@Override
	public void applyStyle(Style style) {
		name.setTextFill(style.getCountryNameItemText());
		code.setFill(style.getCountryCodeItemText());
		
		NodeUtils.focusBorder(this, style.getTextLink());

		backgroundProperty().bind(Bindings.when(hoverProperty()).then(Backgrounds.make(style.getCountryCodeItemHover(), 3.0))
				.otherwise(Background.EMPTY));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
