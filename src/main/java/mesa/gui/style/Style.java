package mesa.gui.style;

import javafx.scene.paint.Color;

public class Style {
	public static final Style DARK = new Style(
			//Color.web("#f26963").darker(),
			Color.web("#5865f2"),			// accent
			Color.web("#36393f"),			// background 1
			Color.web("#2f3136"),			// background 2
			Color.web("#202225"),			// background 3
			Color.web("#292b2f"),			// background 4
			Color.web("#18191c"), 			// background 5
			
			Color.web("#4f545c29"),			// back modifier hover
			Color.web("#4f545c52"),			// back modifier selected
			Color.web("#4f545c3d"),			// back modifier active
			Color.web("#ffffff0f"),			// back modifier accent
			
			Color.web("#4f545c52"),			// back modifier selected deprecated
			
			Color.WHITE,					// text1
			Color.web("#dcddde"),			// text2
			Color.web("#72767d"),			// text muted
			
			Color.web("#b9bbbe"),			// interactive normal
			Color.web("#dcddde"),			// interactive hover
			Color.web("#fff"),				// interactive active
			
			Color.web("#8e9297"),			// channel default
			
			Color.gray(0, .1),
			Color.gray(0, .3),
			Color.web("#040405")

	);

	private Color accent;

	private Color back1;
	private Color back2;
	private Color back3;
	private Color back4;
	private Color back5;
	
	private Color backHover;
	private Color backSelected;
	private Color backActive;
	private Color backAccent;
	
	private Color backSelectedDeprecated;

	private Color text1;
	private Color text2;
	private Color textMuted;
	
	private Color interactiveNormal;
	private Color interactiveHover;
	private Color interactiveActive;
	
	private Color channelDefault;

	private Color textBack1;
	private Color textBorder1;
	private Color textBorderHover1;

	private Style(Color accent, Color back1, Color back2, Color back3, Color back4, Color back5, Color backHover, Color backSelected, Color backActive, Color backAccent, Color backSelectedDeprecated,
			Color text1,Color text2, Color textMuted, Color interactiveNormal, Color interactiveHover, Color interactiveActive, Color channelDefault, Color textBack1, Color textBorder1, Color textBorderHover1) {
		
		this.accent = accent;
		
		this.back1 = back1;
		this.back2 = back2;
		this.back3 = back3;
		this.back4 = back4;
		this.back5 = back5;

		this.backHover = backHover;
		this.backSelected = backSelected;
		this.backActive = backActive;
		this.backAccent = backAccent;
		
		this.backSelectedDeprecated = backSelectedDeprecated;
		
		this.text1 = text1;
		this.text2 = text2;
		this.textMuted = textMuted;
		
		this.interactiveNormal = interactiveNormal;
		this.interactiveHover = interactiveHover;
		this.interactiveActive = interactiveActive;

		this.channelDefault = channelDefault;

		this.textBack1 = textBack1;
		this.textBorder1 = textBorder1;
		this.textBorderHover1 = textBorderHover1;
	}
	
	public Color getAccent() {
		return accent;
	}

	public Color getBack1() {
		return back1;
	}

	public Color getBack2() {
		return back2;
	}

	public Color getBack3() {
		return back3;
	}

	public Color getBack4() {
		return back4;
	}

	public Color getBack5() {
		return back5;
	}
	
	public Color getBackHover() {
		return backHover;
	}
	
	public Color getBackSelected() {
		return backSelected;
	}
	
	public Color getBackActive() {
		return backActive;
	}
	
	public Color getBackAccent() {
		return backAccent;
	}
	
	public Color getBackSelectedDeprecated() {
		return backSelectedDeprecated;
	}

	public Color getText1() {
		return text1;
	}
	
	public Color getText2() {
		return text2;
	}
	
	public Color getTextMuted() {
		return textMuted;
	}
	
	public Color getInteractiveNormal() {
		return interactiveNormal;
	}
	
	public Color getInteractiveHover() {
		return interactiveHover;
	}
	
	public Color getInteractiveActive() {
		return interactiveActive;
	}
	
	public Color getChannelDefault() {
		return channelDefault;
	}

	public Color getTextBack1() {
		return textBack1;
	}

	public Color getTextBorder1() {
		return textBorder1;
	}

	public Color getTextBorderHover1() {
		return textBorderHover1;
	}
	
	public void setAccent(Color accent) {
		this.accent = accent;
	}
}
