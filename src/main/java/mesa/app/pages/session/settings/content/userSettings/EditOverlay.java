package mesa.app.pages.session.settings.content.userSettings;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.Overlay;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class EditOverlay extends Overlay implements Styleable {
	private VBox root;
	private Label head, subHead;
	private HBox bottom;
	private Button cancel;
	private ColorIcon closeIcon;
	
	protected Button done;
	protected VBox center;

	public EditOverlay(Settings settings, String edit_what) {
		root = new VBox();
		root.setMaxWidth(440);

		StackPane preTop = new StackPane();
		preTop.setAlignment(Pos.TOP_RIGHT);
		preTop.setPadding(new Insets(16, 16, 16, 16));

		VBox top = new VBox(8);
		top.setPadding(new Insets(8, 0, 12, 0));
		top.setAlignment(Pos.CENTER);

		closeIcon = new ColorIcon(settings.getWindow(), "close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(this::hide);
		closeIcon.setCursor(Cursor.HAND);
		
		preTop.getChildren().addAll(top, closeIcon);

		head = new Label(settings.getWindow(), "change_attr", new Font(24, FontWeight.BOLD));
		head.addParam(0, "&" + edit_what);
		head.setTransform(TextTransform.CAPITALIZE_PHRASE);

		subHead = new Label(settings.getWindow(), "enter_attr", new Font(15));
		subHead.addParam(0, "&" + edit_what);
		subHead.setTransform(TextTransform.CAPITALIZE_PHRASE);

		top.getChildren().addAll(head, subHead);

		center = new VBox(16);
		center.setPadding(new Insets(0, 16, 16, 16));

		root.getChildren().addAll(preTop, center);

		bottom = new HBox(8);
		bottom.setMaxWidth(440);
		bottom.setPadding(new Insets(16));

		cancel = new Button(settings.getWindow(), "cancel", 3, 24, 38);
		cancel.setFont(new Font(14, FontWeight.BOLD));
		cancel.setUlOnHover(true);
		cancel.setAction(this::hide);

		done = new Button(settings.getWindow(), "done", 3, 24, 38);
		done.setFont(new Font(14, FontWeight.BOLD));

		bottom.getChildren().addAll(new ExpandingHSpace(), cancel, done);

		setContent(root, bottom);

		applyStyle(settings.getWindow().getStyl());
	}

	public BooleanProperty doneDisabled() {
		return done.disableProperty();
	}

	public void setAction(Runnable run) {
		done.setAction(run);
	}

	public void startLoading() {
		done.startLoading();
	}

	public void stopLoading() {
		done.stopLoading();
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBack1(), new CornerRadii(5, 5, 0, 0, false)));
		bottom.setBackground(Backgrounds.make(style.getBack2(), new CornerRadii(0, 0, 5, 5, false)));
		head.setFill(style.getText1());
		subHead.setFill(style.getInteractiveNormal());

		cancel.setFill(Color.TRANSPARENT);
		cancel.setTextFill(style.getText1());

		done.setFill(style.getAccent());
		done.setTextFill(style.getText1());

		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getText1()).otherwise(style.getText1().deriveColor(0, 1, 1, .5)));
	}
}
