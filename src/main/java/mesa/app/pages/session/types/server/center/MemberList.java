package mesa.app.pages.session.types.server.center;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.StackPane;
import mesa.app.pages.session.SessionPage;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MemberList extends StackPane implements Styleable {
	public MemberList(SessionPage session) {
		
		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBackgroundSecondary()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
