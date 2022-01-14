package mesa.data.bean;

import org.json.JSONArray;
import org.json.JSONObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server extends Bean {
	private IntegerProperty id;
	private StringProperty owner;
	private StringProperty name;
	private StringProperty icon;
	
	private ObservableList<ChannelGroup> groups;
	private ObservableList<String> members;
	
	private int order;

	public Server(JSONObject obj, int order) {
		id = new SimpleIntegerProperty();
		owner = new SimpleStringProperty();
		name = new SimpleStringProperty();
		icon = new SimpleStringProperty();
		
		groups = FXCollections.observableArrayList();
		members = FXCollections.observableArrayList();
		
		init(obj);
		
		this.order = order;
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {"
			+ "\n\tid : " + id.get()
			+ "\n\towner : " + owner.get()
			+ "\n\tname : " + name.get()
			+ "\n\ticon : " + icon.get()
			+ "\n\tgroups : " + stringifyGroups()
		+ "\n}";
	}
	
	private String stringifyGroups() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0;i<groups.size();i++) {
			if(i != 0) {
				sb.append(", ");
			}
			sb.append(groups.get(i).toString());
		}
		
		return sb.toString();
	}
}