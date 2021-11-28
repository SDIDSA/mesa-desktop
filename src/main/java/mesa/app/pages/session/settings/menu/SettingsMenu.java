package mesa.app.pages.session.settings.menu;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.user_settings.MyAccount;
import mesa.app.utils.Colors;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class SettingsMenu extends VBox implements Styleable {
	private Settings settings;
	private ArrayList<Rectangle> separators;
	
	public SettingsMenu(Settings settings) {
		this.settings = settings;
		setPadding(new Insets(60, 6, 60, 20));
		setMinWidth(218);
		setAlignment(Pos.TOP_CENTER);
		
		separators = new ArrayList<>();
		
		Section userSettings = new Section(settings, "user_settings", true);
		userSettings.addItem(new SectionItem(settings, "my_account", MyAccount.class));
		userSettings.addItem(new SectionItem(settings, "user_profile"));
		userSettings.addItem(new SectionItem(settings, "privacy_and_safety"));
		userSettings.addItem(new SectionItem(settings, "authorized_apps"));
		userSettings.addItem(new SectionItem(settings, "connections"));
		
		Section appSettings = new Section(settings, "app_settings");
		appSettings.addItem(new SectionItem(settings, "appearance"));
		appSettings.addItem(new SectionItem(settings, "accessibility"));
		appSettings.addItem(new SectionItem(settings, "voice_and_video"));
		appSettings.addItem(new SectionItem(settings, "text_and_images"));
		appSettings.addItem(new SectionItem(settings, "notifications"));
		appSettings.addItem(new SectionItem(settings, "shortcuts"));
		appSettings.addItem(new SectionItem(settings, "language"));
		SectionItem os = new SectionItem(settings, "os_settings");
		os.getLab().addParam(0, settings.getWindow().getOsName());
		appSettings.addItem(os);
		appSettings.addItem(new SectionItem(settings, "advanced"));
		
		Section logoutSection = new Section(settings);
		SectionItem logout = new SectionItem(settings, "log_out", () -> settings.getSession().logoutPrompt());
		logout.setTextFill(Colors.Error);
		logoutSection.addItem(logout);
		
		addSection(userSettings);
		separate();
		addSection(appSettings);
		separate();
		addSection(logoutSection);
		separate();
		
		applyStyle(settings.getWindow().getStyl());
	}
	
	public void addSection(Section section) {
		getChildren().add(section);
	}
	
	public void separate() {
		Rectangle sep = new Rectangle(172, 1);
		
		getChildren().addAll(new FixedVSpace(10), sep, new FixedVSpace(8));
		
		styleSeparator(settings.getWindow().getStyl(), sep);
	}
	
	private void styleSeparator(Style style, Rectangle sep) {
		sep.setFill(style.getBackAccent());
	}

	@Override
	public void applyStyle(Style style) {
		separators.forEach(sep -> styleSeparator(style, sep));
	}
}
