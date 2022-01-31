package mesa.emojis;

import java.util.ArrayList;
import java.util.List;

public class EmojiGroup {
	private String name;
	private ArrayList<EmojiSubgroup> subgroups;
	private ArrayList<Emoji> emojis;
	
	public EmojiGroup(String name) {
		this.name = name;
		subgroups = new ArrayList<>();
		emojis = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<Emoji> getEmojis() {
		return emojis;
	}
	
	public List<EmojiSubgroup> getSubgroups() {
		return subgroups;
	}
	
	public void addEmoji(Emoji emoji) {
		emojis.add(emoji);
	}
	
	public void addSubgroup(EmojiSubgroup subgroup) {
		subgroups.add(subgroup);
	}
	
	public int size() {
		return emojis.size();
	}
	
	public Emoji get(int index) {
		return emojis.get(index);
	}
	
	public EmojiSubgroup getSubgroup(int index) {
		return subgroups.get(index);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
