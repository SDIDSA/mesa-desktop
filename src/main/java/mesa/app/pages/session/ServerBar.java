package mesa.app.pages.session;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.items.BarItem;
import mesa.app.pages.session.items.ColorBarItem;
import mesa.app.pages.session.types.home.Home;
import mesa.app.utils.Colors;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ServerBar extends VBox implements Styleable {
	private ArrayList<Rectangle> seps;

	public ServerBar(SessionPage session) {
		super(8);
		setAlignment(Pos.TOP_CENTER);
		setPadding(new Insets(4, 0, 0, 0));
		setMinWidth(72);

		seps = new ArrayList<Rectangle>();
		Home home = new Home(session);
		addContent(home);
		separate();
		addItem(new ColorBarItem(session, Colors.GREEN, "add_server", "plus", 16));
		addItem(new ColorBarItem(session, Colors.GREEN, "discover", "compass", 20));

		applyStyle(session.getWindow().getStyl());
	}

	public void addContent(Content content) {
		addItem(content.getItem());
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
		seps.forEach(sep -> {
			sep.setFill(style.getBack2());
		});
	}
}
