package mesa.api;

import java.util.function.Consumer;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

public class Auth {
	public static final JSONObject netErr = new JSONObject("{\"err\":[{\"key\":\"global\",\"value\":\"net_err\"}]}");
	
	public static void auth(String email, String password, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.LOGIN, "login with credentials", onResult,
				new Param("email_phone", email),
				new Param("password", hashPassword(password)));
	}

	public static void register(String email, String username, String password, String birthDate,
			Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.REGISTER, "create account", onResult,
				new Param("email", email),
				new Param("username", username),
				new Param("password", hashPassword(password)),
				new Param("birth_date", birthDate));	
	}
	
	public static void verifyEmail(String user_id, String code, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.VERIFY_EMAIL,  "verify email", onResult,
				new Param("user_id", user_id),
				new Param("verification_code", code));
	}
	
	public static void editUsername(String user_id, String username, String password, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.EDIT_USERNAME, "change username", onResult, 
				new Param("user_id", user_id),
				new Param("username", username),
				new Param("password", hashPassword(password)));
	}
	
	public static void editEmail(String user_id, String email, String password, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.EDIT_EMAIL, "change email", onResult, 
				new Param("user_id", user_id),
				new Param("email", email),
				new Param("password", hashPassword(password)));
	}
	
	public static void changePassword(String user_id, String current_pass, String new_pass, Consumer<JSONObject> onResult) {
		API.asyncPost(API.Auth.CHANGE_PASSWORD, "change password", onResult,
				new Param("user_id", user_id),
				new Param("curr_pass", hashPassword(current_pass)),
				new Param("new_pass", hashPassword(new_pass)));
	}

	public static String hashPassword(String password) {
		return DigestUtils.sha256Hex(password);
	}
}
