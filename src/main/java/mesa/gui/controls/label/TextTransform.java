package mesa.gui.controls.label;

import java.util.function.Function;

public abstract class TextTransform implements Function<String, String> {

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
				if (!res.isEmpty()) {
					res.append(' ');
				}
				res.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
			}

			return res.toString();
		}
	};

	public static final TextTransform CAPITALIZE_PHRASE = new TextTransform() {
		@Override
		public String apply(String param) {
			StringBuilder res = new StringBuilder();

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
			boolean found = false;
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < param.length(); i++) {
				if (i >= param.length() - 4) {
					found = true;
				}
				sb.append(found ? param.charAt(i) : '*');
			}

			return sb.toString();
		}
	};

	public abstract String apply(String param);
}
