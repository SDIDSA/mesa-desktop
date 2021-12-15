package mesa.app.component.input;

import java.util.List;
import java.util.function.Consumer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import mesa.app.utils.Colors;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.Input;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public abstract class InputField extends VBox implements Styleable {
	private Window window;

	private Label lab;
	private HBox input;

	private Label err;
	private HBox labs;

	private Text t;

	protected StringProperty value;

	protected InputField(Window window, String key, double width) {
		super(8);
		this.window = window;
		getStyleClass().addAll("field", key);

		value = new SimpleStringProperty("");

		labs = new HBox();
		labs.setAlignment(Pos.CENTER_LEFT);

		err = new Label(window, null, new Font(12, FontPosture.ITALIC));
		err.setFill(Colors.Error);

		t = new Text(" - ");
		t.setFill(Colors.Error);
		t.setFont(err.getFont());
		t.setOpacity(0);

		lab = new Label(window, key, new Font(12, FontWeight.BOLD));
		lab.setTransform(TextTransform.UPPERCASE);
		lab.setOpacity(.7);

		labs.getChildren().addAll(lab, t, err);

		setMinWidth(width);
		setMaxWidth(width);

		input = new HBox(15);

		getChildren().addAll(labs, input);
	}

	protected InputField(Window window, String key) {
		this(window, key, 200);
	}

	public StringProperty valueProperty() {
		return value;
	}

	private void forEach(Consumer<Input> consumer) {
		for (Node c : input.getChildren()) {
			if (c instanceof Input in) {
				consumer.accept(in);
			}
		}
	}

	public void addOnKeyPressed(Consumer<KeyCode> consumer) {
		forEach(in -> in.addEventFilter(KeyEvent.KEY_PRESSED, event -> consumer.accept(event.getCode())));
	}

	public void setError(String error, String plus) {
		t.setOpacity(1);
		lab.setFill(Colors.Error);
		if (plus != null) {
			err.addParam(0, plus);
		}
		err.setKey(error);

		forEach(in -> in.setBorder(Colors.Error, Colors.Error, Colors.Error));
	}

	public void setError(String string) {
		setError(string, null);
	}

	public void removeError() {
		t.setOpacity(0);
		err.setKey(null);
		applyStyle(window.getStyl());

		forEach(in -> in.applyStyle(window.getStyl()));
	}

	protected void addInput(Input in) {
		HBox.setHgrow(in, Priority.ALWAYS);
		input.getChildren().add(in);
	}

	protected void addInputs(Input... inputs) {
		for (Input in : inputs) {
			addInput(in);
		}
	}

	protected void addInputs(List<? extends Input> inputs) {
		for (Input in : inputs) {
			addInput(in);
		}
	}

	protected void addNode(Node n) {
		input.getChildren().add(n);
	}

	public void align(Pos alignment) {
		input.setAlignment(alignment);
	}

	@Override
	public void applyStyle(Style style) {
		lab.setFill(style.getHeaderSecondary());
	}
	
	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

	public String getValue() {
		return value.get();
	}

	public String getKey() {
		return lab.getKey();
	}

	public abstract void setValue(String string);

	/**
	 * Remove the error (if exists) from this field and clear the values of all
	 * children inputs using using {@link Input#clear()}
	 */
	public void clear() {
		removeError();
		forEach(Input::clear);
	}

	public String getErrorKey() {
		return err.getKey();
	}
}
