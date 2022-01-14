package mesa.data.bean;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChannelGroup extends Bean {
	private IntegerProperty id;
	private StringProperty name;
	
	private Server server;

	private ObservableList<Channel> channels;

	public ChannelGroup(JSONObject obj) {
		id = new SimpleIntegerProperty();
		name = new SimpleStringProperty();

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