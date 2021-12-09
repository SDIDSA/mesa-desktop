package mesa.app.pages.session;

import java.awt.Dimension;

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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mesa.app.pages.Page;
import mesa.app.pages.login.LoginPage;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.items.BarItem;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.menu.SectionItem;
import mesa.data.User;
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

	private double duration = .4;

	private StackPane side;
	private StackPane main;

	private HBox root;
	private Settings settings;

	private Timeline showSettings;
	private Timeline hideSettings;

	private Interpolator inter = SplineInterpolator.ANTICIPATEOVERSHOOT;

	public SessionPage(Window window) {
		super(window, new Dimension(970, 530));

		user = new User(window.getJsonData("user"));
		window.putLoggedUser(user);
		
		registerSocket(window);

		root = new HBox();

		settings = new Settings(this);
		settings.setOpacity(0);
		settings.getRoot().setScaleX(1.1);
		settings.getRoot().setScaleY(1.1);

		ServerBar servers = new ServerBar(this);

		side = new StackPane();
		side.setMinWidth(240);
		side.setAlignment(Pos.TOP_CENTER);

		main = new StackPane();
		main.setAlignment(Pos.TOP_CENTER);
		HBox.setHgrow(main, Priority.ALWAYS);

		root.getChildren().addAll(servers, side, main);

		showSettings = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(root.opacityProperty(), 0, inter), new KeyValue(root.scaleXProperty(), 0.93, inter),
				new KeyValue(root.scaleYProperty(), 0.93, inter),

				new KeyValue(settings.opacityProperty(), 1, inter),
				new KeyValue(settings.getRoot().scaleXProperty(), 1, inter),
				new KeyValue(settings.getRoot().scaleYProperty(), 1, inter)));
		showSettings.setOnFinished(e -> {
			afterTransition();
			getChildren().remove(root);
		});

		hideSettings = new Timeline(
				new KeyFrame(Duration.seconds(duration), new KeyValue(root.opacityProperty(), 1, inter),
						new KeyValue(root.scaleXProperty(), 1, inter), new KeyValue(root.scaleYProperty(), 1, inter),

						new KeyValue(settings.opacityProperty(), 0, inter),
						new KeyValue(settings.getRoot().scaleXProperty(), 1.1, inter),
						new KeyValue(settings.getRoot().scaleYProperty(), 1.1, inter)));

		hideSettings.setOnFinished(e -> {
			afterTransition();
			getChildren().remove(settings);
		});

		getChildren().add(root);
		applyStyle(window.getStyl());
	}

	private void registerSocket(Window window) {
		Socket socket = window.getMainSocket();

		socket.on("user_sync", data -> {
			JSONObject obj = new JSONObject(data[0].toString());

			obj.keySet().forEach(key -> 
				Platform.runLater(() -> 
					user.set(key, obj.get(key))
				)
			);
		});
	}

	private void prepareNode(Node node) {
		node.setCache(true);
		node.setCacheHint(CacheHint.SPEED);
	}

	private void clearNode(Node node) {
		node.setCache(false);
		node.setCacheHint(CacheHint.DEFAULT);
	}

	private void beforeTransition() {
		prepareNode(root);
		prepareNode(settings);
		prepareNode(settings.getRoot());
	}

	private void afterTransition() {
		clearNode(root);
		clearNode(settings);
		clearNode(settings.getRoot());
	}

	public void showSettings() {
		beforeTransition();
		if (!getChildren().contains(settings)) {
			getChildren().add(settings);
		}
		root.setMouseTransparent(true);
		settings.setMouseTransparent(false);
		hideSettings.stop();
		showSettings.playFromStart();
	}

	public void hideSettings() {
		beforeTransition();
		if (!getChildren().contains(root)) {
			getChildren().add(root);
		}
		settings.setMouseTransparent(true);
		root.setMouseTransparent(false);
		showSettings.stop();
		hideSettings.playFromStart();
	}

	public User getUser() {
		return user;
	}

	public void load(Content content) {
		side.getChildren().setAll(content.getSide());
		main.getChildren().setAll(content.getMain());
	}

	public void logout() {
		SectionItem.clearCache();
		BarItem.clear();
		Tooltip.clear();
		window.clearLoggedUser();
		window.loadPage(LoginPage.class);
	}

	public void logoutPrompt() {
		Alert confirm = new Alert(this, AlertType.LOGOUT);
		confirm.setHead("log_out");
		confirm.setBody("logout_confirm");
		confirm.addAction(ButtonType.LOGOUT, this::logout);
		confirm.show();
	}

	@Override
	public void applyStyle(Style style) {
		window.setFill(style.getBackgroundTertiary());
		window.setBorder(Color.web("#494a4d"), 1);

		side.setBackground(Backgrounds.make(style.getBackgroundSecondary(), new CornerRadii(8.0, 0, 0, 0, false)));
		main.setBackground(Backgrounds.make(style.getBackgroundPrimary()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
