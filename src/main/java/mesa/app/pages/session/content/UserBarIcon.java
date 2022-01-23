package mesa.app.pages.session.content;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import mesa.app.pages.session.SessionPage;
import mesa.gui.NodeUtils;
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
		setPadding(new Insets(5));
		setCursor(Cursor.HAND);

		icon = new ColorIcon(name, 20);
		icon.setMouseTransparent(true);
		icon.setScaleX(.8);
		icon.setScaleY(.8);
		
		setFocusTraversable(true);

		KeyedTooltip tip = new KeyedTooltip(session.getWindow(), tooltip, Tooltip.UP);
		tip.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		Tooltip.install(this, tip);

		setMaxHeight(32);

		setOnMouseClicked(e -> {
			if (action != null) {
				action.run();
			}
		});
		setOnKeyPressed(e-> {
			if(e.getCode().equals(KeyCode.SPACE) && action != null) {
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
		Background back = Backgrounds.make(style.getBackgroundModifierSelected(), 4.0);

		backgroundProperty().bind(Bindings.when(hoverProperty()).then(back).otherwise(Background.EMPTY));
		NodeUtils.focusBorder(this, style.getTextLink());
		icon.fillProperty().bind(Bindings.when(hoverProperty()).then(style.getInteractiveHover())
				.otherwise(style.getInteractiveNormal()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
