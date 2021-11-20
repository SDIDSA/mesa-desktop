package mesa.app.pages.session.content;

import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.items.BarItem;

public abstract class Content {
	private SessionPage session;
	
	private BarItem item;
	private ContentSide side;
	private ContentMain main;
	
	protected Content(SessionPage session) {
		this.session = session;
		
		side = new ContentSide();
		main = new ContentMain();
	}
	
	public void setItem(BarItem item) {
		this.item = item;
		
		item.setAction(()-> session.load(this));
	}
	
	public BarItem getItem() {
		return item;
	}
	
	public ContentSide getSide() {
		return side;
	}
	
	public ContentMain getMain() {
		return main;
	}
}
