package mesa.app.pages.session.settings.content.userSettings;

import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.SettingsContent;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class MyAccount extends SettingsContent implements Styleable {

	private Label title;
	
	public MyAccount(Settings settings) {
		super(settings);
		Window window = settings.getSession().getWindow();
		
		title = new Label(window, "my_account", header);
		
		getChildren().addAll(title, new FixedVSpace(20), new ProfileOverview(settings));
		
		applyStyle(window.getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		title.setFill(style.getText1());
	}

}
