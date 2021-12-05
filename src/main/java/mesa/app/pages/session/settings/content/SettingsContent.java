package mesa.app.pages.session.settings.content;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.gui.controls.Font;
import mesa.gui.controls.space.Separator;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class SettingsContent extends VBox implements Styleable {
	protected Font header = new Font(Font.DEFAULT_FAMILY, 20, FontWeight.BOLD);

	private ArrayList<Separator> separators;

	public SettingsContent() {
		separators = new ArrayList<>();
	}

	public void separate(double margin) {
		Separator sep = new Separator(Orientation.HORIZONTAL);
		setMargin(sep, new Insets(margin, 0, margin, 0));
		separators.add(sep);
		getChildren().add(sep);
	}

	private void styleSeparator(Style style, Separator sep) {
		sep.setFill(style.getBackgroundModifierAccent());
	}

	private void styleSeparators(Style style) {
		separators.forEach(sep -> styleSeparator(style, sep));
	}

	@Override
	public void applyStyle(Style style) {
		styleSeparators(style);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
