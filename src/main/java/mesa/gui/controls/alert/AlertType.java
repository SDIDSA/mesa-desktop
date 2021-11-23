package mesa.gui.controls.alert;

import java.util.Arrays;
import java.util.List;

public enum AlertType {
	DEFAULT(ButtonType.CANCEL, ButtonType.LOGOUT),
	LOGOUT(ButtonType.CANCEL, ButtonType.LOGOUT);
	
	private List<ButtonType> buttons;
	
	private AlertType(ButtonType...buttonTypes) {
		buttons = Arrays.asList(buttonTypes);
	}
	
	public List<ButtonType> getButtons() {
		return buttons;
	}
}
