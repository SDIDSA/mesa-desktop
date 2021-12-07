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
import mesa.gui.controls.Font;
import mesa.gui.controls.alert.Overlay;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public abstract class AbstractOverlay extends Overlay implements Styleable {
	private StackPane preRoot;
	private HBox bottom;
	private Button cancel;

	protected ColorIcon closeIcon;
	protected VBox root;
	protected Button done;
	protected VBox center;

	protected Form form;

	protected AbstractOverlay(SessionPage session, double width) {
		super(session);

		root = new VBox();

		preRoot = new StackPane();
		preRoot.setAlignment(Pos.TOP_RIGHT);
		preRoot.setMaxWidth(width);

		closeIcon = new ColorIcon("close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(this::hide);
		closeIcon.setCursor(Cursor.HAND);
		closeIcon.applyStyle(session.getWindow().getStyl());

		center = new VBox(16);
		center.setPadding(new Insets(0, 16, 16, 16));

		root.getChildren().add(center);
		
		root.setPickOnBounds(false);
		center.setPickOnBounds(false);

		preRoot.getChildren().addAll(closeIcon ,root);

		bottom = new HBox(8);
		bottom.setMaxWidth(width);
		bottom.setPadding(new Insets(16));

		cancel = new Button(session.getWindow(), "cancel", 3, 24, 38);
		cancel.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		cancel.setUlOnHover(true);
		cancel.setAction(this::hide);

		done = new Button(session.getWindow(), "done", 3, 24, 38);
		done.setFont(new Font(14, FontWeight.BOLD));

		bottom.getChildren().addAll(new ExpandingHSpace(), cancel, done);

		setContent(preRoot, bottom);

		form = new Form();
		form.setDefaultButton(done);
	}

	protected AbstractOverlay(SessionPage session) {
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

	public void setOnCancel(Runnable action) {
		Runnable onClose = () -> {
			hide();
			action.run();
		};
		closeIcon.setAction(onClose);
		cancel.setAction(onClose);
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
		preRoot.setBackground(Backgrounds.make(style.getBackgroundPrimary(), new CornerRadii(5, 5, 0, 0, false)));
		bottom.setBackground(Backgrounds.make(style.getBackgroundSecondary(), new CornerRadii(0, 0, 5, 5, false)));

		cancel.setFill(Color.TRANSPARENT);
		cancel.setTextFill(style.getLinkButtonText());

		done.setFill(style.getAccent());
		done.setTextFill(Color.WHITE);

		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getHeaderPrimary())
				.otherwise(style.getHeaderSecondary()));
	}
}
