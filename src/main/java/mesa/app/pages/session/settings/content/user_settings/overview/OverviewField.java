package mesa.app.pages.session.settings.content.user_settings.overview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Font;
import mesa.gui.controls.alert.Overlay;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class OverviewField extends HBox implements Styleable {
	private Label name;
	private HBox value;
	private HBox preEdit;

	protected Button edit;

	private ObjectProperty<Overlay> overlay;

	public OverviewField(Settings settings, String key) {
		setAlignment(Pos.CENTER);

		VBox left = new VBox(4);

		name = new Label(settings.getWindow(), key, new Font(12, FontWeight.BOLD));
		name.setTransform(TextTransform.UPPERCASE);

		value = new HBox();
		value.setAlignment(Pos.CENTER_LEFT);
		value.setMinHeight(22);
		value.setMaxHeight(22);

		left.getChildren().add(name);

		left.getChildren().add(value);

		preEdit = new HBox();
		preEdit.setAlignment(Pos.CENTER);

		edit = new Button(settings.getWindow(), "overview_edit", 3, 16, 32);
		edit.setFont(new Font(14, FontWeight.BOLD));
		
		overlay = new SimpleObjectProperty<>();
		
		edit.setAction(() -> {
			if (overlay.get() != null)
				overlay.get().show();
		});

		getChildren().addAll(left, new ExpandingHSpace(), preEdit, edit);

		applyStyle(settings.getWindow().getStyl());
	}

	public void setOverlay(Overlay overlay) {
		this.overlay.set(overlay);
	}

	public ObjectProperty<Overlay> overlayProperty() {
		return overlay;
	}

	public void setValue(Node... node) {
		value.getChildren().setAll(node);
	}

	public void addToPreEdit(Node... nodes) {
		preEdit.getChildren().addAll(nodes);
	}

	@Override
	public void applyStyle(Style style) {
		name.setFill(style.getInteractiveNormal());
		edit.setFill(style.getSecondaryButtonBack());
		edit.setTextFill(Color.WHITE);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
