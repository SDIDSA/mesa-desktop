package mesa.gui.controls.label;

import java.util.ArrayList;

import javafx.scene.text.Text;
import mesa.gui.controls.Font;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.window.Window;

public class Label extends Text implements Localized {
	private Window window;

	private TextTransform transform = TextTransform.NONE;

	private String key;

	private ArrayList<String> params = new ArrayList<>();

	public Label(Window window, String key, Font font) {
		super();
		this.window = window;
		this.key = key;
		setFont(font);
		applyLocale(window.getLocale());
	}

	public Label(Window window, String key) {
		this(window, key, Font.DEFAULT);
	}

	public void setFont(Font font) {
		setFont(font.getFont());
	}

	public void setTransform(TextTransform transform) {
		this.transform = transform;
		setText(transform.apply(getText()));
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		applyLocale(window.getLocale());
	}

	public void addParam(int i, String param) {
		if (i >= params.size()) {
			params.add(param);
		} else {
			params.set(i, param);
		}
		applyLocale(window.getLocale());
	}

	@Override
	public void applyLocale(Locale locale) {
		if (key != null && !key.isEmpty()) {
			String val = locale.get(key);
			for (int i = 0; i < params.size(); i++) {
				String param = params.get(i);
				param = (param.charAt(0) == '&' && param.length() > 1) ? locale.get(param.substring(1)) : param;
				val = val.replace("{$" + i + "}", param);
			}
			setText(transform.apply(val));
		} else {
			setText("");
		}
	}
}
