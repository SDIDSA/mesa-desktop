package mesa.gui.controls.popup.context;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ContextMenuItem extends StackPane implements Styleable {
	protected Text lab;
	private BooleanProperty active;
	
	private ContextMenu menu;
	
	private Runnable action;
	
	private Color fill;
	
	public ContextMenuItem(ContextMenu menu, String key, Color fill, boolean keyed) {
		this.menu = menu;
		this.fill = fill;
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(8));
		setCursor(Cursor.HAND);
		
		Font f = new Font(Font.DEFAULT_FAMILY_MEDIUM, 14);
		lab = keyed ? new Label(menu.getOwner(), key, f) : new Text(key, f);
		getChildren().add(lab);
		
		active = new SimpleBooleanProperty(false);
		
		applyStyle(menu.getOwner().getStyl());
	}

	public ContextMenuItem(ContextMenu menu, String key, Color fill) {
		this(menu, key, fill, false);
	}

	public ContextMenuItem(ContextMenu menu, String key) {
		this(menu, key, null, false);
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public void fire() {
		if(action != null) {
			action.run();
		}
		menu.hide();
	}

	public void setActive(boolean active) {
		this.active.set(active);
	}
	
	@Override
	public void applyStyle(Style style) {
		lab.fillProperty().bind(Bindings.when(active).then(Color.WHITE).otherwise(fill == null ? style.getInteractiveNormal() : fill));
		backgroundProperty().bind(Bindings.when(active).then(Backgrounds.make(fill == null ? style.getAccent() : fill, 3.0)).otherwise(Background.EMPTY));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
