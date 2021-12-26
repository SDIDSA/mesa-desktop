package mesa.gui.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import mesa.data.bean.CountryCode;
import mesa.gui.controls.image.ImageProxy;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.window.Window;

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

	public static Image selectImage(Window window, int size) {
		try {
			File f = selectFile(window, "Image", "*.png", "*.jpg");
			return ImageProxy.load(f.getAbsolutePath(), size, true);
		} catch (Exception e) {
			return null;
		}
	}

	public static File selectImage(Window window) {
		try {
			return selectFile(window, "Image", "*.png", "*.jpg");
		} catch (Exception e) {
			return null;
		}
	}

	private static File selectFile(Window window, String type, String... extensions) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter(type, extensions));
		return fc.showOpenDialog(window);
	}
}
