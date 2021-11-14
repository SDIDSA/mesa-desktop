package mesa.gui.file;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import mesa.gui.exception.ErrorHandler;

public class FileUtils {
	public static String readFile(String path) {
		StringBuilder sb = new StringBuilder();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(path)));
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
		} finally {
			try {
				br.close();
			} catch (Exception x) {
				ErrorHandler.handle(x, "closing stream after reading file " + path);
			}
		}

		return sb.toString();
	}
}
