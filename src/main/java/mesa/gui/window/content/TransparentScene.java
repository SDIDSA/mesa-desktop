package mesa.gui.window.content;

import javafx.scene.Parent;
import javafx.scene.paint.Color;

public class TransparentScene extends javafx.scene.Scene {
	public TransparentScene(Parent root, double width, double height) {
		super(root, width, height);

		focusOwnerProperty().addListener((obs, ov, nv) -> {
//			System.out.println(nv.getClass().getSimpleName());
		});
		
		setFill(Color.TRANSPARENT);
	}
}
