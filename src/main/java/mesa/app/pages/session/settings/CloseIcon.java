package mesa.app.pages.session.settings;

import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class CloseIcon extends StackPane implements Styleable {
	private ColorIcon icon;
	
	public CloseIcon(Settings settings) {
		setMinSize(36, 36);
		setMaxSize(36, 36);
		setCursor(Cursor.HAND);
		
		icon = new ColorIcon(settings.getWindow(), "delete", 18);
		
		getChildren().add(icon);
		
		setOnMouseClicked(e-> settings.getSession().hideSettings());
		
		applyStyle(settings.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		setBorder(Borders.make(style.getTextMuted(), 18, 2));
		backgroundProperty().bind(Bindings.when(hoverProperty()).then(Backgrounds.make(style.getBackActive().deriveColor(0, 1, 1, 3), 36)).otherwise(Background.EMPTY));
		
		icon.setFill(style.getText2());
	}
}
