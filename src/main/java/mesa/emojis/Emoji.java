package mesa.emojis;

import java.io.File;

import javafx.scene.image.Image;
import mesa.gui.controls.image.ImageProxy;

public class Emoji implements Comparable<Emoji>{
	private String value;
	private String name;
	private File file;

	public Emoji(File file) {
		this.file = file;
		this.value = prepareValue(file.getName().replace(".png", ""), "-");
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public Image getImage(double size) {
		return ImageProxy.load(file.getAbsolutePath(), size, true);
	}

	public static String prepareValue(String name, String delimiter) {
		String[] parts = name.split(delimiter);

		StringBuilder sb = new StringBuilder();

		for (String part : parts) {
			sb.appendCodePoint(Integer.parseInt(part, 16));
		}

		return sb.toString();
	}
	
	public static String addi() {
		StringBuilder sb = new StringBuilder();

		sb.appendCodePoint(Integer.parseInt("FE0F", 16));

		return sb.toString();
	}

	@Override
	public String toString() {
		return "Emoji [value=" + value + ", name=" + name + ", file=" + file + "]";
	}

	@Override
	public int compareTo(Emoji o) {
		return Integer.compare(o.value.length(), value.length());
	}

}
