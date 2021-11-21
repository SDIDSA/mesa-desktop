package mesa.app.pages.session.settings.content;

import java.util.ArrayList;

import javafx.geometry.Orientation;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Font;
import mesa.gui.controls.space.Separator;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class SettingsContent extends VBox implements Styleable {
	protected Font header = new Font(Font.DEFAULT_FAMILY, 20, FontWeight.BOLD);
	private Settings settings;
	
	private ArrayList<Separator> separators;
	
	public SettingsContent(Settings settings) {
		this.settings = settings;
		
		separators = new ArrayList<>();
	}
	
	public void separate() {
		Separator sep = new Separator(Orientation.HORIZONTAL);
		
		getChildren().add(sep);
		
		styleSeparator(settings.getWindow().getStyl(), sep);
	}
	
	private void styleSeparator(Style style, Separator sep) {
		sep.setFill(style.getBackAccent());
	}
	
	private void styleSeparators(Style style) {
		separators.forEach(sep -> styleSeparator(style, sep));
	}

	@Override
	public void applyStyle(Style style) {
		styleSeparators(style);
	}
}
