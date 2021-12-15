package mesa.app.pages.session.settings.menu;

import mesa.app.pages.session.settings.Settings;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;

public class Section extends VBox implements Styleable {

	private Label title;
	private VBox items;

	public Section(Settings settings, String titleKey, boolean first) {

		items = new VBox(2);
		NodeUtils.nestedFocus(items);

		if (titleKey != null) {
			title = new Label(settings.getWindow(), titleKey, new Font(12, FontWeight.BOLD));
			title.setTransform(TextTransform.UPPERCASE);
			StackPane titCont = new StackPane(title);
			titCont.setAlignment(Pos.CENTER_LEFT);
			titCont.setPadding(new Insets(first ? 0 : 6, 0, 6, 10));
			titCont.setMaxWidth(192);
			titCont.setMinWidth(0);
			getChildren().add(titCont);
		}

		getChildren().add(items);

		applyStyle(settings.getWindow().getStyl());
	}
	
	public Section(Settings settings) {
		this(settings, null);
	}

	public void addItem(SectionItem item) {
		items.getChildren().add(item);
	}

	public Section(Settings settings, String titleKey) {
		this(settings, titleKey, false);
	}

	@Override
	public void applyStyle(Style style) {
		if (title != null)
			title.setFill(style.getChannelsDefault());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
