package mesa.api;

import java.util.function.Consumer;

import org.json.JSONObject;

import mesa.data.SessionManager;

public class Session {
	private static final String USER_ID = "user_id";

	private Session() {

	}

	private static void call(String path, String action, Consumer<JSONObject> onResult, Param... params) {
		API.asyncPost(path, action, onResult, SessionManager.getSession(), params);
	}

	public static void logout(String userId, Consumer<JSONObject> onResult) {
		call(API.Session.LOGOUT, "logout", onResult, new Param(USER_ID, userId));
	}

	public static void getUser(Consumer<JSONObject> onResult) {
		call(API.Session.GET_USER, "get user data", onResult);
	}
}
