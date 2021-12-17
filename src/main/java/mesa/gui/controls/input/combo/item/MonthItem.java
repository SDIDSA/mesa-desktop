package mesa.gui.controls.input.combo.item;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mesa.gui.window.Window;

public class MonthItem extends KeyedTextItem {
	private StringProperty value;

	public MonthItem(Window window, int month) {
		super(window, "month_" + month);

		value = new SimpleStringProperty((month < 10 ? "0" : "") + month);
	}

	@Override
	public StringProperty getValue() {
		return value;
	}
}
