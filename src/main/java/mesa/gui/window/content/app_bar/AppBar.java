package mesa.gui.window.content.app_bar;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;
import mesa.gui.window.helpers.MoveResizeHelper;

public class AppBar extends HBox implements Styleable {
	private ColorIcon icon;

	public AppBar(Window window, MoveResizeHelper helper) {
		setPadding(new Insets(0, 8, 0, 10));
		setMinHeight(28);
		setAlignment(Pos.CENTER);

		icon = new ColorIcon("mesa", 10);
		icon.setMouseTransparent(true);

		HBox buttons = new HBox(4);
		buttons.setAlignment(Pos.CENTER);

		AppBarButton minimize = new AppBarButton(window, "minimize");
		minimize.setOnMouseClicked(e -> window.setIconified(true));

		AppBarButton maxRest = new AppBarButton(window, "maximize");
		maxRest.setOnMouseClicked(e -> window.maxRestore());

		AppBarButton exit = new AppBarButton(window, "close");
		exit.setOnMouseClicked(e -> window.close());

		buttons.getChildren().addAll(minimize, maxRest, exit);

		getChildren().addAll(icon, new ExpandingHSpace(), buttons);

		helper.addOnTile(() -> maxRest.setIcon("restore"));

		helper.addOnUnTile(() -> maxRest.setIcon("maximize"));
		
		applyStyle(window.getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		icon.setFill(style.getInteractiveNormal());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
