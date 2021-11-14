package mesa.app.pages.session.settings.content.userSettings;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.overlay.Overlay;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class OverviewField extends HBox implements Styleable {
	private Label name;
	private HBox value;
	private HBox preEdit;
	private Button edit;
	
	private Overlay editOver;

	public OverviewField(Settings settings, String key) {
		setAlignment(Pos.CENTER);

		VBox left = new VBox(4);

		name = new Label(settings.getSession().getWindow(), key, new Font(12, FontWeight.BOLD));
		name.setTransform(TextTransform.UPPERCASE);

		value = new HBox();
		value.setAlignment(Pos.CENTER_LEFT);
		value.setMinHeight(22);
		value.setMaxHeight(22);

		left.getChildren().add(name);

		left.getChildren().add(value);

		preEdit = new HBox();
		preEdit.setAlignment(Pos.CENTER);

		edit = new Button(settings.getSession().getWindow(), "overview_edit", 3, 60, 32);
		edit.setFont(new Font(14, FontWeight.BOLD));

		getChildren().addAll(left, new ExpandingHSpace(), preEdit, edit);
		
		edit.setAction(()-> {
			if(editOver != null) {
				editOver.show(settings.getSession());
			}
		});
		
		applyStyle(settings.getSession().getWindow().getStyl());
	}
	
	public void setEditOver(Overlay editOver) {
		this.editOver = editOver;
	}

	public void addToValue(Node... node) {
		value.getChildren().addAll(node);
	}
	
	public void addToPreEdit(Node...nodes) {
		preEdit.getChildren().addAll(nodes);
	}

	@Override
	public void applyStyle(Style style) {
		name.setFill(style.getInteractiveNormal());
		edit.setFill(style == Style.DARK ? Color.web("#4f545c") : Color.TRANSPARENT);
		edit.setTextFill(style.getText1());
	}
}
