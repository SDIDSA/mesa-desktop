package mesa.app.pages.session.types.server.center;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import mesa.emojis.Emojis;
import mesa.gui.controls.SplineInterpolator;

public class EmojiInputIcon extends StackPane {
	private double size;
	
	private ImageView view;
	
	private ColorAdjust gray;
	private ColorAdjust color;

	private Timeline hover;
	private Timeline unhover;

	private double scale = 1.16;
	public EmojiInputIcon(double size) {
		view = new ImageView();
		
		this.size = (int) (size * scale);
		
		setTranslateY(-1);
		
		gray = new ColorAdjust();
		gray.setSaturation(-.8);
		
		setPickOnBounds(true);
		setCursor(Cursor.HAND);

		double scaleBack = 1.0 / scale;
		
		view.setScaleX(scaleBack);
		view.setScaleY(scaleBack);
		
		hover = new Timeline(new KeyFrame(Duration.seconds(0.2), 
				new KeyValue(view.scaleXProperty(), 1, SplineInterpolator.OVERSHOOT), 
				new KeyValue(view.scaleYProperty(), 1, SplineInterpolator.OVERSHOOT)));

		unhover = new Timeline(new KeyFrame(Duration.seconds(0.2), 
				new KeyValue(view.scaleXProperty(), scaleBack, SplineInterpolator.ANTICIPATE), 
				new KeyValue(view.scaleYProperty(), scaleBack, SplineInterpolator.ANTICIPATE)));

		change();

		setOnMouseEntered(e -> {
			change();

			unhover.stop();
			hover.playFromStart();
		});

		setOnMouseExited(e -> {
			hover.stop();
			unhover.playFromStart();
		});

		effectProperty().bind(Bindings.when(hoverProperty()).then(color).otherwise(gray));
		
		getChildren().setAll(view);
	}

	private void change() {
		view.setImage(Emojis.getRandomEmoji(0).getImage(size));
	}
}
