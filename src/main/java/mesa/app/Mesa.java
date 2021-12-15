package mesa.app;

import io.socket.client.IO;
import io.socket.client.Socket;
import javafx.application.Application;
import javafx.stage.Stage;
import mesa.api.API;
import mesa.app.pages.login.LoginPage;
import mesa.gui.locale.Locale;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class Mesa extends Application {

	@Override
	public void start(Stage dismiss) throws Exception {
		System.setProperty("prism.lcdtext", "false");
		Window window = new Window(this, Style.DARK, Locale.FR_FR);
		window.setTitle("mesa");
		window.setOnShown(e -> window.loadPage(LoginPage.class, window::centerOnScreen));
		window.show();
		
		Socket socket = IO.socket(API.BASE);
		socket.connect();
		
		window.putMainSocket(socket);
		
		window.addOnClose(socket::close);
	}

}
