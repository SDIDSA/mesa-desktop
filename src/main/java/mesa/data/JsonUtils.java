package mesa.data;

import org.json.JSONObject;

public class JsonUtils {
	public static JSONObject make(String... strings) {
		if (strings.length % 2 == 1) {
			throw new IllegalArgumentException("must be called with an even number of args");
		}

		JSONObject obj = new JSONObject();

		for (int i = 0; i < strings.length; i += 2) {
			obj.put(strings[i], strings[i + 1]);
		}

		return obj;
	}
}
