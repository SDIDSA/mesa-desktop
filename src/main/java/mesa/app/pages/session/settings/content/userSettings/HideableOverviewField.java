package mesa.app.pages.session.settings.content.userSettings;

import javafx.scene.text.Text;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Link;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.FixedHSpace;
import mesa.gui.style.Style;

public class HideableOverviewField extends OverviewField {

	private boolean hidden = true;
	private Text value;
	private String full;

	public HideableOverviewField(Settings settings, String key, String value, TextTransform hide) {
		super(settings, key);

		this.full = value;

		this.value = new Text(hide.apply(value));
		this.value.setFont(new Font(16).getFont());

		Link reveal = new Link(settings.getSession().getWindow(), "overview_reveal");
		reveal.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		reveal.setAction(() -> {
			if (hidden) {
				reveal.setKey("overview_hide");
				this.value.setText(full);
				hidden = false;
			} else {
				reveal.setKey("overview_reveal");
				this.value.setText(hide.apply(full));
				hidden = true;
			}
		});

		addToValue(this.value, new FixedHSpace(4), reveal);

		applyStyle(settings.getSession().getWindow().getStyl());
	}

	public void setFull(String full) {
		this.full = full;
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
