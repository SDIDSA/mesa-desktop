package mesa.api;

import java.util.function.Consumer;

import org.json.JSONObject;

import javafx.application.Platform;
import mesa.api.json.JsonApiCall;
import mesa.api.json.Param;
import mesa.api.multipart.MultiPartApiCall;
import mesa.api.multipart.Part;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.exception.LogHandler;

public class API {
	public static final JSONObject netErr = new JSONObject("{\"err\":[{\"key\":\"global\",\"value\":\"net_err\"}]}");

	public static final String BASE = "http://localhost:4000/";
	
	public static final String INVITE_BASE = "https://mesa-invite.tk/";
	
	public static final String INVITE_CODE = "hTKzmak";

	public static class Auth {
		private static final String PREFIX = BASE + "auth/";

		public static final String LOGIN = PREFIX + "login";

		public static final String REGISTER = PREFIX + "register";

		public static final String VERIFY_EMAIL = PREFIX + "verify";

		public static final String EDIT_USERNAME = PREFIX + "editUsername";

		public static final String EDIT_EMAIL = PREFIX + "editEmail";

		public static final String SEND_PHONE_CODE = PREFIX + "sendPhoneCode";

		public static final String VERIFY_PHONE = PREFIX + "verifyPhone";

		public static final String FINALIZE_PHONE = PREFIX + "finalizePhone";

		public static final String REMOVE_PHONE = PREFIX + "removePhone";

		public static final String CHANGE_PASSWORD = PREFIX + "changePassword";
		
		public static final String DELETE_ACCOUNT = PREFIX + "deleteAccount";
		
		private Auth() {
			
		}
	}
	
	public static class Session {
		private static final String PREFIX = BASE + "session/";

		public static final String LOGOUT = PREFIX + "logout";
		
		public static final String GET_USER = PREFIX + "getUser";
		
		public static final String CREATE_SERVER = PREFIX + "createServer";

		public static final String GET_SERVERS = PREFIX + "getServers";
		
		public static final String GET_SERVER = PREFIX + "getServer";
		
		public static final String CREATE_INVITE = PREFIX + "createInvite";
		
		public static final String JOIN_WITH_INVITE = PREFIX + "joinWithInvite";
		
		private Session() {
			
		}
	}

	public static void asyncJsonPost(String path, String action, Consumer<JSONObject> onResult, String session, Param... params) {
		asyncPost(new JsonApiCall(path, params), action, onResult, session);
	}

	public static void asyncMultiPost(String path, String action, Consumer<JSONObject> onResult, String session, Part...parts) {
		asyncPost(new MultiPartApiCall(path, parts), action, onResult, session);
	}

	private static void asyncPost(ApiCall call, String action, Consumer<JSONObject> onResult, String session) {
		new Thread() {
			@Override
			public void run() {
				try {
					call.execute(result -> {
						LogHandler.log(result.toString(4));
						onResult.accept(result);
					}, session);
				} catch (Exception x) {
					ErrorHandler.handle(x, action);
					Platform.runLater(() -> onResult.accept(netErr));
				}
			}
		}.start();
	}
	
	
	public static void asyncJsonPost(String path, String action, Consumer<JSONObject> onResult, Param... params) {
		asyncJsonPost(path, action, onResult, null, params);
	}

	private API() {

	}
}
