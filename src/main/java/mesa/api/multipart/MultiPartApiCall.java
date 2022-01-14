package mesa.api.multipart;

import java.io.IOException;
import java.util.function.Consumer;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javafx.application.Platform;
import mesa.api.ApiCall;

public class MultiPartApiCall extends ApiCall {
	private Part[] parts;

	public MultiPartApiCall(String path, Part... parts) {
		super(path);
		this.parts = parts;
	}
	
	public void execute(Consumer<JSONObject> onResult, String token) throws IOException  {
		HttpPost httpPost = new HttpPost(path);
		httpPost.addHeader("Accept", "application/json");
		
		if(token != null) {
			httpPost.addHeader("token", token);
		}

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		for (Part part : this.parts) {
			builder.addPart(part.getKey(), part.getValue());
		}

		httpPost.setEntity(builder.build());

		CloseableHttpResponse response = client.execute(httpPost);

		JSONObject res = new JSONObject(EntityUtils.toString(response.getEntity()));
		
		Platform.runLater(() -> onResult.accept(res));
	}
}
