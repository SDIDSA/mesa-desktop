package mesa.app.pages.session.content.create_server.pages;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.app.utils.Colors;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class ExploreJoin extends HBox implements Styleable {

	private ColorIcon icon;
	private Label lab;
	private Label subLab;
	private ColorIcon arrow;

	private ArrayList<Runnable> actions;

	public ExploreJoin(Window owner) {
		super(12);
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(12));
		setCursor(Cursor.HAND);

		actions = new ArrayList<>();

		setOnMouseClicked(e-> actions.forEach(Runnable::run));
		setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.SPACE)) {
				actions.forEach(Runnable::run);
			}
		});

		setFocusTraversable(true);

		icon = new ColorIcon("explore", 40);

		lab = new Label(owner, "dont_have_invite", new Font(15, FontWeight.BOLD));
		subLab = new Label(owner, "check_server_discovery", new Font(12));
		subLab.setOpacity(.8);

		arrow = new ColorIcon("expand", 16);
		arrow.setRotate(-90);

		VBox labels = new VBox(4);
		labels.setAlignment(Pos.CENTER_LEFT);
		
		labels.getChildren().addAll(lab, subLab);
		
		getChildren().addAll(icon, labels, new ExpandingHSpace(), arrow);

		applyStyle(owner.getStyl());
	}

	public ExploreJoin addAction(Runnable action) {
		actions.add(action);
		return this;
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBackgroundSecondary(), 8.0));

		NodeUtils.focusBorder(this, style.getTextLink(), Color.TRANSPARENT, 8.0);

		lab.setFill(style.getTextNormal());
		subLab.setFill(style.getTextNormal());

		icon.setFill(Colors.GREEN);

		arrow.setFill(style.getTextNormal());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
