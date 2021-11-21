package mesa.app.pages.session.settings.content.user_settings.overlays;

import org.json.JSONArray;

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
import mesa.app.component.Form;
import mesa.app.pages.session.SessionPage;
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

public class BasicOverlay extends Overlay implements Styleable {
	private VBox root;
	private HBox bottom;
	private Button cancel;
	private ColorIcon closeIcon;

	protected Label head;
	protected Label subHead;
	
	protected Button done;
	protected VBox center;

	protected Form form;

	public BasicOverlay(SessionPage session, double width) {
		root = new VBox();
		root.setMaxWidth(width);

		StackPane preTop = new StackPane();
		preTop.setAlignment(Pos.TOP_RIGHT);
		preTop.setPadding(new Insets(16, 16, 16, 16));

		VBox top = new VBox(8);
		top.setPadding(new Insets(8, 0, 12, 0));
		top.setAlignment(Pos.CENTER);

		closeIcon = new ColorIcon("close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(this::hide);
		closeIcon.setCursor(Cursor.HAND);

		preTop.getChildren().addAll(top, closeIcon);

		head = new Label(session.getWindow(), "", new Font(24, FontWeight.BOLD));
		head.setTransform(TextTransform.CAPITALIZE_PHRASE);

		subHead = new Label(session.getWindow(), "", new Font(15));
		subHead.setTransform(TextTransform.CAPITALIZE_PHRASE);

		top.getChildren().addAll(head, subHead);

		center = new VBox(16);
		center.setPadding(new Insets(0, 16, 16, 16));

		root.getChildren().addAll(preTop, center);

		bottom = new HBox(8);
		bottom.setMaxWidth(width);
		bottom.setPadding(new Insets(16));

		cancel = new Button(session.getWindow(), "cancel", 3, 24, 38);
		cancel.setFont(new Font(14, FontWeight.BOLD));
		cancel.setUlOnHover(true);
		cancel.setAction(this::hide);

		done = new Button(session.getWindow(), "done", 3, 24, 38);
		done.setFont(new Font(14, FontWeight.BOLD));

		bottom.getChildren().addAll(new ExpandingHSpace(), cancel, done);

		setContent(root, bottom);

		form = new Form();
		form.setDefaultButton(done);

		applyStyle(session.getWindow().getStyl());
	}
	
	public BasicOverlay(SessionPage session) {
		this(session, 440);
	}

	public void applyErrors(JSONArray errors) {
		form.applyErrors(errors);
	}

	public BooleanProperty doneDisabled() {
		return done.disableProperty();
	}

	public void setAction(Runnable run) {
		done.setAction(() -> {
			if (form.check())
				run.run();
		});
	}

	public void startLoading() {
		done.startLoading();
	}

	public void stopLoading() {
		done.stopLoading();
	}

	@Override
	public void hide() {
		form.clearErrors();
		super.hide();
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

		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getText1())
				.otherwise(style.getText1().deriveColor(0, 1, 1, .5)));
	}
}
