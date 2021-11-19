package mesa.app.pages.session.settings.menu;

import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;

public class Section extends VBox implements Styleable {
	
	private Label title;
	private VBox items;

	public Section(Settings settings, String titleKey, boolean first) {
		title = new Label(settings.getWindow(), titleKey, new Font(12, FontWeight.BOLD));
		title.setTransform(TextTransform.UPPERCASE);
		StackPane titCont = new StackPane(title);
		titCont.setAlignment(Pos.CENTER_LEFT);
		titCont.setPadding(new Insets(first ? 0 : 6, 10, 6, 10));

		items = new VBox(2);
		
		getChildren().addAll(titCont, items);

		applyStyle(settings.getWindow().getStyl());
	}
	
	public void addItem(SectionItem item) {
		items.getChildren().add(item);
	}

	public Section(Settings settings, String titleKey) {
		this(settings, titleKey, false);
	}

	@Override
	public void applyStyle(Style style) {
		title.setFill(style.getChannelDefault());
	}

}
