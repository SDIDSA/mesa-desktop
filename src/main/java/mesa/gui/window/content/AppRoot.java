package mesa.gui.window.content;

import java.awt.Dimension;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import mesa.app.pages.Page;
import mesa.app.utils.Colors;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.window.Window;
import mesa.gui.window.content.app_bar.AppBar;
import mesa.gui.window.helpers.MoveResizeHelper;
import mesa.gui.window.helpers.TileHint.Tile;

public class AppRoot extends BorderPane {

	private AppPreRoot parent;
	private MoveResizeHelper helper;

	private Paint borderFill;
	private double borderWidth;

	private AppBar bar;

	public AppRoot(Window window, AppPreRoot parent) {
		this.parent = parent;
		DropShadow ds = new DropShadow(15, Color.gray(0, .25));
		setEffect(ds);

		setFocusTraversable(true);
		
		addEventFilter(MouseEvent.MOUSE_PRESSED, e-> requestFocus());

		setBorderFill(Colors.DEFAULT_WINDOW_BORDER, 1);
		setFill(window.getStyl().getBack3());

		helper = new MoveResizeHelper(window, parent, 5);

		addEventFilter(MouseEvent.MOUSE_MOVED, helper::onMove);

		addEventFilter(MouseEvent.MOUSE_PRESSED, helper::onPress);

		addEventFilter(MouseEvent.MOUSE_RELEASED, e -> helper.onRelease());

		setOnMouseDragged(helper::onDrag);

		setOnMouseClicked(helper::onClick);

		parent.paddedProperty().addListener((obs, ov, nv) -> {
			setFill(getBackground().getFills().get(0).getFill());
			setBorderFill(borderFill, borderWidth);
		});

		bar = new AppBar(window, helper);
		setTop(bar);
	}

	public void setFill(Paint fill) {
		setBackground(Backgrounds.make(fill, parent.isPadded() ? 11.0 : 0));
	}

	public void setBorderFill(Paint fill, double width) {
		borderFill = fill;
		borderWidth = width;
		setBorder(Borders.make(fill, parent.isPadded() ? 10.0 : 1, parent.isPadded() ? width : 0));
	}

	public void setContent(Page page) {
		setCenter(page);
	}

	public void applyTile(Tile tile) {
		helper.applyTile(tile);
	}

	public void unTile() {
		helper.unTile();
	}

	public boolean isTiled() {
		return helper.isTiled();
	}

	public void setMinSize(Dimension d) {
		helper.setMinSize(d);
	}

	public AppBar getAppBar() {
		return bar;
	}

}
