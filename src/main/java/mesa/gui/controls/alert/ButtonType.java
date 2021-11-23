package mesa.gui.controls.alert;

import javafx.scene.paint.Color;
import mesa.app.utils.Colors;

public enum ButtonType {
	CLOSE("close"),
	DONE("done"),
	CANCEL("cancel", false),
	LOGOUT("log_out", Colors.Error);
	
	private String key;
	private boolean filled;
	private Color fill;
	private ButtonType(String key, Color fill, boolean filled) {
		this.key = key;
		this.fill = fill;
		this.filled = filled;
	}
	
	private ButtonType(String key) {
		this(key, null, true);
	}
	
	private ButtonType(String key, boolean filled) {
		this(key, null, filled);
	}
	
	private ButtonType(String key, Color fill) {
		this(key, fill, true);
	}
	
	public String getKey() {
		return key;
	}
	
	public boolean isFilled() {
		return filled;
	}
	
	public Color getFill() {
		return fill;
	}
}
