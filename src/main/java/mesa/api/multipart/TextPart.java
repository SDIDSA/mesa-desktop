package mesa.api.multipart;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;

public class TextPart extends Part {

	String value;
	
	public TextPart(String key, String value) {
		super(key);
		this.value = value;
	}
	
	@Override
	public ContentBody getValue() {
		return new StringBody(value, ContentType.MULTIPART_FORM_DATA);
	}

}
