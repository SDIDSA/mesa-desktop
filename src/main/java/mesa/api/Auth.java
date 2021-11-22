package mesa.api;

import java.util.function.Consumer;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

public class Auth {
	public static final JSONObject netErr = new JSONObject("{\"err\":[{\"key\":\"global\",\"value\":\"net_err\"}]}");
	private static final String USER_ID = "user_id";
	private static final String PASSWORD = "password";
	
	private Auth() {
		
	}
	
	public static void auth(String email, String password, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.LOGIN, "login with credentials", onResult,
				new Param("email_phone", email),
				new Param(PASSWORD, hashPassword(password)));
	}

	public static void register(String email, String username, String password, String birthDate,
			Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.REGISTER, "create account", onResult,
				new Param("email", email),
				new Param("username", username),
				new Param(PASSWORD, hashPassword(password)),
				new Param("birth_date", birthDate));	
	}
	
	public static void verifyEmail(String userId, String code, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.VERIFY_EMAIL,  "verify email", onResult,
				new Param(USER_ID, userId),
				new Param("verification_code", code));
	}
	
	public static void editUsername(String userId, String username, String password, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.EDIT_USERNAME, "change username", onResult, 
				new Param(USER_ID, userId),
				new Param("username", username),
				new Param(PASSWORD, hashPassword(password)));
	}
	
	public static void editEmail(String userId, String email, String password, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.EDIT_EMAIL, "change email", onResult, 
				new Param(USER_ID, userId),
				new Param("email", email),
				new Param(PASSWORD, hashPassword(password)));
	}
	
	public static void changePassword(String userId, String currentPass, String newPass, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.CHANGE_PASSWORD, "change password", onResult,
				new Param(USER_ID, userId),
				new Param("curr_pass", hashPassword(currentPass)),
				new Param("new_pass", hashPassword(newPass)));
	}
	
	public static void deleteAccount(String userId, String password, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.DELETE_ACCOUNT, "delete account", onResult,
				new Param(USER_ID, userId),
				new Param(PASSWORD, password));
	}

	public static String hashPassword(String password) {
		return DigestUtils.sha256Hex(password);
	}
}
