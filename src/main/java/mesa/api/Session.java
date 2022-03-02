package mesa.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Consumer;

import org.json.JSONObject;

import mesa.api.json.Param;
import mesa.api.multipart.FilePart;
import mesa.api.multipart.Part;
import mesa.api.multipart.TextPart;
import mesa.data.SessionManager;
import mesa.data.bean.Channel;
import mesa.data.bean.Server;
import mesa.data.bean.User;

public class Session {

	private Session() {

	}

	private static void call(String path, String action, Consumer<JSONObject> onResult, Param... params) {
		API.asyncJsonPost(path, action, onResult, SessionManager.getSession(), params);
	}

	private static void callMulti(String path, String action, Consumer<JSONObject> onResult, Part... parts) {
		API.asyncMultiPost(path, action, onResult, SessionManager.getSession(), parts);
	}

	public static void logout(Consumer<JSONObject> onResult) {
		call(API.Session.LOGOUT, "logout", onResult);
	}

	public static void createServer(String serverName, String template, String audience, File icon,
			Consumer<JSONObject> onResult) {
		callMulti(API.Session.CREATE_SERVER, "create server", onResult, new TextPart("name", serverName),
				new TextPart("template", template), new TextPart("audience", audience), new FilePart("icon", icon));
	}

	public static void getServers(Consumer<JSONObject> onResult) {
		call(API.Session.GET_SERVERS, "get servers", onResult);
	}

	public static void getServer(int id, Consumer<JSONObject> onResult) {
		call(API.Session.GET_SERVER, "get server data", onResult, new Param(Server.SERVER_ID, id));
	}

	public static void generateInvite(int server, Consumer<JSONObject> onResult) {
		call(API.Session.CREATE_INVITE, "create invite", onResult, new Param(Server.SERVER_ID, server));
	}

	public static void joinWithInvite(String inviteCode, Consumer<JSONObject> onResult) {
		call(API.Session.JOIN_WITH_INVITE, "join server with invite", onResult, new Param("invite_code", inviteCode));
	}

	public static void sendMessage(String content, int channel, int server, Consumer<JSONObject> onResult) {
		try {
			String val = URLEncoder.encode(content, "utf-8");
			call(API.Session.SEND_MESSAGE, "send message", onResult, new Param("content", val),
					new Param(Channel.CHANNEL, channel), new Param(Server.SERVER, server));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void seen(int channel, Consumer<JSONObject> onResult) {
		call(API.Session.SEEN, "mark a channel as seen", onResult, new Param(Channel.CHANNEL, channel));
	}

	public static void getUser(Consumer<JSONObject> onResult) {
		call(API.Session.GET_USER, "get user data", onResult);
	}

	public static void getUserForId(String id, Consumer<JSONObject> onResult) {
		call(API.Session.GET_USER_FOR_ID, "get user data for id : " + id, onResult, new Param(User.USER_ID, id));
	}

	public static void getMessages(int channel, Consumer<JSONObject> onResult) {
		call(API.Session.GET_MESSAGES, "get messasges for channel " + channel, onResult, new Param(Channel.CHANNEL, channel));
	}

	public static void deleteChannel(int channel, int server, Consumer<JSONObject> onResult) {
		call(API.Session.DELETE_CHANNEL, "delete channel" + channel, onResult,
				new Param(Channel.CHANNEL, channel),
				new Param(Server.SERVER, server));
	}
	
	public static void createChannel(int server, int group, String name, String type, Consumer<JSONObject> onResult) {
		call(API.Session.CREATE_CHANNEL, "create channel", onResult, new Param(Server.SERVER, server), new Param("group", group), new Param("name", name), new Param("type", type));
	}
}
