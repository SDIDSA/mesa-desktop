package mesa.app.component.input;

import javafx.beans.binding.Bindings;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.combo.ComboInput;
import mesa.gui.controls.input.combo.item.MonthItem;
import mesa.gui.controls.input.combo.item.TextItem;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.window.Window;

public class DateInputField extends InputField implements Localized {
	private Window window;
	
	private ComboInput month;
	private ComboInput day;
	private ComboInput year;

	public DateInputField(Window window, String key, double width) {
		super(window, key, width);
		this.window = window;

		Font font = new Font(16);

		month = new ComboInput(window, font, "birth_month");
		day = new ComboInput(window, font, "birth_day");
		year = new ComboInput(window, font, "birth_year");

		for (int i = 1; i <= 31; i++) {
			day.addItem(new TextItem(window, (i < 10 ? "0" : "") + i));
		}
		for (int i = 2017; i >= 1960; i--) {
			year.addItem(new TextItem(window, Integer.toString(i)));
		}

		value.bind(Bindings
				.when(
						day.valueProperty().isNotEmpty().and(
						month.valueProperty().isNotEmpty()).and(
						year.valueProperty().isNotEmpty()))
				.then(
						day.valueProperty().concat('/').concat(
						month.valueProperty()).concat('/').concat(
						year.valueProperty())).otherwise(""));

		addInputs(month, day, year);

		applyStyle(window.getStyl());
		applyLocale(window.getLocale());
	}

	@Override
	public void applyLocale(Locale locale) {
		for (int i = 1; i <= 12; i++) {
			month.addItem(new MonthItem(window, i));
		}
	}

	@Override
	public void setValue(String string) {
		String[] parts = string.split("/");
		day.setValue(parts[0]);
		month.setValue(parts[1]);
		year.setValue(parts[2]);
	}

}
