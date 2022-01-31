package mesa.gui.controls.input;

import java.util.function.Consumer;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import mesa.gui.controls.Font;
import mesa.gui.controls.input.styles.ChannelInputStyle;
import mesa.gui.controls.popup.context.ContextMenu;
import mesa.gui.controls.popup.context.items.CheckMenuItem;
import mesa.gui.controls.popup.context.items.KeyedMenuItem;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class ChannelTextInput extends Input implements Styleable {
	private RichInput field;
	private HBox preField;

	private BooleanProperty notSelected;
	
	private ContextMenu menu;

	public ChannelTextInput(Window window, Font font, String key, boolean hidden) {
		super(key);
		
		setInputStyle(new ChannelInputStyle(this));

		field = new RichInput();
		field.setBackground(Background.EMPTY);
		field.setPadding(new Insets(10));
		field.setLineSpacing(4);

		field.focusedProperty().addListener((obs, ov, nv) -> inputStyle.focus(nv));

		notSelected = new SimpleBooleanProperty(true);
		notSelected.bind(Bindings.createBooleanBinding(()-> field.getSelection().getLength() == 0, field.selectionProperty()));

		value.bind(field.textProperty());

		preField = new HBox();
		preField.setAlignment(Pos.TOP_CENTER);
		preField.getChildren().add(field);

		HBox.setHgrow(field, Priority.ALWAYS);

		getChildren().add(preField);

		prepareMenu(window);

		setFont(font);
		
		setMinHeight(0);
		applyStyle(window.getStyl());
	}
	
	public void setAction(Runnable action) {
		field.setAction(action);
	}
	
	public void setRadius(double radius) {
		inputStyle.setRadius(radius);
	}

	public void setFieldPadding(Insets insets) {
		field.setPadding(insets);
	}
	
	public boolean isMenuShowing() {
		return menu.isShowing();
	}
	
	public ReadOnlyBooleanProperty menuShowingProperty() {
		return menu.showingProperty();
	}
	
	private void prepareMenu(Window window) {
		menu = new ContextMenu(window);

		CheckMenuItem stickers = new CheckMenuItem(menu, "stickers", null);
		stickers.setChecked(true);
		CheckMenuItem spellCheck = new CheckMenuItem(menu, "spellcheck", null);
		spellCheck.setChecked(true);
		KeyedMenuItem languages = new KeyedMenuItem(menu, "languages", null);

		KeyedMenuItem copy = new KeyedMenuItem(menu, "copy", null);
		copy.setAccelerator("ctrl+c");
		copy.setAction(this::copy);

		KeyedMenuItem cut = new KeyedMenuItem(menu, "cut", null);
		cut.setAccelerator("ctrl+x");
		cut.setAction(this::cut);

		KeyedMenuItem paste = new KeyedMenuItem(menu, "paste", null);
		paste.setAccelerator("ctrl+v");
		paste.setAction(this::paste);

		menu.addMenuItem(stickers);
		menu.separate();
		menu.addMenuItem(spellCheck);
		menu.addMenuItem(languages);
		menu.separate();
		menu.addMenuItem(copy);
		menu.addMenuItem(cut);
		menu.addMenuItem(paste);

		spellCheck.checkedProperty().addListener((obs, ov, nv) -> {
			if (nv.booleanValue()) {
				menu.enable(languages);
			} else {
				menu.disable(languages);
			}
		});

		Consumer<Boolean> checkSelection = nv -> {
			if (nv.booleanValue()) {
				menu.disable(cut);
				menu.disable(copy);
			} else {
				menu.enable(copy);
				menu.enable(cut);
			}
		};

		notSelected().addListener((obs, ov, nv) -> checkSelection.accept(nv));

		checkSelection.accept(notSelected().getValue());

		field.setOnContextMenuRequested(e -> menu.showPop(this, e));

		addEventFilter(MouseEvent.MOUSE_PRESSED, e -> menu.hide());
	}

	public void setFocusable(boolean focusable) {
		field.setFocusTraversable(focusable);
		if (!focusable) {
			setMouseTransparent(true);
		}
	}
	
	@Override
	public void requestFocus() {
		field.requestFocus();
	}

	public void addPostField(Node... nodes) {
		preField.getChildren().addAll(nodes);
	}

	public void addPreField(Node node) {
		preField.getChildren().add(0, node);
	}

	public void addPreField(Node... nodes) {
		for (Node node : nodes) {
			addPreField(node);
		}
	}

	public ChannelTextInput(Window window, Font font, String key) {
		this(window, font, key, false);
	}

	@Override
	public void setFont(Font font) {
		field.setFont(font);
	}

	@Override
	public boolean isFocus() {
		return field.isFocused();
	}
	

	public ReadOnlyBooleanProperty focusProperty() {
		return field.focusedProperty();
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

	public void copy() {
		field.copy();
	}

	public void cut() {
		field.cut();
	}

	public void paste() {
		field.paste();
	}

	public BooleanProperty notSelected() {
		return notSelected;
	}

	public void setPrompt(String string) {
		field.setPrompt(string);
	}

	@Override
	public void applyStyle(Style style) {
		inputStyle.applyStyle(style);

		field.setTextFill(style.getTextNormal());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
