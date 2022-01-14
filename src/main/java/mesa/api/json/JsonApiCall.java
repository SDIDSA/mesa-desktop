package mesa.api.json;

import java.io.IOException;
import java.util.function.Consumer;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javafx.application.Platform;
import mesa.api.ApiCall;

public class JsonApiCall extends ApiCall {
	private Param[] params;

	public JsonApiCall(String path, Param... params) {
		super(path);
		this.params = params;
	}

	public void execute(Consumer<JSONObject> onResult, String token) throws IOException {
		HttpPost httpPost = new HttpPost(path);
		httpPost.addHeader("Accept", "application/json");

		if (token != null) {
			httpPost.addHeader("token", token);
		}

		JSONObject paramsToSend = new JSONObject();
		for (Param param : this.params) {
			paramsToSend.put(param.getKey(), param.getValue());
		}

		StringEntity requestEntity = new StringEntity(paramsToSend.toString(), ContentType.APPLICATION_JSON);

		httpPost.setEntity(requestEntity);

		CloseableHttpResponse response = client.execute(httpPost);

		JSONObject res = new JSONObject(EntityUtils.toString(response.getEntity()));

		Platform.runLater(() -> onResult.accept(res));
	}

	public Param[] getParams() {
		return params;
	}

}
