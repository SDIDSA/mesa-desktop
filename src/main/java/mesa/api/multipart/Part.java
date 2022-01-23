package mesa.api.multipart;

import org.apache.http.entity.mime.content.ContentBody;

public abstract class Part {
	private String key;
	
	protected Part(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public abstract ContentBody getValue();
}
