package mesa.gui.controls.scroll;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Scrollable extends StackPane {
	private StackPane contentCont;
	
	public Scrollable() {
		setAlignment(Pos.TOP_LEFT);
		setMinHeight(0);
		
		contentCont = new StackPane();
		StackPane scrollBarCont = new StackPane();
		scrollBarCont.setPadding(new Insets(5));
		scrollBarCont.setAlignment(Pos.CENTER_RIGHT);
		
		ScrollBar sb = new ScrollBar(6, 0);
		scrollBarCont.setPickOnBounds(false);
		scrollBarCont.getChildren().add(sb);
		sb.install(this, contentCont);
		
		sb.opacityProperty().bind(Bindings.when(sb.pressedProperty().or(sb.hoverProperty())).then(1).otherwise(Bindings.when(hoverProperty()).then(.4).otherwise(0)));
		
		addEventFilter(ScrollEvent.ANY, e-> {
			sb.scrollByPixels(e.getDeltaY(), contentCont.getHeight());
		});
		
		contentCont.translateYProperty().bind(sb.positionProperty().multiply(contentCont.heightProperty().subtract(heightProperty())).multiply(-1));
		
		Rectangle clip = new Rectangle();
		clip.widthProperty().bind(widthProperty());
		clip.heightProperty().bind(heightProperty());
		clip.yProperty().bind(contentCont.translateYProperty().negate());
		
		contentCont.setClip(clip);
		getChildren().addAll(contentCont, scrollBarCont);
	}
	
	public void setContent(Parent content) {
		contentCont.getChildren().setAll(content);
	}
	
	public ReadOnlyDoubleProperty contentHeightProperty() {
		return contentCont.heightProperty();
	}
}
