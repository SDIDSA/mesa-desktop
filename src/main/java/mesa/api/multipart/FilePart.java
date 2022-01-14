package mesa.api.multipart;

import java.io.File;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

public class FilePart extends Part {

	private File value;
	
	public FilePart(String key, File value) {
		super(key);
		this.value = value;
	}

	@Override
	public ContentBody getValue() {
		return new FileBody(value);
	}

}
