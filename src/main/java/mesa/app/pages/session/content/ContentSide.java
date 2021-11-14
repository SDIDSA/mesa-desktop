package mesa.app.pages.session.content;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ContentSide extends VBox {
	private StackPane top;
	private StackPane center;
	private StackPane bottom;
	
	public ContentSide() {
		top = new StackPane();
		center = new StackPane();
		center.setAlignment(Pos.TOP_CENTER);
		bottom = new StackPane();
		
		VBox.setVgrow(center, Priority.ALWAYS);
	
		getChildren().addAll(top, center, bottom);
	}
	
	public void setTop(Node node) {
		top.getChildren().setAll(node);
	}
	
	public void setCenter(Node node) {
		center.getChildren().setAll(node);
	}
	
	public void setBottom(Node node) {
		bottom.getChildren().setAll(node);
	}
}
