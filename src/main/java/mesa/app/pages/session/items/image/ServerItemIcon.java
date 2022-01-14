package mesa.app.pages.session.items.image;

import javafx.beans.property.ObjectProperty;
import javafx.scene.shape.Rectangle;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.items.ItemIcon;
import mesa.gui.controls.image.Icon;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ServerItemIcon extends ItemIcon {

	private Rectangle back;

	public ServerItemIcon(SessionPage session, String iconPath) {
		super(session);
		
		back = new Rectangle(48,48);
		
		Icon icon = new Icon(session.getWindow(), iconPath, 48);
		
		content.getChildren().addAll(back, icon);
		
		applyStyle(session.getWindow().getStyl());
	}
	
	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		
		back.setFill(style.getBackgroundPrimary());
	}
	
	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
