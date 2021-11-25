package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import mesa.data.CountryCode;
import mesa.gui.controls.Font;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class CountryCodeItem extends HBox implements Styleable {

	private Label name;
	private Text code;

	public CountryCodeItem(CountryCode data) {
		setAlignment(Pos.CENTER);
		setPadding(new Insets(8, 10, 8, 10));
		setMinHeight(34);
		setMinWidth(200);

		name = new Label(data.getName());
		name.setFont(new Font(12).getFont());
		name.setTextOverrun(OverrunStyle.ELLIPSIS);

		code = new Text(data.getCode());
		code.setFont(new Font(13, FontWeight.BOLD).getFont());

		setCursor(Cursor.HAND);
		
		getChildren().addAll(name, new ExpandingHSpace(), code);
	}

	@Override
	public void applyStyle(Style style) {
		name.setTextFill(style.getInteractiveNormal());
		code.setFill(style.getText1());

		backgroundProperty().bind(Bindings.when(hoverProperty()).then(Backgrounds.make(
				style == Style.DARK ? Color.web("#20222599"): null
				, 3.0))
				.otherwise(Background.EMPTY));
	}

}
