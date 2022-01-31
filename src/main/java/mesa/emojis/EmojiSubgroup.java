package mesa.emojis;

import java.util.ArrayList;
import java.util.List;

public class EmojiSubgroup {
	private String name;
	private ArrayList<Emoji> emojis;
	
	public EmojiSubgroup(String name) {
		this.name = name;
		emojis = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<Emoji> getEmojis() {
		return emojis;
	}
	
	public void addEmoji(Emoji emoji) {
		emojis.add(emoji);
	}
	
	public int size() {
		return emojis.size();
	}
	
	public Emoji get(int index) {
		return emojis.get(index);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
