package mesa.gui.controls.image;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SearchClearIcon extends StackPane {
	private ColorIcon search;
	private ColorIcon clear;
	
	private Timeline showSearch;
	private Timeline showClear;
	
	public SearchClearIcon(double size) {
		search = new ColorIcon("search", size);
		clear = new ColorIcon("clear", size);
		
		setMaxSize(search.getMaxWidth(), search.getMaxHeight());
		
		getChildren().addAll(clear, search);
		
		showSearch = animate(clear, search);
		showClear = animate(search, clear);
		
		search();
	}
	
	public void search() {
		setCursor(Cursor.DEFAULT);
		showClear.stop();
		showSearch.playFromStart();
	}
	
	public void clear() {
		setCursor(Cursor.HAND);
		showSearch.stop();
		showClear.playFromStart();
	}
	
	private Timeline animate(ColorIcon hide, ColorIcon show) {
		return new Timeline(new KeyFrame(Duration.seconds(.1),
				new KeyValue(hide.opacityProperty(), 0, Interpolator.EASE_BOTH),
				new KeyValue(hide.rotateProperty(), 45, Interpolator.EASE_BOTH),
				
				new KeyValue(show.opacityProperty(), 1, Interpolator.EASE_BOTH),
				new KeyValue(show.rotateProperty(), 0, Interpolator.EASE_BOTH)
			));
	}
	
	public void setFill(Color fill) {
		search.setFill(fill);
		clear.setFill(fill);
	}
}
