package mesa.app.pages.session.types.server.center;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import mesa.app.pages.session.SessionPage;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class NoTextChannel extends VBox implements Styleable {

	public NoTextChannel(SessionPage session) {
		setAlignment(Pos.CENTER);
		
		applyStyle(session.getWindow().getStyl());
	}
	
	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBackgroundTertiary()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
