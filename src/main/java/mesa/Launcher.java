package mesa;

import java.net.URISyntaxException;

import javafx.application.Application;
import mesa.app.Mesa;

public class Launcher {
	public static void main(String[] args) throws URISyntaxException {
		Application.launch(Mesa.class, args);
	}
}
