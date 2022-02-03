package mesa.app.pages.session.types.server.center;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.layer_icon.LayerIcon;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class NoTextChannel extends StackPane implements Styleable {

	private Label head;
	private MultiText body;
	
	private LayerIcon icon;
	
	public NoTextChannel(SessionPage session) {
		
		VBox content = new VBox();
		content.setMinWidth(0);
		content.setMaxWidth(440);
		content.setAlignment(Pos.CENTER);
		
		head = new Label(session.getWindow(), "No text channels", new Font(16, FontWeight.BOLD));
		head.setTransform(TextTransform.UPPERCASE);
		
		body = new MultiText(session.getWindow(), "You find yourself in a strange place. You don't have access to any text channels or there are none in this server.", new Font(15));
		body.center();
		
		VBox.setMargin(body, new Insets(12, 0, 0, 0));
		
		icon = new LayerIcon(256, "noChannel_l0", "noChannel_l1", "noChannel_l2");
		VBox.setMargin(icon, new Insets(24, 0, 64, 0));
		
		content.getChildren().addAll(icon, head, body);
		
		getChildren().add(content);
		
		applyStyle(session.getWindow().getStyl());
	}
	
	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBackgroundTertiary()));
		
		head.setFill(style.getTextMuted());
		body.setFill(style.getTextMuted());
		
		icon.setOpacity(.6);
		icon.setOpacity(0, .2);
		icon.setFill(0, style.getTextMuted());
		icon.setFill(1, style.getTextMuted());
		icon.setFill(2, style.getBackgroundTertiary());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
