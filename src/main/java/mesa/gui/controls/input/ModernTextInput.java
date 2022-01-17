package mesa.gui.controls.input;

import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import mesa.gui.controls.Font;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class ModernTextInput extends ModernInput {
	private TextInputControl field;
	private HBox preField;

	private AtomicInteger caretPos;

	private BooleanProperty notSelected;
	private ObjectProperty<IndexRange> selection;

	public ModernTextInput(Window window, Font font, String key, boolean hidden) {
		super(key);

		field = hidden ? new PasswordField() : new TextField();
		field.setBackground(Background.EMPTY);
		field.setBorder(Border.EMPTY);
		field.setPadding(new Insets(10));

		caretPos = new AtomicInteger();
		field.caretPositionProperty().addListener((obs, ov, nv) -> {
			if (field.isFocused()) {
				caretPos.set(nv.intValue());
			}
		});

		notSelected = new SimpleBooleanProperty(true);
		selection = new SimpleObjectProperty<>(field.getSelection());
		field.selectionProperty().addListener((obs, ov, nv) -> {
			if (field.isFocused()) {
				selection.set(nv);
				notSelected.set(nv.getLength() == 0);
			}
		});

		value.bind(field.textProperty());

		preField = new HBox();
		preField.setAlignment(Pos.CENTER);
		preField.getChildren().add(field);

		HBox.setHgrow(field, Priority.ALWAYS);

		getChildren().add(preField);

		setFont(font);

		applyStyle(window.getStyl());
	}

	public void setPrompt(String promt) {
		field.setPromptText(promt);
	}

	public void addPostField(Node... nodes) {
		preField.getChildren().addAll(nodes);
	}

	public ModernTextInput(Window window, Font font, String key) {
		this(window, font, key, false);
	}

	public void positionCaret(int pos) {
		field.positionCaret(pos);
	}

	@Override
	public void setFont(Font font) {
		field.setFont(font.getFont());
	}

	@Override
	protected boolean isFocus() {
		return field.isFocused();
	}

	@Override
	public String getValue() {
		return field.getText();
	}

	@Override
	public void setValue(String value) {
		field.setText(value);
	}

	@Override
	public void clear() {
		setValue("");
	}

	@Override
	public void requestFocus() {
		int pos = caretPos.get();
		field.requestFocus();
		field.deselect();
		field.positionCaret(pos);
	}

	@Override
	public boolean supportsContextMenu() {
		return true;
	}

	@Override
	public Node contextMenuNode() {
		return field;
	}

	@Override
	public void copy() {
		String selected = field.getText().substring(selection.get().getStart(), selection.get().getEnd());
		ClipboardContent cpc = new ClipboardContent();
		cpc.putString(selected);
		Clipboard.getSystemClipboard().setContent(cpc);
		caretPos.set(selection.get().getEnd());
	}

	@Override
	public void cut() {
		copy();
		field.replaceText(selection.get(), "");
		caretPos.set(selection.get().getStart());
		
		selection.set(field.getSelection());
		notSelected.set(true);
	}

	@Override
	public void paste() {
		if(notSelected.get()) {
			field.positionCaret(caretPos.getAndAdd(Clipboard.getSystemClipboard().getString().length()));
			field.paste();
		}else {
			field.replaceText(selection.get(), Clipboard.getSystemClipboard().getString());
		}
		
		selection.set(field.getSelection());
		notSelected.set(true);
	}

	@Override
	public BooleanProperty notSelected() {
		return notSelected;
	}

	@Override
	public void applyStyle(Style style) {
		super.applyStyle(style);

		Color tx = style.getTextNormal();
		field.setStyle("-fx-text-fill: " + Styleable.colorToCss(tx) + ";-fx-prompt-text-fill: "
				+ Styleable.colorToCss(tx.deriveColor(0, 1, 1, .3))
				+ ";-fx-background-color:transparent;-fx-text-box-border: transparent;");
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
