package mesa.app.pages.session.items;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ColorItemIcon extends ItemIcon implements Styleable {

	private Rectangle back;
	private Color to;

	private ColorIcon ic;
	private Timeline doCol;
	private Timeline undoCol;

	public ColorItemIcon(SessionPage session, Color to, String icon, int size) {
		super(session);

		this.to = to;
		back = new Rectangle(48, 48);

		ic = new ColorIcon(icon, size);

		getChildren().addAll(back, ic);

		applyStyle(session.getWindow().getStyl());
	}

	public ColorItemIcon(SessionPage session, Color to, String icon) {
		this(session, to, icon, 16);
	}

	@Override
	public void hover() {
		undoCol.stop();
		doCol.playFromStart();

		super.hover();
	}

	@Override
	public void unhover() {
		if (canUnhover) {
			doCol.stop();
			undoCol.playFromStart();
		}

		super.unhover();
	}

	@Override
	public void unselect() {
		super.unselect();
		doCol.stop();
		undoCol.playFromStart();
	}

	public void setTo(Color to) {
		this.to = to;
		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		if (!selected) {
			back.setFill(style.getBackgroundPrimary());
			ic.setFill(to);
		}

		if (to != null) {
			doCol = new Timeline(
					new KeyFrame(Duration.seconds(.1), new KeyValue(back.fillProperty(), to, Interpolator.EASE_BOTH),
							new KeyValue(ic.fillProperty(), Color.WHITE, Interpolator.EASE_BOTH)));
			undoCol = new Timeline(new KeyFrame(Duration.seconds(.1),
					new KeyValue(back.fillProperty(), style.getBackgroundPrimary(), Interpolator.EASE_BOTH),
					new KeyValue(ic.fillProperty(), to, Interpolator.EASE_BOTH)));
		}
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
