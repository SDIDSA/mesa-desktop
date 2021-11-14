package mesa.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Param {
	private String key, value;

	public Param(String key, String value) {
		this.key = key;
		this.value = value;
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
