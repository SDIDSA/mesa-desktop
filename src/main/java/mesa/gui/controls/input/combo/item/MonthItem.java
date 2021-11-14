package mesa.gui.controls.input.combo.item;

import mesa.gui.window.Window;

public class MonthItem extends KeyedTextItem {
	private int month;
	public MonthItem(Window window, int month) {
		super(window, "month_" + month);
		this.month = month;
	}
	
	@Override
	public String getValue() {
		return (month < 10 ? "0":"") + month;
	}
}
