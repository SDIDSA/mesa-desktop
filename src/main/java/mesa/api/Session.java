package mesa.api;

import java.io.File;
import java.util.function.Consumer;

import org.json.JSONObject;

import mesa.api.json.Param;
import mesa.api.multipart.FilePart;
import mesa.api.multipart.Part;
import mesa.api.multipart.TextPart;
import mesa.data.SessionManager;

public class Session {
	private static final String SERVER_ID = "server_id";
	
	private Session() {

	}

	private static void call(String path, String action, Consumer<JSONObject> onResult, Param... params) {
		API.asyncJsonPost(path, action, onResult, SessionManager.getSession(), params);
	}

	private static void callMulti(String path, String action, Consumer<JSONObject> onResult, Part...parts) {
		API.asyncMultiPost(path, action, onResult, SessionManager.getSession(), parts);
	}

	public static void logout(Consumer<JSONObject> onResult) {
		call(API.Session.LOGOUT, "logout", onResult);
	}

	public static void createServer(String serverName, String template, String audience, File icon, Consumer<JSONObject> onResult) {
		callMulti(API.Session.CREATE_SERVER, "create server", onResult,
				new TextPart("name", serverName),
				new TextPart("template", template),
				new TextPart("audience", audience),
				new FilePart("icon", icon));
	}
	
	public static void getServers(Consumer<JSONObject> onResult) {
		call(API.Session.GET_SERVERS, "get servers", onResult);
	}
	
	public static void getServer(int id, Consumer<JSONObject> onResult) {
		call(API.Session.GET_SERVER, "get server data", onResult, new Param(SERVER_ID, Integer.toString(id)));
	}
	
	public static void generateInvite(int server, Consumer<JSONObject> onResult) {
		call(API.Session.CREATE_INVITE, "create invite", onResult, new Param(SERVER_ID, Integer.toString(server)));
	}
	
	public static void joinWithInvite(String inviteCode, Consumer<JSONObject> onResult) {
		call(API.Session.JOIN_WITH_INVITE, "join server with invite", onResult, new Param("invite_code", inviteCode));
	}
	
	public static void getUser(Consumer<JSONObject> onResult) {
		call(API.Session.GET_USER, "get user data", onResult);
	}
}
