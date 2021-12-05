package mesa.gui.controls.label;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import mesa.gui.controls.Font;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class Link extends StackPane implements Styleable {
	private Label label;

	private Runnable action;

	public Link(Window window, String key, Font font) {
		super();
		getStyleClass().addAll("link", key);

		label = new Label(window, key, font);

		label.underlineProperty().bind(hoverProperty());
		getChildren().add(label);

		prefWidthProperty()
				.bind(Bindings.createDoubleBinding(() -> label.getBoundsInLocal().getWidth() + (isFocused() ? 8 : 0),
						label.boundsInLocalProperty(), focusedProperty()));

		prefHeightProperty().bind(Bindings.createDoubleBinding(() -> label.getBoundsInLocal().getHeight() + 4,
				label.boundsInLocalProperty(), focusedProperty()));

		setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
		setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

		setFocusTraversable(true);

		setOnMouseClicked(this::fire);
		setOnKeyPressed(this::fire);

		setCursor(Cursor.HAND);

		applyStyle(window.getStyl());
	}

	private void fire(MouseEvent dismiss) {
		fire();
	}

	private void fire(KeyEvent e) {
		if (e.getCode().equals(KeyCode.SPACE)) {
			fire();
		}
	}

	public void fire() {
		if (action != null) {
			action.run();
		}
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

	public void setKey(String key) {
		label.setKey(key);
	}

	public void setFont(Font font) {
		label.setFont(font);
	}

	public Link(Window window, String key) {
		this(window, key, Font.DEFAULT);
	}

	@Override
	public void applyStyle(Style style) {
		label.setFill(style.getTextLink());

		Border unfocused = Borders.make(Color.TRANSPARENT);
		Border focused = Borders.make(style.getTextLink(), 4.0);

		borderProperty().bind(Bindings.when(focusedProperty()).then(focused).otherwise(unfocused));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
