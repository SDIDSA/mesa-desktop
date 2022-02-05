package mesa.app.pages.session;

import java.awt.Dimension;
import java.util.HashMap;

import org.json.JSONObject;

import io.socket.client.Socket;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import mesa.api.Session;
import mesa.app.pages.Page;
import mesa.app.pages.login.LoginPage;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.items.BarItem;
import mesa.app.pages.session.settings.GlobalSettings;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.menu.SectionItem;
import mesa.app.pages.session.types.server.ServerContent;
import mesa.app.pages.session.types.server.center.ChannelDisplayMain;
import mesa.app.pages.session.types.server.left.ChannelEntry;
import mesa.app.utils.Threaded;
import mesa.data.SessionManager;
import mesa.data.bean.Channel;
import mesa.data.bean.Message;
import mesa.data.bean.Server;
import mesa.data.bean.User;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.controls.alert.Alert;
import mesa.gui.controls.alert.AlertType;
import mesa.gui.controls.alert.ButtonType;
import mesa.gui.controls.popup.tooltip.Tooltip;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class SessionPage extends Page {
	private User user;

	private static double duration = .4;
	private static Interpolator inter = SplineInterpolator.ANTICIPATEOVERSHOOT;

	private StackPane side;
	private StackPane main;

	private HBox root;
	private Settings settings;

	private ServerBar servers;

	private Content loaded;

	public SessionPage(Window window) {
		super(window, new Dimension(970, 530));

		user = new User(window.getJsonData("user"));
		window.putLoggedUser(user);

		registerSocket(window);

		root = new HBox();
		root.setMinHeight(0);
		root.setMaxHeight(-1);

		settings = new GlobalSettings(this);

		servers = new ServerBar(this);

		side = new StackPane();
		side.setMinWidth(240);
		side.setAlignment(Pos.TOP_CENTER);

		main = new StackPane();
		main.setAlignment(Pos.TOP_CENTER);
		HBox.setHgrow(main, Priority.ALWAYS);

		root.getChildren().addAll(servers, side, main);

		try {
			window.getServers().forEach(obj -> {
				int serId = ((JSONObject) obj).getInt("server");
				int order = ((JSONObject) obj).getInt("order");

				Session.getServer(serId, result -> {
					Server server = new Server(result.getJSONObject("server"), order);

					ServerContent sc = new ServerContent(this, server);

					Threaded.runAfter(500, () -> servers.addServer(sc));
				});
			});
		} catch (Exception x) {
			x.printStackTrace();
		}

		getChildren().add(root);
		applyStyle(window.getStyl());
	}

	private void registerSocket(Window window) {
		Socket socket = window.getMainSocket();

		socket.on("user_sync", data -> {
			JSONObject obj = new JSONObject(data[0].toString());

			obj.keySet().forEach(key -> Platform.runLater(() -> user.set(key, obj.get(key))));
		});
		
		socket.on("user_change", data -> {
			JSONObject obj = new JSONObject(data[0].toString());
			String uid = obj.getString("user_id");
			
			obj.remove("user_id");
			
			User.getForId(uid, foundUser -> 
									obj.keySet().forEach(key -> 
											Platform.runLater(() -> 
												foundUser.set(key, obj.get(key)))));
		});

		socket.on("join_server", data -> {
			JSONObject obj = new JSONObject(data[0].toString());

			int id = obj.getInt("id");
			int order = obj.getInt("order");

			Session.getServer(id, result -> {
				Server server = new Server(result.getJSONObject("server"), order);

				ServerContent sc = new ServerContent(this, server);

				servers.addServer(sc);
			});
		});

		socket.on("message", data -> {
			JSONObject obj = new JSONObject(data[0].toString());

			Platform.runLater(() -> servers.handleMessage(Message.get(obj)));
		});

		socket.on("delete_channel", data -> {
			JSONObject obj = new JSONObject(data[0].toString());

			int server = obj.getInt("server");
			int channel = obj.getInt("channel");

			Platform.runLater(() -> servers.removeChannel(server, channel));
		});

		socket.on("create_channel", data -> {
			JSONObject obj = new JSONObject(data[0].toString());

			int server = obj.getInt("server");
			int group = obj.getInt("group");
			Channel channel = new Channel(obj.getJSONObject("channel"));

			Platform.runLater(() -> servers.addChannel(server, group, channel));
		});
	}

	public void showSettings() {
		showSettings(settings);
	}

	private static HashMap<Settings, SettingsTransition> transitionCache = new HashMap<>();

	public void showSettings(Settings other) {
		SettingsTransition transition = transitionCache.get(other);
		if (transition == null) {
			transition = new SettingsTransition(other, this);
			transitionCache.put(other, transition);
		}

		if (!getChildren().contains(other)) {
			getChildren().add(other);
		}
		root.setMouseTransparent(true);
		other.setMouseTransparent(false);

		transition.show();

		other.requestFocus();
	}

	@Override
	public void requestFocus() {
		getChildren().get(0).requestFocus();
	}

	public void hideSettings(Settings other) {
		SettingsTransition transition = transitionCache.get(other);

		if (!getChildren().contains(root)) {
			getChildren().add(root);
		}
		root.setMouseTransparent(false);
		other.setMouseTransparent(true);

		transition.hide();
	}

	public User getUser() {
		return user;
	}

	public void load(Content content) {
		side.getChildren().setAll(content.getSide());
		main.getChildren().setAll(content.getMain());

		content.onLoad();
		loaded = content;
	}

	public boolean isLoaded(Content content) {
		return loaded == content;
	}

	public Content getLoaded() {
		return loaded;
	}

	public void logout(Runnable onDone) {
		Session.logout(e -> {
			window.getMainSocket().off("user_sync");
			window.getMainSocket().io().off("reconnect");
			SessionManager.clearSession();
			SectionItem.clearCache();
			ChannelDisplayMain.clearCache();
			ChannelEntry.clearCache();
			transitionCache.clear();
			BarItem.clear();
			Tooltip.clear();
			window.clearLoggedUser();
			window.loadPage(LoginPage.class);
			if (onDone != null) {
				onDone.run();
			}
		});
	}

	public void logout() {
		logout(null);
	}

	public void logoutPrompt() {
		Alert confirm = new Alert(this, AlertType.LOGOUT);
		confirm.setHead("log_out");
		confirm.addLabel("logout_confirm");
		confirm.addAction(ButtonType.LOGOUT, () -> {
			confirm.startLoading(ButtonType.LOGOUT);
			logout(() -> confirm.stopLoading(ButtonType.LOGOUT));
		});
		confirm.show();
	}

	@Override
	public void applyStyle(Style style) {
		window.setFill(style.getBackgroundTertiary());
		window.setBorder(style.getSessionWindowBorder(), 1);

		side.setBackground(Backgrounds.make(style.getBackgroundSecondary(), new CornerRadii(8.0, 0, 0, 0, false)));
		main.setBackground(Backgrounds.make(style.getBackgroundPrimary()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

	private static class SettingsTransition {
		private Timeline show;
		private Timeline hide;

		private Runnable before;

		public SettingsTransition(Settings settings, SessionPage page) {
			show = new Timeline(
					new KeyFrame(Duration.seconds(duration), new KeyValue(page.root.opacityProperty(), 0, inter),
							new KeyValue(page.root.scaleXProperty(), 0.93, inter),
							new KeyValue(page.root.scaleYProperty(), 0.93, inter),

							new KeyValue(settings.opacityProperty(), 1, inter),
							new KeyValue(settings.getRoot().scaleXProperty(), 1, inter),
							new KeyValue(settings.getRoot().scaleYProperty(), 1, inter)));
			show.setOnFinished(e -> {
				afterTransition(settings, settings.getRoot(), page.root);
				page.getChildren().remove(page.root);
			});

			hide = new Timeline(
					new KeyFrame(Duration.seconds(duration), new KeyValue(page.root.opacityProperty(), 1, inter),
							new KeyValue(page.root.scaleXProperty(), 1, inter),
							new KeyValue(page.root.scaleYProperty(), 1, inter),

							new KeyValue(settings.opacityProperty(), 0, inter),
							new KeyValue(settings.getRoot().scaleXProperty(), 1.1, inter),
							new KeyValue(settings.getRoot().scaleYProperty(), 1.1, inter)));

			hide.setOnFinished(e -> {
				afterTransition(settings, settings.getRoot(), page.root);
				page.getChildren().remove(settings);
				page.requestFocus();
			});

			before = () -> beforeTransition(settings, settings.getRoot(), page.root);
		}

		public void show() {
			before.run();

			hide.stop();
			show.playFromStart();
		}

		public void hide() {
			before.run();

			show.stop();
			hide.playFromStart();
		}

		private static void prepareNode(Node... nodes) {
			for (Node node : nodes) {
				node.setCache(true);
				node.setCacheHint(CacheHint.SPEED);
			}
		}

		private static void clearNode(Node... nodes) {
			for (Node node : nodes) {
				node.setCache(false);
				node.setCacheHint(CacheHint.DEFAULT);
			}
		}

		private static void beforeTransition(Node... nodes) {
			prepareNode(nodes);
		}

		private static void afterTransition(Node... nodes) {
			clearNode(nodes);
		}
	}

}
