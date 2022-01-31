package mesa.gui.controls.label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.function.UnaryOperator;

import org.ocpsoft.prettytime.PrettyTime;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;

public abstract class TextTransform implements UnaryOperator<String> {

	private TextTransform() {

	}

	public static final TextTransform NONE = new TextTransform() {
		@Override
		public String apply(String param) {
			return param;
		}
	};

	public static final TextTransform UPPERCASE = new TextTransform() {
		@Override
		public String apply(String param) {
			return param.toUpperCase();
		}
	};

	public static final TextTransform LOWERCASE = new TextTransform() {
		@Override
		public String apply(String param) {
			return param.toLowerCase();
		}
	};

	public static final TextTransform CAPITALIZE = new TextTransform() {
		@Override
		public String apply(String param) {
			StringBuilder res = new StringBuilder();

			for (String word : param.split(" ")) {
				if (res.length() != 0) {
					res.append(' ');
				}
				res.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
			}

			return res.toString();
		}
	};

	public static final TextTransform CAPITALIZE_PHRASE = new TextTransform() {
		@Override
		public String apply(String param) {
			StringBuilder res = new StringBuilder();

			if (param.isEmpty()) {
				return param;
			}

			res.append(Character.toUpperCase(param.charAt(0)));

			res.append(param.substring(1).toLowerCase());

			return res.toString();
		}
	};

	public static final TextTransform HIDE_EMAIL = new TextTransform() {
		@Override
		public String apply(String param) {
			boolean found = false;
			StringBuilder sb = new StringBuilder();
			for (char c : param.toCharArray()) {
				if (c == '@') {
					found = true;
				}
				sb.append(found ? c : '*');
			}
			return sb.toString();
		}
	};

	public static final TextTransform HIDE_PHONE = new TextTransform() {
		@Override
		public String apply(String param) {
			if (param == null) {
				return "";
			}
			StringBuilder sb = new StringBuilder();

			int count = 0;
			for (int i = param.length() - 1; i >= 0; i--) {
				char c = param.charAt(i);
				sb.insert(0, count >= 4 ? "*" : c);
				count += Character.isDigit(c) ? 1 : 0;
			}

			return sb.toString();
		}
	};

	private static final PrettyTime prettyTime = new PrettyTime();
	private static final long TZ_OFFSET = TimeZone.getDefault().getRawOffset() /1000;
	private static final DateTimeFormatter parser = DateTimeFormatter.ofPattern("ddMMyyHHmmss");
	public static final TextTransform FORMAT_TIME = new TextTransform() {
		@Override
		public String apply(String t) {
			try {
				LocalDateTime dt = LocalDateTime.parse(t, parser).plusSeconds(TZ_OFFSET);
				return prettyTime.format(dt);
			} catch (Exception x) {
				return t;
			}
		}
	};

	public static StringBinding transformBinding(StringBinding binding, TextTransform transform) {
		return Bindings.createStringBinding(() -> transform.apply(binding.get()), binding);
	}

	public static StringBinding transformBinding(StringProperty binding, TextTransform transform) {
		return Bindings.createStringBinding(() -> transform.apply(binding.get()), binding);
	}
}
