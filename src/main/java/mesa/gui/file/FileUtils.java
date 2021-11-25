package mesa.gui.file;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import mesa.data.CountryCode;
import mesa.gui.exception.ErrorHandler;

public class FileUtils {
	private FileUtils() {

	}

	public static String readFile(String path) {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(path)))) {
			String line;
			boolean first = true;
			while ((line = br.readLine()) != null) {
				if (first) {
					first = false;
				} else {
					sb.append("\n");
				}
				sb.append(line);
			}
		} catch (Exception x) {
			ErrorHandler.handle(x, "reading file " + path);
		}

		return sb.toString();
	}

	private static List<CountryCode> countryCodes;

	public static List<CountryCode> readCountryCodes() {
		if (countryCodes == null) {
			countryCodes = new ArrayList<>();
			JSONObject obj = new JSONObject(readFile("/countries.json"));
			for (Object o : obj.getJSONArray("countryCodes")) {
				JSONObject countryObj = (JSONObject) o;
				countryCodes.add(
						new CountryCode(countryObj.getString("country_name"), countryObj.getString("dialling_code")));
			}
		}
		return countryCodes;
	}
}
