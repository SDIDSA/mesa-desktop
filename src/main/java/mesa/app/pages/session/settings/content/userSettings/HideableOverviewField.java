package mesa.app.pages.session.settings.content.userSettings;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
	private StringProperty full;

	public HideableOverviewField(Settings settings, String key, String fullText, TextTransform hide) {
		super(settings, key);

		hidden = new SimpleBooleanProperty(true);
		full = new SimpleStringProperty(fullText);

		this.value = new Text();
		this.value.setFont(new Font(16).getFont());

		Link reveal = new Link(settings.getSession().getWindow(), "overview_reveal");
		reveal.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		reveal.setAction(() -> {
			hidden.set(!hidden.get());
		});
		
		value.textProperty().bind(Bindings.createStringBinding(()-> {
			return hidden.get() ? hide.apply(full.get()) : full.get();
		}, hidden, full));

		addToValue(this.value, new FixedHSpace(4), reveal);

		applyStyle(settings.getSession().getWindow().getStyl());
	}

	public void setFull(String full) {
		this.full.set(full);
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
