package mesa.data.bean;

import org.json.JSONObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Channel extends Bean {
	private IntegerProperty id;
	private StringProperty name;
	private StringProperty type;
	
	private ChannelGroup group;

	public Channel(JSONObject obj) {
		id = new SimpleIntegerProperty();
		name = new SimpleStringProperty();
		type = new SimpleStringProperty();
		init(obj);
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

	@Override
	public String toString() {
		return "{"
				+ "\n\t\t\tid : " + id.get() 
				+ "\n\t\t\tname : " + name.get() 
				+ "\n\t\t\ttype : " + type.get() 
			+ "\n\t\t}";
	}
}