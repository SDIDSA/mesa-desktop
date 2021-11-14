package mesa.gui.controls.overlay;

import org.json.JSONArray;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.app.component.Form;
import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
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

	Button cancel, done;

	private TextInputField field, password;

	private ColorIcon closeIcon;

	private Form form;

	public EditOverlay(Settings settings, String edit_what) {
		root = new VBox();
		root.setMaxWidth(440);

		StackPane preTop = new StackPane();
		preTop.setAlignment(Pos.TOP_RIGHT);
		preTop.setPadding(new Insets(16, 16, 16, 16));

		VBox top = new VBox(8);
		top.setPadding(new Insets(8, 0, 12, 0));
		top.setAlignment(Pos.CENTER);

		preTop.getChildren().add(top);

		head = new Label(settings.getSession().getWindow(), "change_attr", new Font(24, FontWeight.BOLD));
		head.addParam(0, "&" + edit_what);
		head.setTransform(TextTransform.CAPITALIZE_PHRASE);

		subHead = new Label(settings.getSession().getWindow(), "enter_attr", new Font(15));
		subHead.addParam(0, "&" + edit_what);
		subHead.setTransform(TextTransform.CAPITALIZE_PHRASE);

		top.getChildren().addAll(head, subHead);

		VBox center = new VBox(16);
		center.setPadding(new Insets(0, 16, 16, 16));

		field = new TextInputField(settings.getSession().getWindow(), edit_what, 408);
		password = new TextInputField(settings.getSession().getWindow(), "current_password", 408, true);

		center.getChildren().addAll(field, password);

		form = NodeUtils.getForm(center);

		root.getChildren().addAll(preTop, center);

		bottom = new HBox(8);
		bottom.setMaxWidth(440);
		bottom.setPadding(new Insets(16));

		cancel = new Button(settings.getSession().getWindow(), "cancel", 3, 96, 38);
		cancel.setFont(new Font(14, FontWeight.BOLD));
		cancel.setUlOnHover(true);
		cancel.setAction(this::hide);

		closeIcon = new ColorIcon(settings.getSession().getWindow(), "close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(this::hide);
		closeIcon.setCursor(Cursor.HAND);
		
		preTop.getChildren().add(closeIcon);

		done = new Button(settings.getSession().getWindow(), "done", 3, 96, 38);
		done.setFont(new Font(14, FontWeight.BOLD));
		
		form.setDefaultButton(done);

		bottom.getChildren().addAll(new ExpandingHSpace(), cancel, done);

		setContent(root, bottom);

		addOnShown(field::requestFocus);

		applyStyle(settings.getSession().getWindow().getStyl());
	}

	public String getValue() {
		return field.getValue();
	}

	public String getPassword() {
		return password.getValue();
	}

	public StringProperty valueProperty() {
		return field.valueProperty();
	}

	public BooleanProperty doneDisabled() {
		return done.disableProperty();
	}

	public void setAction(Runnable run) {
		done.setAction(run);
	}

	public boolean checkForm() {
		return form.check();
	}

	public void applyErrors(JSONArray errors) {
		form.applyErrors(errors);
	}

	public void startLoading() {
		done.startLoading();
	}

	public void stopLoading() {
		done.stopLoading();
	}

	public void addPostField(Node... nodes) {
		field.addPostField(nodes);
	}

	public void setValue(String val) {
		field.setValue(val);
	}

	@Override
	public void hide() {
		form.clearErrors();
		password.clear();
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

		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getText1()).otherwise(style.getText1().deriveColor(0, 1, 1, .5)));
	}
}
