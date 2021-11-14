package mesa.app.pages.session.settings.content;

import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Font;

public class SettingsContent extends VBox {
	protected Font header = new Font(Font.DEFAULT_FAMILY, 20, FontWeight.BOLD);
	
	public SettingsContent(Settings settings) {
	}
}
