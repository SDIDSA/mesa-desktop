package mesa.data.bean;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mesa.app.pages.session.types.server.ServerContent;
import mesa.app.pages.session.types.server.left.ChannelGroupEntry;

public class ChannelGroup extends Bean {
	private IntegerProperty id;
	private StringProperty name;

	private BooleanBinding unreadBinding;
	private BooleanProperty unread;
	
	private Server server;

	private ObservableList<Channel> channels;

	private ChannelGroupEntry channelGroupEntry;
	
	public ChannelGroup(JSONObject obj) {
		id = new SimpleIntegerProperty();
		name = new SimpleStringProperty();

		unread = new SimpleBooleanProperty();
		
		channels = FXCollections.observableArrayList();

		init(obj);
	}
	
	public void setChannelGroupEntry(ChannelGroupEntry channelGroupEntry) {
		this.channelGroupEntry = channelGroupEntry;
	}
	
	public ChannelGroupEntry getChannelGroupEntry() {
		return channelGroupEntry;
	}
	
	public ServerContent getServerContent() {
		return server.getServerContent();
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public Server getServer() {
		return server;
	}

	public ObservableList<Channel> getChannels() {
		return channels;
	}

	public void addChannel(Channel channel) {
		channel.setGroup(this);
		channels.add(channel);
		
		if(unreadBinding == null) {
			unreadBinding = Bindings.when(channel.unreadProperty()).then(true).otherwise(false);
		}else {
			unreadBinding = unreadBinding.or(channel.unreadProperty());
		}
		
		unread.unbind();
		unread.bind(unreadBinding);
	}

	public boolean isUnread() {
		return unread.get();
	}
	
	public BooleanProperty unreadProperty() {
		return unread;
	}
	
	public void setChannels(JSONArray arr) {
		arr.forEach(obj -> addChannel(new Channel((JSONObject) obj)));
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

	public Channel getChannel(Integer chid) {
		for(Channel channel : channels) {
			if(channel.getId().equals(chid)) {
				return channel;
			}
		}
		
		return null;
	}
	
	public boolean hasChannel(Integer chid) {
		for(Channel channel : channels) {
			if(channel.getId().equals(chid)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "{"
				+ "\n\t\tid : " + id.get() 
				+ "\n\t\tname : " + name.get() 
				+ "\n\t\tchannels : " + stringifyChannels()
			+ "\n\t}";
	}
	
	private String stringifyChannels() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0;i<channels.size();i++) {
			if(i != 0) {
				sb.append(", ");
			}
			sb.append(channels.get(i).toString());
		}
		
		return sb.toString();
	}

	public boolean removeChannel(int channel) {
		boolean removed = channels.removeIf(ch -> ch.getId().equals(channel));
		if(removed) {
			if(!channels.isEmpty()) {
				unreadBinding = Bindings.when(channels.get(0).unreadProperty()).then(true).otherwise(false);
				
				for(int i = 1; i < channels.size(); i++) {
					unreadBinding = unreadBinding.or(channels.get(i).unreadProperty());
				}
				
				unread.unbind();
				unread.bind(unreadBinding);
			}else {
				unreadBinding = null;
				
				unread.unbind();
				unread.set(false);
			}
			return true;
		}
		return false;
	}
}