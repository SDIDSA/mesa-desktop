package mesa.emojis;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import mesa.gui.file.FileUtils;

public class Emojis {
	private static ArrayList<Emoji> emojiList;
	private static ArrayList<EmojiGroup> groups;
	private static Random random = new Random();
	
	private Emojis() {

	}

	public static void init() {
		emojiList = new ArrayList<>();
		groups = new ArrayList<>();

		String[] emojiDataLines = FileUtils.readFile("/emojis.txt").split("\n");

		EmojiGroup categ = null;
		EmojiSubgroup subCateg = null;
		for (String dataLine : emojiDataLines) {
			if (!dataLine.isBlank() && dataLine.indexOf('#') != 0) {
				String value = Emoji.prepareValue(dataLine.split(";")[0].trim(), " ");

				String name = dataLine.substring(dataLine.indexOf("#")).trim();
				name = name.substring(name.indexOf(" ") + 1).trim();
				name = name.substring(name.indexOf(" ") + 1).trim();
				name = name.substring(name.indexOf(" ") + 1).trim();

				Emoji found = null;
				
				URL url = Emojis.class.getResource("/images/emojis/72x72/" + Emoji.readableValue(value, '-') + ".png");
				
				if(url != null) {
					found = new Emoji(url, value);
					emojiList.add(found);
				}

				if (found != null) {
					found.setName(name);
					if (categ != null) {
						categ.addEmoji(found);
					}
					if (subCateg != null) {
						subCateg.addEmoji(found);
					}
				}
			} else if (dataLine.contains(" group: ")) {
				categ = new EmojiGroup(dataLine.split(" group: ")[1].trim());
				groups.add(categ);
			} else if (dataLine.contains(" subgroup: ")) {
				subCateg = new EmojiSubgroup(dataLine.split(" subgroup: ")[1].trim());
				if (categ != null) {
					categ.addSubgroup(subCateg);
				}
			}

		}

		emojiList.removeIf(em -> em.getName() == null);
		groups.removeIf(group -> group.getEmojis().isEmpty());
		
		Collections.sort(emojiList);
	}

	public static Emoji getRandomEmoji() {
		return emojiList.get(random.nextInt(emojiList.size()));
	}

	public static Emoji getRandomEmoji(int index) {
		EmojiGroup group = groups.get(index);
		return group.get(random.nextInt(group.size()));
	}

	public static Emoji getRandomEmoji(int index, int subIndex) {
		EmojiGroup group = groups.get(index);
		EmojiSubgroup subgroup = group.getSubgroup(subIndex);
		return subgroup.get(random.nextInt(subgroup.size()));
	}
	
	public static Emoji getEmoji(String value) {
		for(Emoji emoji: emojiList) {
			if(emoji.getValue().equals(value)) {
				return emoji;
			}
		}
		
		return null;
	}

	public static List<EmojiIndex> find(String txt) {
		ArrayList<EmojiIndex> res = new ArrayList<>();

		for (Emoji map : emojiList) {
			int index = txt.indexOf(map.getValue());
			while (index != -1) {
				res.add(new EmojiIndex(index, map.getValue()));
				txt = txt.replaceFirst(map.getValue(), replace(map.getValue().length()));
				index = txt.indexOf(map.getValue());
			}
		}

		Collections.sort(res);
		return res;
	}

	private static String replace(int size) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < size; i++) {
			sb.append(" ");
		}

		return sb.toString();
	}
}
