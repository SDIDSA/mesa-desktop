package mesa.app.pages.session.content;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.popup.tooltip.KeyedTooltip;
import mesa.gui.controls.popup.tooltip.Tooltip;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class UserBarIcon extends StackPane implements Styleable {
	private ColorIcon icon;

	private Runnable action;
	public UserBarIcon(SessionPage session, String name, String tooltip) {
		setPadding(new Insets(6));
		setCursor(Cursor.HAND);
	
		icon = new ColorIcon(session.getWindow(), name, 20);
		icon.setMouseTransparent(true);
		icon.setScaleX(.8);
		icon.setScaleY(.8);
		
		KeyedTooltip tip = new KeyedTooltip(session.getWindow(), tooltip, Tooltip.UP, -10);
		tip.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		
		Tooltip.install(this, tip);

		setMaxHeight(32);
		
		setOnMouseClicked(e-> {
			if(action != null)  {
				action.run();
			}
		});
		
		getChildren().add(icon);

		applyStyle(session.getWindow().getStyl());
	}

	public void setAction(Runnable action) {
		this.action = action;
	}
	
	@Override
	public void applyStyle(Style style) {
		icon.setFill(style.getText2());
		
		Background back = Backgrounds.make(style.getBackSelectedDeprecated(), 4.0);
		
		backgroundProperty().bind(Bindings.when(hoverProperty()).then(back).otherwise(Background.EMPTY));
	}
}
