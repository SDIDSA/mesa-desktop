package mesa.gui.controls.alert;

import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class AlertButton extends Button implements Styleable {
	private ButtonType type;
	
	public AlertButton(Alert alert, ButtonType type) {
		super(alert.getWindow(), type.getKey(), 3, 24, 38);
		this.type = type;
		
		setFont(new Font(14, FontWeight.BOLD));
		
		if(!type.isFilled()) {
			setUlOnHover(true);
		}
		
		
		applyStyle(alert.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		setTextFill(style.getText1());
		
		if(type.isFilled()) {
			if(type.getFill() == null) {
				setFill(style.getAccent());
			}else {
				setFill(type.getFill());
			}
		}else {
			setFill(Color.TRANSPARENT);
		}
	}

}
