package mesa.emojis;

import java.net.URL;

import javafx.scene.image.Image;
import mesa.gui.controls.image.ImageProxy;

public class Emoji implements Comparable<Emoji>{
	private String value;
	private String name;
	private URL url;

	public Emoji(URL url, String value) {
		this.url = url;
		this.value = value;
	}
	
	public URL getUrl() {
		return url;
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
		return ImageProxy.load(url, size);
	}

	public static String prepareValue(String name, String delimiter) {
		String[] parts = name.split(delimiter);

		StringBuilder sb = new StringBuilder();

		for (String part : parts) {
			sb.appendCodePoint(Integer.parseInt(part, 16));
		}

		return sb.toString();
	}

	public static String readableValue(String value, char delimiter) {
		StringBuilder sb = new StringBuilder();

		value.codePoints().forEach(codePoint -> sb.append(Integer.toHexString(codePoint)).append(" "));

		return sb.toString().trim().replace(' ', delimiter);
	}
	
	public static String addi() {
		StringBuilder sb = new StringBuilder();

		sb.appendCodePoint(Integer.parseInt("FE0F", 16));

		return sb.toString();
	}

	@Override
	public String toString() {
		return "Emoji [value=" + value + ", name=" + name + ", url=" + url + "]";
	}

	@Override
	public int compareTo(Emoji o) {
		return Integer.compare(o.value.length(), value.length());
	}

}
