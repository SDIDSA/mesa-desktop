package mesa.app.pages.session.types.server.center;

import java.util.HashMap;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import mesa.emojis.Emoji;
import mesa.emojis.Emojis;
import mesa.gui.controls.SplineInterpolator;

public class EmojiInputIcon extends StackPane {
	private ImageView view;

	private Timeline hover;
	private Timeline unhover;

	private double scale = 0;

	public EmojiInputIcon(double size) {
		setAlignment(Pos.TOP_CENTER);

		scale = 32.0 / size;
		
		view = new ImageView();
		view.setFitHeight(size);
		view.setFitWidth(size);
		view.setSmooth(true);

		setPickOnBounds(true);
		setCursor(Cursor.HAND);

		hover = new Timeline(new KeyFrame(Duration.seconds(0.2),
				new KeyValue(view.scaleXProperty(), scale, SplineInterpolator.OVERSHOOT),
				new KeyValue(view.scaleYProperty(), scale, SplineInterpolator.OVERSHOOT)));

		unhover = new Timeline(new KeyFrame(Duration.seconds(0.2),
				new KeyValue(view.scaleXProperty(), 1, SplineInterpolator.OVERSHOOT),
				new KeyValue(view.scaleYProperty(), 1, SplineInterpolator.OVERSHOOT)));

		change();
		apply(size);
		grayScale();

		setOnMouseEntered(e -> {
			change();
			apply(size * scale);

			unhover.stop();
			hover.playFromStart();
		});

		setOnMouseExited(e -> {
			hover.stop();
			unhover.playFromStart();
		});

		unhover.setOnFinished(e -> {
			apply(size);

			grayScale();
		});

		getChildren().setAll(view);
	}

	private Emoji emoji;

	private Random random = new Random();
	private void change() {
		emoji = Emojis.getRandomEmoji(0, random.nextInt(10));
	}

	private void apply(double size) {
		view.setImage(emoji.getImage(size));
	}

	private HashMap<Emoji, Image> grayCache = new HashMap<>();
	
	private void grayScale() {
		Image found = grayCache.get(emoji);
		if(found == null) {
			found = grayScale(view.getImage());
			grayCache.put(emoji, found);
		}
		view.setImage(found);
	}

	private static Image grayScale(Image img) {
		WritableImage res = new WritableImage((int) img.getWidth(), (int) img.getHeight());

		PixelReader pr = img.getPixelReader();
		PixelWriter pw = res.getPixelWriter();
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				pw.setColor(x, y, pr.getColor(x, y).grayscale());
			}
		}
		return res;
	}
}
