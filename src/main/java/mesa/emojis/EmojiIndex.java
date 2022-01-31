package mesa.emojis;

public class EmojiIndex implements Comparable<EmojiIndex> {
	private int start;
	private String value;

	public EmojiIndex(int start, String value) {
		this.start = start;
		this.value = value;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(EmojiIndex o) {
		return Integer.compare(start, o.start);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EmojiIndex emojiIndex) {
			return start == emojiIndex.start && value.equals(emojiIndex.value);
		}
		return false;
	}

	@Override
	public String toString() {
		return value + " @ " + start;
	}

}
