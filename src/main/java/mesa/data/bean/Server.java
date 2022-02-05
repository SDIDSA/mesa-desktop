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

public class Server extends Bean {
	private IntegerProperty id;
	private StringProperty owner;
	private StringProperty name;
	private StringProperty icon;

	private BooleanBinding unreadBinding;
	private BooleanProperty unread;

	private ObservableList<ChannelGroup> groups;
	private ObservableList<String> members;

	private int order;

	private ServerContent serverContent;

	public Server(JSONObject obj, int order) {
		id = new SimpleIntegerProperty();
		owner = new SimpleStringProperty();
		name = new SimpleStringProperty();
		icon = new SimpleStringProperty();

		unread = new SimpleBooleanProperty();

		groups = FXCollections.observableArrayList();
		members = FXCollections.observableArrayList();

		init(obj);

		this.order = order;
	}

	public void setServerContent(ServerContent serverContent) {
		this.serverContent = serverContent;
	}

	public ServerContent getServerContent() {
		return serverContent;
	}

	public int getOrder() {
		return order;
	}

	public ObservableList<ChannelGroup> getGroups() {
		return groups;
	}

	public void addGroup(ChannelGroup group) {
		group.setServer(this);
		groups.add(group);

		if (unreadBinding == null) {
			unreadBinding = Bindings.when(group.unreadProperty()).then(true).otherwise(false);
		} else {
			unreadBinding = unreadBinding.or(group.unreadProperty());
		}

		unread.unbind();
		unread.bind(unreadBinding);
	}

	public void removeChannel(int channel) {
		for (ChannelGroup group : groups) {
			if (group.removeChannel(channel)) {
				break;
			}
		}
	}

	public void addChannel(int groupId, Channel channel) {
		for (ChannelGroup group : groups) {
			if (group.getId().equals(groupId)) {
				group.addChannel(channel);
				break;
			}
		}
	}

	public boolean isUnread() {
		return unread.get();
	}

	public BooleanProperty unreadProperty() {
		return unread;
	}

	public void addMember(String member) {
		members.add(member);
	}

	public IntegerProperty idProperty() {
		return id;
	}

	public void setMembers(JSONArray arr) {
		arr.forEach(obj -> addMember((String) obj));
	}
	
	public ObservableList<String> getMembers() {
		return members;
	}

	public void setGroups(JSONArray arr) {
		arr.forEach(obj -> addGroup(new ChannelGroup((JSONObject) obj)));
	}

	public Integer getId() {
		return id.get();
	}

	public void setId(Integer val) {
		id.set(val);
	}

	public StringProperty ownerProperty() {
		return owner;
	}

	public String getOwner() {
		return owner.get();
	}

	public void setOwner(String val) {
		owner.set(val);
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

	public StringProperty iconProperty() {
		return icon;
	}

	public String getIcon() {
		return icon.get();
	}

	public void setIcon(String val) {
		icon.set(val);
	}

	public Channel getChannel(Integer channel) {
		for (ChannelGroup group : groups) {
			Channel ch = group.getChannel(channel);
			if (ch != null) {
				return ch;
			}
		}

		return null;
	}

	public boolean hasChannel(Integer channel) {
		for (ChannelGroup group : groups) {
			if (group.hasChannel(channel)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {" + "\n\tid : " + id.get() + "\n\towner : " + owner.get() + "\n\tname : "
				+ name.get() + "\n\ticon : " + icon.get() + "\n\tgroups : " + stringifyGroups() + "\n}";
	}

	private String stringifyGroups() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < groups.size(); i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(groups.get(i).toString());
		}

		return sb.toString();
	}
}