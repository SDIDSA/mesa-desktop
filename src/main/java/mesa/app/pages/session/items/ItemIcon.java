package mesa.app.pages.session.items;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import mesa.app.pages.session.SessionPage;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ItemIcon extends StackPane implements Styleable {
	private static final double SIZE = 48;
	private static final double HOV_RAD = 30;

	private Timeline doRad;
	private Timeline undoRad;

	private Rectangle focusEffect;

	protected SessionPage session;
	protected boolean selected = false;

	protected boolean canUnhover = true;

	protected StackPane content;

	public ItemIcon(SessionPage session) {
		this.session = session;
		setMinSize(SIZE, SIZE);
		setMaxSize(SIZE, SIZE);

		content = new StackPane();
		content.setMinSize(SIZE, SIZE);
		content.setMaxSize(SIZE, SIZE);

		setCursor(Cursor.HAND);
		
		int focusAdd = 8;

		focusEffect = new Rectangle(SIZE + focusAdd, SIZE + focusAdd);
		focusEffect.setStrokeType(StrokeType.INSIDE);
		focusEffect.setFill(Color.TRANSPARENT);
		focusEffect.setStrokeWidth(1);
		focusEffect.setArcHeight(SIZE + focusAdd);
		focusEffect.setArcWidth(SIZE + focusAdd);
		focusEffect.setVisible(false);

		Rectangle clip = new Rectangle(SIZE, SIZE);
		clip.setArcHeight(SIZE);
		clip.setArcWidth(SIZE);

		doRad = new Timeline(new KeyFrame(Duration.seconds(.1),
				new KeyValue(clip.arcHeightProperty(), HOV_RAD, Interpolator.EASE_BOTH),
				new KeyValue(clip.arcWidthProperty(), HOV_RAD, Interpolator.EASE_BOTH),
				
				new KeyValue(focusEffect.arcHeightProperty(), HOV_RAD + focusAdd, Interpolator.EASE_BOTH),
				new KeyValue(focusEffect.arcWidthProperty(), HOV_RAD + focusAdd, Interpolator.EASE_BOTH)
			));

		undoRad = new Timeline(
				new KeyFrame(Duration.seconds(.1),
						new KeyValue(clip.arcHeightProperty(), SIZE, Interpolator.EASE_BOTH),
						new KeyValue(clip.arcWidthProperty(), SIZE, Interpolator.EASE_BOTH),
						
						new KeyValue(focusEffect.arcHeightProperty(), SIZE + focusAdd, Interpolator.EASE_BOTH),
						new KeyValue(focusEffect.arcWidthProperty(), SIZE + focusAdd, Interpolator.EASE_BOTH)
					));

		addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
			if (!selected) {
				hover();
			}
		});

		addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
			if (!selected) {
				unhover();
			}
		});

		content.setClip(clip);
		getChildren().addAll(focusEffect, content);
	}

	public void focus() {
		focusEffect.setVisible(true);
	}

	public void unfocus() {
		focusEffect.setVisible(false);
	}

	public void setCanUnhover(boolean canUnhover) {
		this.canUnhover = canUnhover;
	}

	public void hover() {
		undoRad.stop();
		doRad.playFromStart();
	}

	public void unhover() {
		if (canUnhover) {
			doRad.stop();
			undoRad.playFromStart();
		}
	}

	public void select() {
		selected = true;
	}

	public void unselect() {
		selected = false;
		doRad.stop();
		undoRad.playFromStart();
	}

	public boolean isSelected() {
		return selected;
	}

	@Override
	public void applyStyle(Style style) {
		focusEffect.setStroke(style.getTextLink());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
