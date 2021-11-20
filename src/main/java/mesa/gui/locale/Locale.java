package mesa.gui.locale;

import java.util.HashMap;

import org.json.JSONObject;

import mesa.gui.exception.EntryNotFountException;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.file.FileUtils;

public class Locale {
	public static final Locale EN_US = new Locale("en_US");
	
	private String name;
	private HashMap<String, String> values;
	
	private Locale(String name) {
		this.name = name;
		values = new HashMap<>();
		String file = FileUtils.readFile("/locales/" + name + ".json");
		JSONObject obj = new JSONObject(file);
		
		for(String key:obj.keySet()) {
			values.put(key, obj.getString(key));
		}
	}
	
	public String get(String key) {
		String found = values.get(key);
		
		if(found == null) {
			ErrorHandler.handle(new EntryNotFountException(), "get value of [" + key + "] for locale [" + name + "]");
			found = key;
		}
		
		return found;
	}
}
