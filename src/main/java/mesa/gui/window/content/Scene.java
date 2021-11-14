package mesa.gui.window.content;

import javafx.scene.Parent;
import javafx.scene.paint.Color;

public class Scene extends javafx.scene.Scene{
	public Scene(Parent root, double width, double height) {
		super(root, width, height);
		
		setFill(Color.TRANSPARENT);
	}
}
