package mesa.app.pages.session.settings.content.userSettings;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Link;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.FixedHSpace;
import mesa.gui.style.Style;

public class HideableOverviewField extends OverviewField {

	private Text value;

	private BooleanProperty hidden;

	public HideableOverviewField(Settings settings, String key, StringProperty full, TextTransform hide) {
		super(settings, key);

		hidden = new SimpleBooleanProperty(true);

		this.value = new Text();
		this.value.setFont(new Font(16).getFont());

		Link reveal = new Link(settings.getWindow(), "overview_reveal");
		reveal.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		reveal.setAction(() -> {
			hidden.set(!hidden.get());
		});
		
		value.textProperty().bind(Bindings.createStringBinding(()-> {
			return hidden.get() ? hide.apply(full.get()) : full.get();
		}, hidden, full));

		addToValue(this.value, new FixedHSpace(4), reveal);

		applyStyle(settings.getWindow().getStyl());
	}
	
	@Override
	public void applyStyle(Style style) {
		if (value == null) {
			return;
		}
		super.applyStyle(style);
		value.setFill(style.getText1());
	}
}
