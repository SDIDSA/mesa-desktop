package mesa.app.pages.session.items;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mesa.app.pages.session.SessionPage;

public class ItemIcon extends StackPane {
	private static final double SIZE = 48;
	private static final double HOV_RAD = 30;
	
	protected SessionPage session;

	private boolean selected = false;
	
	private Timeline do_rad, undo_rad;
	public ItemIcon(SessionPage session) {
		this.session = session;
		setMinSize(SIZE, SIZE);
		setMaxSize(SIZE, SIZE);
		
		setCursor(Cursor.HAND);

		Rectangle clip = new Rectangle(SIZE, SIZE);
		clip.setArcHeight(SIZE);
		clip.setArcWidth(SIZE);

		do_rad = new Timeline(new KeyFrame(Duration.seconds(.1),
				new KeyValue(clip.arcHeightProperty(), HOV_RAD, Interpolator.EASE_BOTH),
				new KeyValue(clip.arcWidthProperty(), HOV_RAD, Interpolator.EASE_BOTH)));
		
		undo_rad = new Timeline(new KeyFrame(Duration.seconds(.1),
				new KeyValue(clip.arcHeightProperty(), SIZE, Interpolator.EASE_BOTH),
				new KeyValue(clip.arcWidthProperty(), SIZE, Interpolator.EASE_BOTH)));

		addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
			if(!selected) {
				undo_rad.stop();
				do_rad.playFromStart();
			}
		});
		
		addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
			if(!selected) {
				do_rad.stop();
				undo_rad.playFromStart();
			}
		});
		
		setClip(clip);
	}
	
	public void select() {
		selected = true;
	}
	
	public void unselect() {
		selected = false;
		do_rad.stop();
		undo_rad.playFromStart();
	}
	
	public boolean isSelected() {
		return selected;
	}
}
