package mesa.app;

import javafx.application.Application;
import javafx.stage.Stage;
import mesa.app.pages.login.LoginPage;
import mesa.gui.locale.Locale;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class Mesa extends Application {
	
	@Override
	public void start(Stage dismiss) throws Exception {
		System.setProperty("prism.lcdtext", "false");
		Window window = new Window(Style.DARK, Locale.EN_US);
		window.setTitle("mesa");
		window.setOnShown(e-> {
			window.loadPage(LoginPage.class);
		});
		window.show();
	}
	
}
