package mesa.app.pages.session.types.server.center;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.popup.tooltip.KeyedTooltip;
import mesa.gui.controls.popup.tooltip.Tooltip;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class ChannelTopIcon extends ColorIcon {

	private BooleanProperty active;
	private boolean ignoreStyle = false;

	public ChannelTopIcon(String icon, String hint, double size) {
		super(icon, size, hint != null);

		if (hint == null) {
			setIgnoreStyle(true);
		} else {
			this.sceneProperty().addListener(new ChangeListener<Scene>() {
				@Override
				public void changed(ObservableValue<? extends Scene> obs, Scene ov, Scene nv) {
					if(nv != null) {
						sceneProperty().removeListener(this);
						
						Tooltip tip = new KeyedTooltip((Window) nv.getWindow(), hint, Direction.DOWN);
						tip.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
						Tooltip.install(ChannelTopIcon.this, tip);
					}
				}
			});			
		}

		setCursor(Cursor.HAND);
		setPadding((24.0 - size) / 2.0);
		active = new SimpleBooleanProperty(false);
	}

	public BooleanProperty activeProperty() {
		return active;
	}
	
	public void toggleActive() {
		active.set(!active.get());
	}
	
	public ChannelTopIcon(String icon, double size) {
		this(icon, null, size);
	}

	public void setIgnoreStyle(boolean ignoreStyle) {
		this.ignoreStyle = ignoreStyle;
		setMouseTransparent(ignoreStyle);
	}

	@Override
	public void applyStyle(Style style) {
		if (ignoreStyle) {
			return;
		}
		super.applyStyle(style);
		fillProperty().bind(Bindings.when(hoverProperty()).then(style.getInteractiveHover()).otherwise(
				Bindings.when(active).then(style.getInteractiveActive()).otherwise(style.getInteractiveNormal())));
	}

}
