package mesa;

import javafx.application.Application;
import mesa.app.Mesa;

public class Launcher {
	public static void main(String[] args) {
		Application.launch(Mesa.class, args);
		// parseCssTheme("dark");
	}

//	public static void parseCssTheme(String theme) {
//		String content = FileUtils.readFile("/themes/" + theme + ".txt");
//
//		for (String preLine : content.split("\n")) {
//			String[] line = preLine.trim().split(":");
//			if (line.length != 2) {
//				continue;
//			}
//			String key = line[0].replace("-", " ").trim().replace(" ", "_");
//			String value = line[1].replace(";", "").trim().replace("var(--saturation-factor, 1)*", "")
//					.replaceAll("\\bcalc\\(\\b([0-9]+\\.?[0-9]+%)\\)", "$1");
//
//			String truekey = CaseUtils.toCamelCase(key, true, '_');
//			String falseKey = CaseUtils.toCamelCase(key, false, '_');
//
//			try {
//				Color c = Color.web(value);
//
//				System.out.println(
//						"\tpublic Color get" + truekey + "() {\n\t\treturn colors.get(\"" + falseKey + "\");\n\t}\n\n");
//			} catch (Exception x) {
//			}
//		}
//	}
}
