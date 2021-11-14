package mesa.gui.controls.input.combo.item;

import javafx.scene.text.Text;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.combo.ComboMenuItem;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class TextItem extends ComboMenuItem implements Styleable {
	private Text lab;
	
	public TextItem(Window window, String value) {
		super(window);
		
		lab = new Text(value);
		lab.setFont(new Font(16).getFont());
		
		getChildren().add(lab);
		
		applyStyle(window.getStyl());
	}

	@Override
	public String getDisplay() {
		return lab.getText();
	}

	@Override
	public String getValue() {
		return lab.getText();
	}
	
	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);
		lab.setFill(style.getText1());
		
	}
	
}
