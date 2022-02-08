package mesa.data.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import org.json.JSONObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.types.server.center.MessageDisp;
import mesa.gui.exception.ErrorHandler;

public class Message extends Bean implements Comparable<Message> {
	private static HashMap<Integer, Message> cache = new HashMap<>();

	public static synchronized Message get(JSONObject object) {
		Message found = cache.get(object.getInt("id"));
		if (found == null) {
			found = new Message(object);
			cache.put(found.getId(), found);
		}

		return found;
	}

	private IntegerProperty id;
	private IntegerProperty channel;
	private StringProperty sender;
	private StringProperty type;
	private StringProperty content;
	private StringProperty time;

	public Message() {
		id = new SimpleIntegerProperty();
		channel = new SimpleIntegerProperty();
		sender = new SimpleStringProperty();
		type = new SimpleStringProperty();
		content = new SimpleStringProperty();
		time = new SimpleStringProperty();
	}

	public Message(int id, int channel, String sender, String content, String time) {
		this();
		setId(id);
		setChannel(channel);
		setSender(sender);
		setContent(content);
		setTime(time);
	}

	public Message(JSONObject obj) {
		this();
		init(obj);
		if(!getContent().isEmpty()) {
			try {
				setContent(URLDecoder.decode(getContent(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				ErrorHandler.handle(e, "url-decode message content");
			}
		}
	}

	public IntegerProperty idProperty() {
		return id;
	}

	public Integer getId() {
		return id.get();
	}

	public void setId(Integer val) {
		id.set(val);
		cache.put(val, this);
	}

	public IntegerProperty channelProperty() {
		return channel;
	}

	public Integer getChannel() {
		return channel.get();
	}

	public void setChannel(Integer val) {
		channel.set(val);
	}

	public StringProperty senderProperty() {
		return sender;
	}

	public String getSender() {
		return sender.get();
	}

	public void setSender(String val) {
		sender.set(val);
	}

	public StringProperty contentProperty() {
		return content;
	}

	public String getContent() {
		return content.get();
	}

	public void setContent(String val) {
		content.set(val);
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

	public StringProperty timeProperty() {
		return time;
	}

	public String getTime() {
		return time.get();
	}

	public void setTime(String val) {
		time.set(val);
	}

	private MessageDisp display;

	public MessageDisp getDisplay(SessionPage session) {
		if (type.get().equals("text")) {
			if (display == null) {
				display = new MessageDisp(session, this);
			}

			return display;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " {" + "\tsender : " + sender.get() + "\ttype : " + type.get()
				+ "\tcontent : " + content.get() + "\ttime : " + time.get() + "}";
	}

	@Override
	public int compareTo(Message o) {
		return Integer.compare(id.get(), o.id.get());
	}
}