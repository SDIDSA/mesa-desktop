package mesa.gui.controls.input;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import mesa.emojis.EmojiIndex;
import mesa.emojis.Emojis;
import mesa.gui.controls.Font;

public interface RichText {

	public static void applyText(TextFlow flow, String txt, Font defaultFont) {

		List<EmojiIndex> indexes = Emojis.find(txt);

		flow.getChildren().clear();

		if (indexes.isEmpty()) {
			addText(flow, txt, defaultFont);
		} else {
			int p = 0;
			for (EmojiIndex index : indexes) {
				String t = txt.substring(p, index.getStart());
				addText(flow, t, defaultFont);
				p += t.length();

				flow.getChildren()
						.add(new InputEmojiElement(Emojis.getEmoji(index.getValue()), defaultFont.getSize()));

				p += index.getValue().length();
			}
			addText(flow, txt.substring(p), defaultFont);
		}
	}

	private static void addText(TextFlow flow, String text, Font defaultFont) {
		List<StyleRange> styles = StyleRange.getStyles(text);
		for (int i = 0; i < text.length(); i++) {
			String t = Character.toString(text.charAt(i));
			if (!t.isEmpty()) {
				InputTextElement td = new InputTextElement(t);
				Font font = checkForStyle(styles, i, td, defaultFont);
				td.setFont(font.getFont());
				flow.getChildren().add(td);
			}
		}
	}
	
	private static Font checkForStyle(List<StyleRange> styles, int i, InputTextElement td, Font defaultFont) {
		StyleRange res = null;
		for(StyleRange sr : styles) {
			if (sr.inRange(i) || sr.isBorder(i)) {
				if (sr.inRange(i)) {
					res = sr;
				} else if (sr.isBorder(i)) {
					td.setOpacity(.5);
				}
				break;
			}
		}
		return applyStyle(defaultFont, res);
	}
	
	private static Font applyStyle(Font font, StyleRange style) {
		Font res = font;
		if (style != null && style.italic) {
			res = res.copy().setPosture(FontPosture.ITALIC);
		}
		
		if(style != null && style.bold) {
			res = res.copy().setWeight(FontWeight.BOLD);
		}
		
		return res;
	}
	

	public static class StyleRange {
		int start;
		int end;
		int count;
		boolean bold;
		boolean italic;

		public StyleRange(int start, int end, int count, boolean bold, boolean italic) {
			this.start = start;
			this.end = end;
			this.bold = bold;
			this.italic = italic;
			this.count = count;
		}

		private boolean inRange(int ind) {
			return ind >= start + count && ind <= end - count;
		}

		private boolean outRange(int ind) {
			return ind >= start && ind <= end;
		}

		private boolean isBorder(int ind) {
			return outRange(ind) && !inRange(ind);
		}

		@Override
		public String toString() {
			return "StyleRange [start=" + start + ", end=" + end + ", bold=" + bold + ", italic=" + italic + "]";
		}

		public static List<StyleRange> getStyles(String text) {
			ArrayList<StyleRange> res = new ArrayList<>();
			int start = -1;
			int count = 0;
			int maxCount = 0;
			boolean closed = false;
			boolean cut = false;
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c == '*') {
					if (start == -1) {
						start = i;
						count = 1;
						cut = false;
					} else {
						if (count > maxCount) {
							maxCount = count;
						}
						if (cut) {
							closed = true;
							count -= 1;
						} else {
							count += 1;
						}
						if (count == 0) {
							res.add(new StyleRange(start, i, maxCount, maxCount >= 2, maxCount == 1 || maxCount == 3));
							start = -1;
							count = 0;
							maxCount = 0;
							closed = false;
						}
					}
				} else {
					if (!closed) {
						cut = true;
					} else {
						maxCount -= count;
						res.add(new StyleRange(start + count, i - 1, maxCount, maxCount >= 2,
								maxCount == 1 || maxCount == 3));
						start = -1;
						count = 0;
						maxCount = 0;
						closed = false;
					}
				}
			}
			if (start != -1) {
				maxCount -= count;
				if (maxCount > 0)
					res.add(new StyleRange(start + count, text.length() - 1, maxCount, maxCount >= 2,
							maxCount == 1 || maxCount == 3));
			}
			return res;
		}
	}
}
