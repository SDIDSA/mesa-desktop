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
	private AppBarButton info;
	private ColorIcon icon;

	public AppBar(Window window, MoveResizeHelper helper) {
		setPadding(new Insets(0, 5, 0, 10));
		setMinHeight(23);
		setAlignment(Pos.CENTER);

		icon = new ColorIcon("mesa", 10);
		icon.setMouseTransparent(true);

		HBox buttons = new HBox(4);
		buttons.setAlignment(Pos.CENTER);

		AppBarButton minimize = new AppBarButton(window, "minimize");
		minimize.setAction(() -> window.setIconified(true));

		AppBarButton maxRest = new AppBarButton(window, "maximize");
		maxRest.setAction(window::maxRestore);

		AppBarButton exit = new AppBarButton(window, "close");
		exit.setAction(window::close);

		info = new AppBarButton(window, "info");
		HBox.setMargin(info, new Insets(0, 8, 0, 0));
		
		buttons.getChildren().addAll(info, minimize, maxRest, exit);

		getChildren().addAll(icon, new ExpandingHSpace(), buttons);

		helper.addOnTile(() -> maxRest.setIcon("restore"));

		helper.addOnUnTile(() -> maxRest.setIcon("maximize"));
		
		applyStyle(window.getStyl());
	}

	public void setOnInfo(Runnable action) {
		info.setAction(action);
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
