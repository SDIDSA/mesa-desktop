package mesa.api;

import java.util.function.Consumer;

import org.json.JSONObject;

import javafx.application.Platform;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.exception.LogHandler;

public class API {
	public static final JSONObject netErr = new JSONObject("{\"err\":[{\"key\":\"global\",\"value\":\"net_err\"}]}");

	private static final String BASE = "http://localhost:4000/";

	public static class Auth {
		private static final String PREFIX = BASE + "auth/";

		public static final String LOGIN = PREFIX + "login";

		public static final String REGISTER = PREFIX + "register";

		public static final String VERIFY_EMAIL = PREFIX + "verify";

		public static final String EDIT_USERNAME = PREFIX + "editUsername";

		public static final String EDIT_EMAIL = PREFIX + "editEmail";

		public static final String CHANGE_PASSWORD = PREFIX + "changePassword";
	}

	public static void asyncPost(String path, String action, Consumer<JSONObject> onResult, Param... params) {
		new Thread() {
			public void run() {
				try {
					new ApiCall(path, params).execute(result -> {
						LogHandler.log(result.toString(4));
						onResult.accept(result);
					});
				} catch (Exception x) {
					ErrorHandler.handle(x, action);
					Platform.runLater(() -> onResult.accept(netErr));
				}
			}
		}.start();
	}
}
