package mesa.gui.controls.alert;

import java.util.EnumMap;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mesa.app.pages.Page;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class Alert extends Overlay implements Styleable {
	private StackPane preRoot;
	private HBox bottom;

	protected ColorIcon closeIcon;
	protected VBox root;

	private Label head;
	private MultiText body;

	private EnumMap<ButtonType, Runnable> actions;

	public Alert(Pane owner, Window window , AlertType type, double width) {
		super(owner, window);
		preRoot = new StackPane();
		preRoot.setAlignment(Pos.TOP_RIGHT);
		preRoot.setMaxWidth(width);

		closeIcon = new ColorIcon("close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(this::hide);
		closeIcon.setCursor(Cursor.HAND);
		closeIcon.applyStyle(window.getStyl());
		StackPane.setMargin(closeIcon, new Insets(10));

		root = new VBox();
		root.setPadding(new Insets(16));
		root.setPickOnBounds(false);

		head = new Label(window, "", new Font(Font.DEFAULT_FAMILY_MEDIUM, 20));
		head.setMouseTransparent(true);
		VBox.setMargin(head, new Insets(0, 0, 16, 0));

		body = new MultiText(window);
		body.setLineSpacing(5);
		body.setMinHeight(60);

		root.getChildren().addAll(head, body);

		preRoot.getChildren().addAll(closeIcon, root);

		bottom = new HBox(8);
		bottom.setMaxWidth(width);
		bottom.setPadding(new Insets(16));

		bottom.getChildren().add(new ExpandingHSpace());

		actions = new EnumMap<>(ButtonType.class);
		for (ButtonType buttonType : type.getButtons()) {
			AlertButton button = new AlertButton(this, buttonType);
			button.setAction(() -> {
				Runnable action = actions.get(buttonType);
				if (action == null) {
					hide();
				} else {
					action.run();
				}
			});
			bottom.getChildren().add(button);
		}

		setContent(preRoot, bottom);

		applyStyle(window.getStyl());
	}
	
	public void addToBody(Node...node) {
		root.getChildren().addAll(node);
	}
	
	public Alert(Page page, AlertType type, double width) {
		this(page, page.getWindow(), type, width);
	}
	
	public Alert(Pane owner, Window window, AlertType type) {
		this(owner, window, type, 440);
	}
	
	public Alert(Page page, AlertType type) {
		this(page, type, 440);
	}

	public Alert(Page page) {
		this(page, AlertType.DEFAULT, 440);
	}

	public void addAction(ButtonType type, Runnable action) {
		actions.put(type, action);
	}

	public void setHead(String key) {
		head.setKey(key);
	}

	public void addLabel(String key) {
		body.addLabel(key, new Font(15));
	}
	
	public void addLink(String key) {
		body.addLink(key, new Font(15));
	}
	
	public void setBodyAction(int index, Runnable action) {
		body.setAction(index, action);
	}
	
	public void centerBody() {
		body.center();
	}

	@Override
	public void applyStyle(Style style) {
		preRoot.setBackground(Backgrounds.make(style.getBackgroundPrimary(), new CornerRadii(5, 5, 0, 0, false)));
		bottom.setBackground(Backgrounds.make(style.getBackgroundSecondary(), new CornerRadii(0, 0, 5, 5, false)));

		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getHeaderPrimary())
				.otherwise(style.getHeaderSecondary()));

		head.setFill(style.getHeaderPrimary());
		body.setFill(style.getTextNormal());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
