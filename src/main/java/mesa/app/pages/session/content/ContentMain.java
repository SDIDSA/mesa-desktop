package mesa.app.pages.session.content;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ContentMain extends VBox {
	private StackPane top;
	private StackPane center;
	
	public ContentMain() {
		top = new StackPane();
		center = new StackPane();
		
		center.setAlignment(Pos.CENTER);
		VBox.setVgrow(center, Priority.ALWAYS);
	
		getChildren().addAll(top, center);
	}
	
	public void setTop(Node node) {
		top.getChildren().setAll(node);
	}
	
	public void setCenter(Node node) {
		center.getChildren().setAll(node);
	}
}
