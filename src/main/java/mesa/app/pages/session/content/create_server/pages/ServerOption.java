package mesa.app.pages.session.content.create_server.pages;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontWeight;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.image.layer_icon.LayerIcon;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class ServerOption extends HBox implements Styleable {

	private LayerIcon icon;
	private Label lab;
	private ColorIcon arrow;

	private Runnable action;

	public ServerOption(Window owner, String text, String... iconLayers) {
		super(12);
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(10, 20, 12, 16));
		setCursor(Cursor.HAND);

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
		
		setFocusTraversable(true);
		
		icon = new LayerIcon(42, iconLayers);

		lab = new Label(owner, text);
		lab.setFont(new Font(16, FontWeight.BOLD));
		lab.setTransform(TextTransform.CAPITALIZE);

		arrow = new ColorIcon("expand", 16);
		arrow.setRotate(-90);

		getChildren().addAll(icon, lab, new ExpandingHSpace(), arrow);

		applyStyle(owner.getStyl());
	}

	public ServerOption setAction(Runnable action) {
		this.action = action;
		return this;
	}

	@Override
	public void applyStyle(Style style) {
		backgroundProperty()
				.bind(Bindings.when(hoverProperty()).then(Backgrounds.make(style.getBackgroundModifierHover(), 8.0))
						.otherwise(Backgrounds.make(style.getBackgroundPrimary(), 8.0)));
		
		NodeUtils.focusBorder(this, style.getTextLink(), style.getBackgroundModifierAccent(), 8.0);

		lab.setFill(style.getTextNormal());

		icon.setFill(1, style.getHeaderPrimary());
		icon.setFill(0, style.getAccent());

		arrow.setFill(style.getTextNormal());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
