package mesa.api;

import java.io.IOException;
import java.util.function.Consumer;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javafx.application.Platform;

public class ApiCall {
	private static CloseableHttpClient client = HttpClients.createDefault();

	private String path;
	private Param[] params;

	public ApiCall(String path, Param... params) {
		this.path = path;
		this.params = params;
	}

	public void execute(Consumer<JSONObject> onResult) throws IOException  {
		long start = System.currentTimeMillis();
		
		HttpPost httpPost = new HttpPost(path);
		httpPost.addHeader("Accept", "application/json");

		JSONObject paramsToSend = new JSONObject();
		for (Param param : ApiCall.this.params) {
			paramsToSend.put(param.getKey(), param.getValue());
		}

		StringEntity requestEntity = new StringEntity(paramsToSend.toString(), ContentType.APPLICATION_JSON);

		httpPost.setEntity(requestEntity);

		CloseableHttpResponse response = client.execute(httpPost);

		JSONObject res = new JSONObject(EntityUtils.toString(response.getEntity()));

		long dur = System.currentTimeMillis() - start;
		
		if(dur < 600) {
			//do nothing
		}
		
		Platform.runLater(() -> onResult.accept(res));
	}

	public String getPath() {
		return path;
	}

	public Param[] getParams() {
		return params;
	}

}
