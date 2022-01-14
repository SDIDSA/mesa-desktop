package mesa.data.bean;

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
			Object value = obj.get(key);
			set(key, value);
		});
	}

	public void set(String key, Object value) {
		String setter = setterName(key);

		try {
			getClass().getMethod(setter, value.getClass()).invoke(this, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException x) {
			ErrorHandler.handle(x, "parse " + key + " of type " + value.getClass().getSimpleName());
			x.printStackTrace();
		}
	}

	private static String setterName(String fieldName) {
		return "set" + toCamelCaseMethod(fieldName);
	}

	private static String toCamelCaseMethod(String name) {
		return CaseUtils.toCamelCase(name, true, '_');
	}
}
