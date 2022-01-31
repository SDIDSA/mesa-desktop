package mesa.app.pages.session.content;

import javafx.scene.layout.BorderPane;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.items.BarItem;

public abstract class Content {
	private SessionPage session;
	
	private BarItem item;
	private BorderPane side;
	private BorderPane main;
	
	protected Content(SessionPage session) {
		this.session = session;
		
		side = new BorderPane();
		main = new BorderPane();

		main.setMinHeight(0);
		main.setMaxHeight(-1);
	}
	
	public void setItem(BarItem item) {
		this.item = item;
		
		item.setAction(()-> session.load(this));
	}
	
	public BarItem getItem() {
		return item;
	}
	
	public BorderPane getSide() {
		return side;
	}
	
	public BorderPane getMain() {
		return main;
	}
}
