package mesa.data;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.text.CaseUtils;
import org.json.JSONObject;

import mesa.gui.exception.ErrorHandler;

public class Bean {
	protected void init(JSONObject obj) {
		obj.keySet().forEach(key -> {
			if (obj.isNull(key)) {
				return;
			}
			String value = obj.getString(key);
			String setter = setterName(key);

			try {
				getClass().getMethod(setter, String.class).invoke(this, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException x) {
				ErrorHandler.handle(x, "parse user json");
			}
		});
	}

	private static String setterName(String fieldName) {
		return "set" + toCamelCaseMethod(fieldName);
	}

	private static String toCamelCaseMethod(String name) {
		return CaseUtils.toCamelCase(name, true, '_');
	}
}
