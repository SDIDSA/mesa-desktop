package mesa.gui.controls.popup.context.items;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.popup.context.ContextMenu;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MenuItem extends HBox implements Styleable {
	protected Text lab;
	protected BooleanProperty active;
	
	private ContextMenu menu;
	
	private Runnable action;
	
	private Color fill;
	
	private boolean hideOnAction = true;
	
	private KeyCombination accelerator;
	private Text acceleratorLabel;
	
	public MenuItem(ContextMenu menu, String key, Color fill, boolean keyed) {
		this.menu = menu;
		this.fill = fill;
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(8));
		setCursor(Cursor.HAND);
		
		Font f = new Font(Font.DEFAULT_FAMILY_MEDIUM, 14);
		lab = keyed ? new Label(menu.getOwner(), key, f) : new Text(key, f);
		getChildren().addAll(lab, new ExpandingHSpace());
		
		ColorAdjust ca = new ColorAdjust();
		
		setEffect(ca);
		
		ca.brightnessProperty().bind(Bindings.when(pressedProperty()).then(-.2).otherwise(0));
		
		active = new SimpleBooleanProperty(false);
		
		applyStyle(menu.getOwner().getStyl());
	}
	
	public void setHideOnAction(boolean hideOnAction) {
		this.hideOnAction = hideOnAction;
	}
	
	public boolean isHideOnAction() {
		return hideOnAction;
	}
	
	public KeyCombination getAccelerator() {
		return accelerator;
	}
	
	public void setAccelerator(KeyCombination accelerator) {
		this.accelerator = accelerator;
	}
	
	public void setAccelerator(String keyComb) {
		setAccelerator(KeyCombination.keyCombination(keyComb));
		String text = accelerator.getName();
		if(acceleratorLabel == null) {
			acceleratorLabel = new Text(text, new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
			acceleratorLabel.fillProperty().bind(lab.fillProperty());
			getChildren().add(acceleratorLabel);
		}else {
			acceleratorLabel.set(text);
		}
	}

	public MenuItem(ContextMenu menu, String key, Color fill) {
		this(menu, key, fill, false);
	}

	public MenuItem(ContextMenu menu, String key) {
		this(menu, key, null, false);
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public void fire() {
		if(action != null) {
			try {
				action.run();
			}catch(Exception x) {
				ErrorHandler.handle(x, "fire action for menu item [" + lab.getText() + "]");
			}
		}
		if(hideOnAction) {
			menu.hide();
		}
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
