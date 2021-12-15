package mesa.gui.window;

import mesa.app.pages.Page;
import mesa.data.User;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.locale.Locale;
import mesa.gui.style.Style;
import mesa.gui.window.content.AppPreRoot;
import mesa.gui.window.content.TransparentScene;
import mesa.gui.window.content.app_bar.AppBar;
import mesa.gui.window.helpers.State;
import mesa.gui.window.helpers.TileHint;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import io.socket.client.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Window extends Stage {
	private static final String MAIN_SOCKET = "main_socket";

	private HashMap<String, Object> data = new HashMap<>();

	private ArrayList<Runnable> onClose = new ArrayList<>();

	private DoubleProperty borderWidth;

	private ObjectProperty<Style> style;
	private ObjectProperty<Locale> locale;

	private AppPreRoot root;

	private Application app;

	public Window(Application app, Style style, Locale locale) {
		super();
		this.app = app;
		this.style = new SimpleObjectProperty<>(style);
		this.locale = new SimpleObjectProperty<>(locale);

		borderWidth = new SimpleDoubleProperty(0);

		initStyle(StageStyle.TRANSPARENT);
		setStyle(style);
		setLocale(locale);

		root = new AppPreRoot(this);

		getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/icon_16.png")));
		getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/icon_32.png")));
		getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/icon_64.png")));
		getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/icon_128.png")));

		TransparentScene scene = new TransparentScene(root, 500, 500);

		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.isControlDown()) {
				if (e.getCode().equals(KeyCode.L)) {
					this.style.set(Style.LIGHT);
				} else if (e.getCode().equals(KeyCode.D)) {
					this.style.set(Style.DARK);
				} else if (e.getCode().equals(KeyCode.F)) {
					this.locale.set(Locale.FR_FR);
				} else if (e.getCode().equals(KeyCode.E)) {
					this.locale.set(Locale.EN_US);
				}
			}
		});

		setScene(scene);

		setOnShown(e -> {
			root.requestFocus();
		});

		setOnCloseRequest(e -> {
			e.consume();
			close();
		});
	}

	public void openLink(String link) {
		app.getHostServices().showDocument(link);
	}

	public void addOnClose(Runnable runnable) {
		onClose.add(runnable);
	}

	public String getOsName() {
		String osName = System.getProperty("os.name").toLowerCase();
		return osName.indexOf("win") == 0 ? "Windows" : ""; // will handle other operating systems when targetting them
	}

	public AppBar getAppBar() {
		return root.getAppBar();
	}

	public void loadPage(Class<? extends Page> type, Runnable onFinish) {
		new Thread(() -> {
			try {
				Page page = type.getConstructor(Window.class).newInstance(this);
				Platform.runLater(() -> {
					root.setContent(page);
					if (onFinish != null) {
						onFinish.run();
					}
				});
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException x) {
				ErrorHandler.handle(x, "create page (" + type.getSimpleName() + ".java:0)");
			}
		}).start();
	}

	public void loadPage(Class<? extends Page> type) {
		loadPage(type, null);
	}

	public void setFill(Paint fill) {
		root.setFill(fill);
	}

	public void setBorder(Paint fill, double width) {
		root.setBorder(fill, width);
		borderWidth.set(width);
	}

	public DoubleProperty getBorderWidth() {
		return borderWidth;
	}

	public BooleanProperty paddedProperty() {
		return root.paddedProperty();
	}

	public ObjectProperty<Style> getStyl() {
		return style;
	}

	public ObjectProperty<Locale> getLocale() {
		return locale;
	}

	public void setStyle(Style style) {
		this.style.set(style);
	}

	public void setLocale(Locale locale) {
		this.locale.set(locale);
	}

	public AppPreRoot getRoot() {
		return root;
	}

	public void maxRestore() {
		if (root.isTiled()) {
			restore();
		} else {
			maximize();
		}
	}

	public void setMinSize(Dimension d) {
		root.setMinSize(d);
	}

	private void maximize() {
		root.applyTile(TileHint.tileForState(State.N));
	}

	private void restore() {
		root.unTile();
	}

	@Override
	public void close() {
		onClose.forEach(Runnable::run);
		super.close();
	}

	public void putMainSocket(Socket socket) {
		putData(MAIN_SOCKET, socket);
	}

	public Socket getMainSocket() {
		return getSocket(MAIN_SOCKET);
	}

	public void putData(String key, Object value) {
		data.put(key, value);
	}

	private static final String LOGGED = "logged";

	public void putLoggedUser(User user) {
		putData(LOGGED, user);
	}

	public void clearLoggedUser() {
		data.remove(LOGGED);
	}

	public User getLoggedUser() {
		return getOfType(LOGGED, User.class);
	}

	public JSONObject getJsonData(String key) throws IllegalStateException {
		return getOfType(key, JSONObject.class);
	}

	public Socket getSocket(String key) throws IllegalStateException {
		return getOfType(key, Socket.class);
	}

	private <T> T getOfType(String key, Class<? extends T> type) {
		Object obj = data.get(key);

		if (type.isInstance(obj)) {
			return type.cast(obj);
		} else {
			throw new IllegalStateException("no " + type.getSimpleName() + " was found at key " + key);
		}
	}

}
