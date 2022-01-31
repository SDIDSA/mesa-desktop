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

public class ChannelGroup extends Bean {
	private IntegerProperty id;
	private StringProperty name;

	private BooleanBinding unreadBinding;
	private BooleanProperty unread;
	
	private Server server;

	private ObservableList<Channel> channels;

	public ChannelGroup(JSONObject obj) {
		id = new SimpleIntegerProperty();
		name = new SimpleStringProperty();

		unread = new SimpleBooleanProperty();
		
		channels = FXCollections.observableArrayList();

		init(obj);
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

	public Channel hasChannel(Integer chid) {
		for(Channel channel : channels) {
			if(channel.getId().equals(chid)) {
				return channel;
			}
		}
		
		return null;
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
}