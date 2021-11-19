package mesa.app.pages.session.settings.content.userSettings;

import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.SettingsContent;
import mesa.data.User;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class MyAccount extends SettingsContent implements Styleable {

	private Label title;
	
	public MyAccount(Settings settings) {
		super(settings);
		Window window = settings.getWindow();
		
		title = new Label(window, "my_account", header);
		
		User user = settings.getUser();
		
		UnverifiedEmail unverifiedEmail = new UnverifiedEmail(settings);
		
		getChildren().addAll(title, new FixedVSpace(20));
		
		if(!user.isEmailConfirmed()) {
			getChildren().add(unverifiedEmail);
		}
		
		user.emailConfirmedProperty().addListener((obs, ov, nv)-> {
			if(nv && getChildren().contains(unverifiedEmail)) {
				getChildren().remove(unverifiedEmail);
			}else if(!nv && !getChildren().contains(unverifiedEmail)) {
				getChildren().add(2, unverifiedEmail);
			}
		});
		
		getChildren().addAll(new ProfileOverview(settings));
		
		applyStyle(window.getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		title.setFill(style.getText1());
	}

}
