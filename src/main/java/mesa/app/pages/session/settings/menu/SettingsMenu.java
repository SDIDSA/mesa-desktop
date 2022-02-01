package mesa.app.pages.session.settings.menu;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class SettingsMenu extends VBox implements Styleable {
	private Paint separatorFill;
	private ArrayList<Rectangle> separators;

	public SettingsMenu(Settings settings) {
		setPadding(new Insets(60, 6, 60, 20));
		setMinWidth(218);
		setAlignment(Pos.TOP_CENTER);

		separators = new ArrayList<>();

		applyStyle(settings.getWindow().getStyl());
	}

	public void addSection(Section section) {
		getChildren().add(section);
	}

	public void separate() {
		Rectangle sep = new Rectangle(172, 1);
		separators.add(sep);
		getChildren().addAll(new FixedVSpace(10), sep, new FixedVSpace(8));
		
		styleSeparator(sep);
	}

	private void styleSeparator(Rectangle sep) {
		if (separatorFill != null)
			sep.setFill(separatorFill);
	}

	@Override
	public void applyStyle(Style style) {
		separatorFill = style.getBackgroundModifierAccent();

		separators.forEach(this::styleSeparator);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
