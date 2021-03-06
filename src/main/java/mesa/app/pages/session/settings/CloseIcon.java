package mesa.app.pages.session.settings;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
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
		
		icon = new ColorIcon("delete", 18);
		
		getChildren().add(icon);
		
		setOnMouseClicked(e-> settings.getSession().hideSettings(settings));
		
		applyStyle(settings.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		setBorder(Borders.make(style.getTextMuted(), 18, 2));
		backgroundProperty().bind(Bindings.when(hoverProperty()).then(Backgrounds.make(style.getCloseIconActive(), 36)).otherwise(Background.EMPTY));
		
		icon.setFill(style.getInteractiveNormal());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
