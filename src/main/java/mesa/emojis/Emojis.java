package mesa.emojis;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import mesa.gui.file.FileUtils;

public class Emojis {
	private static ArrayList<Emoji> emojiList;
	private static ArrayList<Category> categories;
	private static Random random = new Random();

	private Emojis() {

	}

	public static void init() {
		emojiList = new ArrayList<>();
		categories = new ArrayList<>();

		File folder = new File(Emojis.class.getResource("/images/emojis/72x72").getFile());

		for (File file : folder.listFiles()) {
			emojiList.add(new Emoji(file));
		}

		String[] emojiDataLines = FileUtils.readFile("/emojis.txt").split("\n");

		Category categ = null;
		for (String dataLine : emojiDataLines) {
			if (!dataLine.isBlank() && dataLine.indexOf('#') != 0) {
				String value = Emoji.prepareValue(dataLine.split(";")[0].trim(), " ");

				String name = dataLine.substring(dataLine.indexOf("#")).trim();
				name = name.substring(name.indexOf(" ") + 1).trim();
				name = name.substring(name.indexOf(" ") + 1).trim();
				name = name.substring(name.indexOf(" ") + 1).trim();

				Emoji found = null;
				for (Emoji emo : emojiList) {
					if (emo.getValue().equals(value)) {
						found = emo;
						break;
					}
				}

				if (found != null && categ != null) {
					found.setName(name);
					categ.addEmoji(found);
				}
			} else if (dataLine.contains(" group:")) {
				categ = new Category(dataLine.split(" group:")[1].trim());
				categories.add(categ);
			}

		}

		emojiList.removeIf(em -> em.getName() == null);
		categories.removeIf(category -> category.getEmojies().isEmpty());
	}

	public static Emoji getRandomEmoji() {
		return emojiList.get(random.nextInt(emojiList.size()));
	}

	public static Emoji getRandomEmoji(int category) {
		return categories.get(category).getEmojies()
				.get(random.nextInt(categories.get(category).getEmojies().size()));
	}
}
