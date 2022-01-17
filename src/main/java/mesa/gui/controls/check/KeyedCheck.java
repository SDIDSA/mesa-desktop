package mesa.gui.controls.check;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.window.Window;

public class KeyedCheck extends HBox {
	private Check check;
	private Label label;
	
	public KeyedCheck(Window window, String key, double size) {
		super(8);
		setAlignment(Pos.CENTER_LEFT);
		
		check = new Check(window, size);
		label = new Label(window, key);
		
		check.setMouseTransparent(true);
		label.setMouseTransparent(true);
		
		setOnMouseClicked(e-> check.flip());
		
		setCursor(Cursor.HAND);
		
		getChildren().addAll(check, label);
	}
	
	public BooleanProperty checkedProperty() {
		return check.checkedProperty();
	}
	
	public void setTextFill(Paint fill) {
		label.setFill(fill);
	}
	
	public void setFont(Font font) {
		label.setFont(font);
	}
	
	public void setKey(String key) {
		label.setKey(key);
	}
}
