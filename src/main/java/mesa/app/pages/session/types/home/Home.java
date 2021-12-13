package mesa.app.pages.session.types.home;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
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

	private Text stylyingTest;
	private Text menuTest;

	public Home(SessionPage session) {
		super(session);

		item = new ColorBarItem(session, null, "home", "logo", 20);

		setItem(item);

		HomeSideTop sideTop = new HomeSideTop(session);
		UserBar sideBot = new UserBar(session);

		sideTop.setAction(() -> {
			// TODO search for users, servers... etc
		});

		getSide().setTop(sideTop);
		getSide().setBottom(sideBot);

		ContextMenu menu1 = new ContextMenu(session.getWindow());
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

		menu1.addMenuItem("Light", () -> session.getWindow().setStyle(Style.LIGHT));
		menu1.separate();
		menu1.addMenuItem("Dark", () -> session.getWindow().setStyle(Style.DARK));

		menuTest = new Text("Context Menu Test");
		stylyingTest = new Text("Global styling test");

		menu.install(menuTest);
		menu1.install(stylyingTest);

		menuTest.setFont(new Font(16).getFont());
		menuTest.setCursor(Cursor.HAND);
		
		stylyingTest.setFont(new Font(16).getFont());
		stylyingTest.setCursor(Cursor.HAND);

		VBox content = new VBox(20, menuTest, stylyingTest);
		content.setAlignment(Pos.TOP_CENTER);
		getSide().setCenter(content);

		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		item.setColor(style.getAccent());
		menuTest.setFill(style.getTextNormal());
		stylyingTest.setFill(style.getTextNormal());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
