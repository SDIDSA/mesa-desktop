package mesa.gui.controls.alert;

import java.util.EnumMap;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import mesa.app.pages.Page;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class Alert extends Overlay implements Styleable {
	private StackPane preRoot;
	private HBox bottom;

	protected ColorIcon closeIcon;
	protected VBox root;

	private Label head;
	private Label body;

	private EnumMap<ButtonType, Runnable> actions;

	public Alert(Page page, AlertType type, double width) {
		super(page);

		preRoot = new StackPane();
		preRoot.setAlignment(Pos.TOP_RIGHT);
		preRoot.setMaxWidth(width);

		closeIcon = new ColorIcon("close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(this::hide);
		closeIcon.setCursor(Cursor.HAND);
		StackPane.setMargin(closeIcon, new Insets(10));

		root = new VBox();
		root.setPadding(new Insets(16));

		head = new Label(page.getWindow(), "", new Font(20, FontWeight.BOLD));
		VBox.setMargin(head, new Insets(0, 0, 16, 0));

		body = new Label(page.getWindow(), "", new Font(15));

		TextFlow preBody = new TextFlow(body);
		preBody.setLineSpacing(5);
		preBody.setMinHeight(60);

		root.getChildren().addAll(head, preBody);

		preRoot.getChildren().addAll(root, closeIcon);

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

		applyStyle(page.getWindow().getStyl());
	}

	public void addAction(ButtonType type, Runnable action) {
		actions.put(type, action);
	}
	
	public Alert(Page page, AlertType type) {
		this(page, type, 440);
	}

	public Alert(Page page) {
		this(page, AlertType.DEFAULT, 440);
	}

	public void setHead(String key) {
		head.setKey(key);
	}

	public void setBody(String key) {
		body.setKey(key);
	}

	@Override
	public void applyStyle(Style style) {
		preRoot.setBackground(Backgrounds.make(style.getBack1(), new CornerRadii(5, 5, 0, 0, false)));
		bottom.setBackground(Backgrounds.make(style.getBack2(), new CornerRadii(0, 0, 5, 5, false)));

		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getText1())
				.otherwise(style.getText1().deriveColor(0, 1, 1, .5)));

		head.setFill(style.getText1());
		body.setFill(style.getText2());
	}
}
