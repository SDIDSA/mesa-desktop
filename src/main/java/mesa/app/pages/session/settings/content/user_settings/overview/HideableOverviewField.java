package mesa.app.pages.session.settings.content.user_settings.overview;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.KeyedLink;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.style.Style;

public class HideableOverviewField extends OverviewField {

	private Text value;
	private Label emptyLabel;

	private BooleanProperty hidden;
	private BooleanProperty empty;

	public HideableOverviewField(Settings settings, String key, StringProperty full, TextTransform hide) {
		super(settings, key);

		empty = new SimpleBooleanProperty(false);

		hidden = new SimpleBooleanProperty(true);

		this.value = new Text();
		this.value.setFont(new Font(16).getFont());
		HBox.setMargin(this.value, new Insets(0, 4, 0, 0));

		KeyedLink reveal = new KeyedLink(settings.getWindow(), "overview_reveal", new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		reveal.setAction(() -> hidden.set(!hidden.get()));

		value.textProperty().bind(
				Bindings.createStringBinding(() -> hidden.get() ? hide.apply(full.get()) : full.get(), hidden, full));

		emptyLabel = new Label(settings.getWindow(), "overview_empty", new Font(16));
		emptyLabel.addParam(0, key);
		emptyLabel.setTransform(TextTransform.CAPITALIZE_PHRASE);

		empty.addListener((obs, ov, nv) -> {
			if (nv.booleanValue()) {
				setValue(emptyLabel);
				edit.setKey("overview_add");
			} else {
				setValue(this.value, reveal);
				edit.setKey("overview_edit");
			}
		});

		setValue(this.value, reveal);

		applyStyle(settings.getWindow().getStyl());
	}

	public BooleanProperty emptyProperty() {
		return empty;
	}

	@Override
	public void applyStyle(Style style) {
		if (value == null) {
			return;
		}
		super.applyStyle(style);
		value.setFill(style.getHeaderPrimary());
		emptyLabel.setFill(style.getHeaderPrimary());
	}
}
