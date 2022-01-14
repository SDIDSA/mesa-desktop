package mesa.gui.controls.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import mesa.gui.exception.ErrorHandler;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;

public class ImageProxy {
	private static HashMap<String, Image> cache = new HashMap<>();
	static {
		File cacheDir = new File("cache");
		if(!cacheDir.exists() || !cacheDir.isDirectory()) {
			cacheDir.mkdir();
		}
	}
	
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

			if (found.getHeight() > size || found.getWidth() > size) {
				found = resize(found, size);
			}
			cache.put(size + "_" + path, found);
		}

		return found;
	}
	
	public static void asyncLoad(String path, double size, Consumer<Image> onLoad) {
		new Thread() {
			@Override
			public void run() {
				Image found = cache.get(size + "_" + path);
				if(found == null) {
					found = lookInCache(path);
					if(found == null) {
						found = new Image(path);
						saveInCache(path, found);
					}
					if (found.getHeight() != size) {
						found = resize(found, size);
					}
					cache.put(size + "_" + path, found);
				}
				onLoad.accept(found);
			}
		}.start();
	}

	public static Image load(String name, double size) {
		return load(name, size, false);
	}

	private static Image resize(Image img, double size) {
		double ratio = img.getWidth() / img.getHeight();
		double nh = 0;
		double nw = 0;
		if(ratio >= 1) {
			nh = size;
			nw = size * ratio;
		}else {
			nw = size;
			nh = size / ratio;
		}
		
		Resizer resizer = DefaultResizerFactory.getInstance().getResizer(
				new Dimension((int) img.getWidth(), (int) img.getHeight()), new Dimension((int) nw, (int) nh));

		BufferedImage o = SwingFXUtils.fromFXImage(img, null);

		BufferedImage scaledImage = new FixedSizeThumbnailMaker((int) nw, (int) nh, true, true).resizer(resizer)
				.make(o);

		return SwingFXUtils.toFXImage(scaledImage, null);
	}
	
	private static Image lookInCache(String path) {
		String name = path.substring(path.lastIndexOf("/") + 1);
		File file = new File("cache/" + name);
		if(file.exists()) {
			try {
				return new Image(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				ErrorHandler.handle(e, "load image from cache");
				return null;
			}
		}else {
			return null;
		}
	}
	
	private static void saveInCache(String path, Image image) {
		String name = path.substring(path.lastIndexOf("/") + 1);
		String extension = name.substring(name.lastIndexOf(".") + 1);
		
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), extension, new File("cache/" + name));
		} catch (IOException e) {
			ErrorHandler.handle(e, "save image in cache");
		}
	}
}
