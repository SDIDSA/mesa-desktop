package mesa.app.pages.session.settings;

import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.settings.content.user_settings.MyAccount;
import mesa.app.pages.session.settings.menu.Section;
import mesa.app.pages.session.settings.menu.SectionItem;
import mesa.app.utils.Colors;

public class GlobalSettings extends Settings {

	public GlobalSettings(SessionPage session) {
		super(session);
		
		Section userSettings = new Section(this, "user_settings", true);
		userSettings.addItem(new SectionItem(this, "my_account", MyAccount.class));
		userSettings.addItem(new SectionItem(this, "user_profile"));
		userSettings.addItem(new SectionItem(this, "privacy_and_safety"));
		userSettings.addItem(new SectionItem(this, "authorized_apps"));
		userSettings.addItem(new SectionItem(this, "connections"));
		
		Section appSettings = new Section(this, "app_settings");
		appSettings.addItem(new SectionItem(this, "appearance"));
		appSettings.addItem(new SectionItem(this, "accessibility"));
		appSettings.addItem(new SectionItem(this, "voice_and_video"));
		appSettings.addItem(new SectionItem(this, "text_and_images"));
		appSettings.addItem(new SectionItem(this, "notifications"));
		appSettings.addItem(new SectionItem(this, "shortcuts"));
		appSettings.addItem(new SectionItem(this, "language"));
		SectionItem os = new SectionItem(this, "os_settings");
		os.getLab().addParam(0, session.getWindow().getOsName());
		appSettings.addItem(os);
		appSettings.addItem(new SectionItem(this, "advanced"));
		
		Section logoutSection = new Section(this);
		SectionItem logout = new SectionItem(this, "log_out", session::logoutPrompt);
		logout.setTextFill(Colors.Error);
		logoutSection.addItem(logout);
		
		sideBar.addSection(userSettings);
		sideBar.separate();
		sideBar.addSection(appSettings);
		sideBar.separate();
		sideBar.addSection(logoutSection);
		sideBar.separate();
	}

}
