package mesa.gui.window;

import mesa.app.pages.Page;
import mesa.gui.NodeUtils;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.content.AppPreRoot;
import mesa.gui.window.content.Scene;
import mesa.gui.window.content.appBar.AppBar;
import mesa.gui.window.helpers.State;
import mesa.gui.window.helpers.TileHint;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.json.JSONObject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Window extends Stage implements Styleable, Localized {
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	private DoubleProperty borderWidth;
	
	private Style style;
	private Locale locale;
	private AppPreRoot root;

	public Window(Style style, Locale locale) {
		super();
		borderWidth = new SimpleDoubleProperty(0);
		
		initStyle(StageStyle.TRANSPARENT);
		setStyle(style);
		setLocale(locale);

		root = new AppPreRoot(this);

		getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/icon_16.png")));
		getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/icon_32.png")));
		getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/icon_64.png")));
		getIcons().add(new Image(getClass().getResourceAsStream("/images/icons/icon_128.png")));
		
		Scene scene = new Scene(root, 500, 500);
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e-> {
			if(e.getCode().equals(KeyCode.PRINTSCREEN)) {
				WritableImage img = root.getChildren().get(0).snapshot(null, null);
				BufferedImage preRes = SwingFXUtils.fromFXImage(img, null);
				BufferedImage res = new BufferedImage(preRes.getWidth(), preRes.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
				res.getGraphics().drawImage(preRes, 0, 0, null);
				try {
					System.out.println(ImageIO.write(res, "png", new File("snapshot.png")));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		setScene(scene);
	}
	
	public String getOsName() {
		String osName = System.getProperty("os.name").toLowerCase();
	    return osName.indexOf("win") == 0 ? "Windows" : ""; //will handle other operating systems when targetting them
	}
	
	public AppBar getAppBar() {
		return root.getAppBar();
	}

	public void loadPage(Page page) {
		System.gc();
		page.setup(this);
		root.setContent(page);
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

	public Style getStyl() {
		return style;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setStyle(Style style) {
		this.style = style;
		applyStyle(style);
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		applyLocale(locale);
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

	@Override
	public void applyStyle(Style style) {
		if (getScene() == null)
			return;
		NodeUtils.applyStyle(getScene().getRoot(), style);
	}

	@Override
	public void applyLocale(Locale locale) {
		if (getScene() == null)
			return;
		NodeUtils.applyLocale(getScene().getRoot(), locale);
	}

	private void maximize() {
		root.applyTile(TileHint.tileForState(State.N));
	}

	private void restore() {
		root.unTile();
	}
	
	public void putData(String key, Object value) {
		data.put(key, value);
	}
	
	public JSONObject getJsonData(String key) {
		Object obj = data.get(key);
		
		if(obj != null && obj instanceof JSONObject) {
			return (JSONObject) obj;
		}else {
			throw new RuntimeException("no json data was found at key " + key);
		}
	}
	

}
