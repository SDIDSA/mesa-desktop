package mesa.gui.controls;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import mesa.gui.exception.ErrorHandler;

public class Font {
	public static final String DEFAULT_FAMILY = "Ubuntu";//Ubuntu
	public static final String DEFAULT_FAMILY_MEDIUM = DEFAULT_FAMILY + " medium";
	public static final FontWeight DEFAULT_WEIGHT = FontWeight.NORMAL;
	public static final FontPosture DEFAULT_POSTURE = FontPosture.REGULAR;
	public static final double DEFAULT_SIZE = 14;

	public static final Font DEFAULT = new Font();

	private String family;
	private double size;
	private FontWeight weight;
	private FontPosture posture;

	public Font(String family, double size, FontWeight weight, FontPosture posture) {
		this.family = family;
		this.size = size;
		this.weight = weight;
		this.posture = posture;
	}

	public Font(String family, double size, FontWeight weight) {
		this(family, size, weight, DEFAULT_POSTURE);
	}

	public Font(String family, double size) {
		this(family, size, DEFAULT_WEIGHT, DEFAULT_POSTURE);
	}

	public Font(String family) {
		this(family, DEFAULT_SIZE, DEFAULT_WEIGHT, DEFAULT_POSTURE);
	}

	public Font(double size) {
		this(DEFAULT_FAMILY, size, DEFAULT_WEIGHT, DEFAULT_POSTURE);
	}

	public Font(FontWeight weight) {
		this(DEFAULT_FAMILY, DEFAULT_SIZE, weight, DEFAULT_POSTURE);
	}

	public Font(int size, FontWeight weight) {
		this(DEFAULT_FAMILY, size, weight, DEFAULT_POSTURE);
	}

	public Font() {
		this(DEFAULT_FAMILY, DEFAULT_SIZE, DEFAULT_WEIGHT, DEFAULT_POSTURE);
	}

	public Font(double size, FontPosture posture) {
		this(DEFAULT_FAMILY, size, DEFAULT_WEIGHT, posture);
	}

	public Font setFamily(String family) {
		this.family = family;
		return this;
	}

	public Font setSize(double size) {
		this.size = size;
		return this;
	}

	public Font setWeight(FontWeight weight) {
		this.weight = weight;
		return this;
	}

	public Font setPosture(FontPosture posture) {
		this.posture = posture;
		return this;
	}

	public javafx.scene.text.Font getFont() {
		return javafx.scene.text.Font.font(family, weight, posture, size);
	}

	static {
		init();
	}

	public static void init() {
		loadFont(DEFAULT_FAMILY);
	}
	
	private static void loadFont(String name) {
		try {
			File parent = new File(URLDecoder.decode(Font.class
					.getResource("/fonts/" + name + "/" + name + "-Regular.ttf").getFile(),
					"utf-8")).getParentFile();
			for (File font : parent.listFiles()) {
				javafx.scene.text.Font.loadFont(new FileInputStream(font), 14);
			}
		} catch (Exception x) {
			ErrorHandler.handle(x, "load font [" + name + "]");
		}
	}
}
