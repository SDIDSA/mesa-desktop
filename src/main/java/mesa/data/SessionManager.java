package mesa.data;

//import java.util.HashMap;
import java.util.prefs.Preferences;

import io.socket.client.Socket;
import mesa.app.utils.Threaded;
import mesa.gui.window.Window;

public class SessionManager {
	private static final String ACCESS_TOKEN = "access_token";

//	private static HashMap<String, String> data = new HashMap<>();
	private SessionManager() {

	}

	public static void put(String key, String value) {
		Preferences.userRoot().put(key, value);
//		data.put(key, value);
	}

	public static String get(String key) {
		return Preferences.userRoot().get(key, null);
//		return data.get(key);
	}

	public static void registerSocket(Socket socket, String token) {
		Runnable register = () -> socket.emit("register", JsonUtils.make("socket", socket.id(), "token", token));

		System.out.println("listening for reconnect...");
		socket.io().on("reconnect", data -> 
			new Thread(() -> {
				Threaded.waitWhile(() -> socket.id() == null);
				register.run();
			}
		).start());
		register.run();
	}

	public static void storeSession(String token, Window window) {
		put(ACCESS_TOKEN, token);
		registerSocket(window.getMainSocket(), token);
	}

	public static String getSession() {
		return get(ACCESS_TOKEN);
	}

	public static void clearSession() {
		Preferences.userRoot().remove(ACCESS_TOKEN);
//		data.remove(ACCESS_TOKEN);
	}
}
