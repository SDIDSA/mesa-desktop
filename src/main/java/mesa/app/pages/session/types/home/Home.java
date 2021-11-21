package mesa.app.pages.session.types.home;

import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.content.UserBar;
import mesa.app.pages.session.items.ColorBarItem;
import mesa.app.utils.Colors;
import mesa.gui.controls.Font;
import mesa.gui.controls.popup.context.ContextMenu;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class Home extends Content implements Styleable {
	private ColorBarItem item;

	public Home(SessionPage session) {
		super(session);

		item = new ColorBarItem(session, null, "home", "logo", 20);

		setItem(item);

		HomeSideTop sideTop = new HomeSideTop(session);
		UserBar sideBot = new UserBar(session);

		sideTop.setAction(() -> {
			//TODO search for users, servers... etc
		});

		getSide().setTop(sideTop);
		getSide().setBottom(sideBot);
		
		ContextMenu menu = new ContextMenu(session.getWindow());
		menu.addMenuItem("Mark As Read");
		menu.separate();
		menu.addMenuItem("Mute Server");
		menu.separate();
		menu.addMenuItem("Hide Muted Channels");
		menu.addMenuItem("Notification Settings");
		menu.addMenuItem("Privacy Settings");
		menu.addMenuItem("Edit Server Profile");
		menu.separate();
		menu.addMenuItem("Leave Server", Colors.Error);
		menu.separate();
		menu.addMenuItem("Copy ID");
		
		Text menuTest = new Text("Context Menu Test");
		
		menu.install(menuTest);
		
		menuTest.setFont(new Font(16).getFont());
		menuTest.setFill(Color.WHITE);
		menuTest.setCursor(Cursor.HAND);
		
		getSide().setCenter(menuTest);


		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		item.setColor(style.getAccent());
	}

}
