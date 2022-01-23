package mesa.gui.controls.popup.context.items;

import javafx.beans.property.BooleanProperty;
import javafx.scene.paint.Color;
import mesa.gui.controls.check.Check;
import mesa.gui.controls.popup.context.ContextMenu;

public class CheckMenuItem extends KeyedMenuItem {

	private Check check;

	public CheckMenuItem(ContextMenu menu, String key, Color fill) {
		super(menu, key, fill);

		check = new Check(menu.getOwner(), 14);

		check.invertedProperty().bind(active);

		check.setMouseTransparent(true);
		
		getChildren().add(check);

		setAction(() -> check.flip());

		setHideOnAction(false);
	}
	
	public void setChecked(boolean checked) {
		check.setChecked(checked);
	}

	public BooleanProperty checkedProperty() {
		return check.checkedProperty();
	}

}
