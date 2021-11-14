package mesa;

import com.kieferlam.javafxblur.Blur;

import javafx.application.Application;
import mesa.app.Mesa;

public class Launcher {
	public static void main(String[] args) {
		Blur.loadBlurLibrary();
		Application.launch(Mesa.class, args);
	}
}
