package mesa.app.pages.session;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.content.OverlayItem;
import mesa.app.pages.session.content.create_server.CreateServer;
import mesa.app.pages.session.items.BarItem;
import mesa.app.pages.session.items.ColorBarItem;
import mesa.app.pages.session.types.home.Home;
import mesa.app.utils.Colors;
import mesa.gui.NodeUtils;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ServerBar extends VBox implements Styleable {
	private ArrayList<Rectangle> seps;

	public ServerBar(SessionPage session) {
		super(8);
		setAlignment(Pos.TOP_CENTER);
		setPadding(new Insets(8, 0, 0, 0));
		setMinWidth(72);

		NodeUtils.nestedFocus(this);
		
		seps = new ArrayList<>();
		
		Home home = new Home(session);
		CreateServer createServer = new CreateServer(session);
		
		addContent(home);
		separate();
		addOverlay(createServer);
		
		addItem(new ColorBarItem(session, Colors.GREEN, "discover", "compass", 20));

		applyStyle(session.getWindow().getStyl());
	}

	public void addContent(Content content) {
		addItem(content.getItem());
	}

	public void addOverlay(OverlayItem overlay) {
		addItem(overlay.getItem());
	}

	public void addItem(BarItem item) {
		getChildren().add(item);
	}

	public void separate() {
		Rectangle sep = new Rectangle(32, 2);
		seps.add(sep);

		getChildren().add(sep);
	}

	@Override
	public void applyStyle(Style style) {
		seps.forEach(sep -> sep.setFill(style.getBackgroundModifierAccent()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
