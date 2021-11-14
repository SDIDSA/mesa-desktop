package mesa.gui.window.content.appBar;

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

		icon = new ColorIcon(window, "mesa", 10);
		icon.setMouseTransparent(true);

		HBox buttons = new HBox(4);
		buttons.setAlignment(Pos.CENTER);

		AppBarButton minimize = new AppBarButton(window, "minimize");
		minimize.setOnMouseClicked(e -> {
			window.setIconified(true);
		});

		AppBarButton max_rest = new AppBarButton(window, "maximize");
		max_rest.setOnMouseClicked(e -> {
			window.maxRestore();
		});

		AppBarButton exit = new AppBarButton(window, "close");
		exit.setOnMouseClicked(e -> {
			window.close();
		});

		buttons.getChildren().addAll(minimize, max_rest, exit);

		getChildren().addAll(icon, new ExpandingHSpace(), buttons);

		helper.addOnTile(() -> {
			max_rest.setIcon("restore");
		});

		helper.addOnUnTile(() -> {
			max_rest.setIcon("maximize");
		});
		
		applyStyle(window.getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		icon.setFill(style.getText2());
	}
}
