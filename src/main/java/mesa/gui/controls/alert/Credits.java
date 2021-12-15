package mesa.gui.controls.alert;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.Link;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class Credits extends Alert {
	private Label mostIcons;
	private Link icons8;
	
	public Credits(Pane owner, Window window) {
		super(owner, window, AlertType.INFO);
		setHead("About Mesa");
		addLabel("Code is hosted at github ");
		addLink("SDIDSA/mesa-desktop");
		setBodyAction(1, ()-> window.openLink("https://github.com/SDIDSA/mesa-desktop"));
		
		mostIcons = new Label(window, "Most icons on this app are from ", new Font(14));
		icons8 = new Link(window, "icons8", new Font(14));
		icons8.setAction(()-> window.openLink("https://icons8.com"));
		
		TextFlow icons = new TextFlow(mostIcons, icons8);
		
		addToBody(icons);
		
		applyStyle(window.getStyl());
	}
	
	@Override
	public void applyStyle(Style style) {
		mostIcons.setFill(style.getTextNormal());
		
		super.applyStyle(style);
	}
	
	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		if(mostIcons == null) {
			return;
		}
		Styleable.bindStyle(this, style);
	}

}
