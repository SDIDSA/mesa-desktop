package mesa.gui.window.content;

import java.awt.Dimension;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import mesa.app.pages.Page;
import mesa.gui.window.Window;
import mesa.gui.window.content.app_bar.AppBar;
import mesa.gui.window.helpers.TileHint.Tile;

public class AppPreRoot extends StackPane {
	public static final double DEFAULT_PADDING = 15;

	private BooleanProperty padded;
	private DoubleProperty padding;
	
	private AppRoot root;
	
	public AppPreRoot(Window window) {
		setBackground(Background.EMPTY);
		padded = new SimpleBooleanProperty(true);
		padding = new SimpleDoubleProperty(DEFAULT_PADDING);
		
		padding.bind(Bindings.when(padded).then(DEFAULT_PADDING).otherwise(0));
		
		paddingProperty().bind(Bindings.createObjectBinding(()-> new Insets(padding.get()), padding));
		
		root = new AppRoot(window, this);
		getChildren().setAll(root);
	}
	
	public DoubleProperty paddingProp() {
		return padding;
	}
	
	public boolean isPadded() {
		return padded.get();
	}
	
	public void setPadded(boolean padded) {
		this.padded.set(padded);
	}
	
	public BooleanProperty paddedProperty() {
		return padded;
	}
	
	public void setFill(Paint fill) {
		root.setFill(fill);
	}
	
	public void setBorder(Paint fill, double width) {
		root.setBorderFill(fill, width);
	}
	
	public void setContent(Page page) {
		root.setContent(page);
	}
	
	public void applyTile(Tile tile) {
		root.applyTile(tile);
	}

	public void unTile() {
		root.unTile();
	}
	
	public boolean isTiled() {
		return root.isTiled();
	}

	public void setMinSize(Dimension d) {
		root.setMinSize(d);
	}

	public AppBar getAppBar() {
		return root.getAppBar();
	}
}
