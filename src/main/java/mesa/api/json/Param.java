package mesa.api.json;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Param {
	private String key;
	private String value;

	public Param(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public Param(String key, int value) {
		this(key, Integer.toString(value));
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public NameValuePair norm() {
		return new BasicNameValuePair(key, value);
	}
}
