package mesa.gui.controls.input;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import mesa.emojis.Emoji;

public class InputEmojiElement extends ImageView implements InputElement {
	private Emoji emoji;
	private double size;
	
	private ColorAdjust ca;
	public InputEmojiElement(Emoji emoji, double size) {
		super(emoji.getImage((int) (size * 1.1)));
		this.size = size;
		this.emoji = emoji;
		
		ca = new ColorAdjust(0, -1, 0, 0);
	}
	
	
	@Override
	public String getValue() {
		return emoji.getValue();
	}

	@Override
	public void select(RichInput input) {
		setEffect(ca);
	}

	@Override
	public void deselect(RichInput input) {
		setEffect(null);
	}
	
	@Override
	public double getBaselineOffset() {
		return size * .9;
	}


	@Override
	public double width() {
		return getImage().getWidth();
	}

}
