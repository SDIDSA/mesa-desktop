package mesa.gui.controls.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;

public class ImageProxy {
	private static HashMap<String, Image> cache = new HashMap<>();

	private ImageProxy() {
		
	}
	
	public static Image load(String name, double size, boolean fullPath) {
		String path = fullPath ? name
				: new StringBuilder().append("/images/icons/").append(name).append('_').append((int) size).append(".png")
						.toString();

		Image found = cache.get(size + "_" + path);

		if (found == null) {
			if (fullPath) {
				found = new Image(path);
			} else {
				found = new Image(ImageProxy.class.getResourceAsStream(path));
			}

			if (found.getHeight() != size) {
				found = resize(found, size);
			}
			cache.put(size + "_" + path, found);
		}

		return found;
	}

	public static Image load(String name, double size) {
		return load(name, size, false);
	}

	private static Image resize(Image img, double size) {
		double ratio = img.getWidth() / img.getHeight();
		double nh = size;
		double nw = size * ratio;
		
		Resizer resizer = DefaultResizerFactory.getInstance().getResizer(
				new Dimension((int) img.getWidth(), (int) img.getHeight()), new Dimension((int) nw, (int) nh));

		BufferedImage o = SwingFXUtils.fromFXImage(img, null);

		BufferedImage scaledImage = new FixedSizeThumbnailMaker((int) nw, (int) nh, true, true).resizer(resizer)
				.make(o);

		return SwingFXUtils.toFXImage(scaledImage, null);
	}
}
