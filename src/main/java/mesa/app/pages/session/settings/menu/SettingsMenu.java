package mesa.app.pages.session.settings.menu;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.userSettings.MyAccount;
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
		
		separators = new ArrayList<Rectangle>();
		
		Section user_settings = new Section(settings, "user_settings", true);
		user_settings.addItem(new SectionItem(settings, "my_account", MyAccount.class));
		user_settings.addItem(new SectionItem(settings, "user_profile"));
		user_settings.addItem(new SectionItem(settings, "privacy_and_safety"));
		user_settings.addItem(new SectionItem(settings, "authorized_apps"));
		user_settings.addItem(new SectionItem(settings, "connections"));
		
		Section app_settings = new Section(settings, "app_settings");
		app_settings.addItem(new SectionItem(settings, "appearance"));
		app_settings.addItem(new SectionItem(settings, "accessibility"));
		app_settings.addItem(new SectionItem(settings, "voice_and_video"));
		app_settings.addItem(new SectionItem(settings, "text_and_images"));
		app_settings.addItem(new SectionItem(settings, "notifications"));
		app_settings.addItem(new SectionItem(settings, "shortcuts"));
		app_settings.addItem(new SectionItem(settings, "language"));
		SectionItem os = new SectionItem(settings, "os_settings");
		os.getLab().addParam(0, settings.getWindow().getOsName());
		app_settings.addItem(os);
		app_settings.addItem(new SectionItem(settings, "advanced"));
		
		addSection(user_settings);
		separate();
		addSection(app_settings);
		
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

	@Override
	public void applyStyle(Style style) {
		separators.forEach(sep -> styleSeparator(style, sep));
	}
	
	private void styleSeparator(Style style, Rectangle sep) {
		sep.setFill(style.getBackAccent());
	}
}
