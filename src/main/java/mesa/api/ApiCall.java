package mesa.api;

import java.io.IOException;
import java.util.function.Consumer;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public abstract class ApiCall {
	protected static CloseableHttpClient client = HttpClients.createDefault();
	protected String path;

	protected ApiCall(String path) {
		this.path = path;
	}

	public abstract void execute(Consumer<JSONObject> onResult, String token) throws IOException;
}
