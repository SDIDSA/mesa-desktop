package mesa.emojis;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private String name;
	private ArrayList<Emoji> emojies;
	
	public Category(String name) {
		this.name = name;
		emojies = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<Emoji> getEmojies() {
		return emojies;
	}
	
	public void addEmoji(Emoji emoji) {
		emojies.add(emoji);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
