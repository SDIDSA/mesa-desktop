package mesa.data.bean;

import org.json.JSONObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mesa.app.pages.session.types.server.ServerContent;
import mesa.app.pages.session.types.server.left.ChannelEntry;
import mesa.app.pages.session.types.server.left.ChannelGroupEntry;

public class Channel extends Bean {
	private static final String TEXT_TYPE = "text";
	
	private IntegerProperty id;
	private StringProperty name;
	private StringProperty type;
	private BooleanProperty unread;
	
	private ChannelGroup group;
	
	private ChannelEntry channelEntry;

	public Channel(JSONObject obj) {
		id = new SimpleIntegerProperty();
		name = new SimpleStringProperty();
		type = new SimpleStringProperty();
		unread = new SimpleBooleanProperty();
		init(obj);
	}
	
	public void setChannelEntry(ChannelEntry channelEntry) {
		this.channelEntry = channelEntry;
	}
	
	public ChannelEntry getChannelEntry() {
		return channelEntry;
	}
	
	public ChannelGroupEntry getChannelGroupEntry() {
		return group.getChannelGroupEntry();
	}
	
	public ServerContent getServerContent() {
		return group.getServerContent();
	}
	
	public void setGroup(ChannelGroup group) {
		this.group = group;
	}
	
	public ChannelGroup getGroup() {
		return group;
	}
	
	public IntegerProperty idProperty() {
		return id;
	}

	public Integer getId() {
		return id.get();
	}

	public void setId(Integer val) {
		id.set(val);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String val) {
		name.set(val);
	}

	public StringProperty typeProperty() {
		return type;
	}

	public String getType() {
		return type.get();
	}

	public void setType(String val) {
		type.set(val);
	}
	
	public BooleanProperty unreadProperty() {
		return unread;
	}
	
	public boolean isUnread() {
		return unread.get();
	}
	
	public void setUnread(Boolean val) {
		unread.set(val);
	}

	public String getTypeChar() {
		return isTextChannel() ? "# " : "";
	}
	
	@Override
	public String toString() {
		return "{"
				+ "\n\t\t\tid : " + id.get() 
				+ "\n\t\t\tname : " + name.get() 
				+ "\n\t\t\ttype : " + type.get() 
			+ "\n\t\t}";
	}

	public boolean isTextChannel() {
		return type.get().equals(TEXT_TYPE);
	}
}