package mesa;

import javafx.application.Application;
import mesa.app.Mesa;
import mesa.emojis.Emojis;

public class Launcher {
	public static void main(String[] args) {
		Emojis.init();
		Application.launch(Mesa.class, args);
	}
}
