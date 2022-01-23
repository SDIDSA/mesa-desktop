package mesa.gui.controls.popup.context;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.popup.context.items.MenuItem;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class ContextMenu extends PopupControl implements Styleable {
	protected Window owner;
	protected VBox root;
	protected ArrayList<MenuItem> items;
	protected HashMap<MenuItem, Node> after;

	private ArrayList<StackPane> separators;

	protected MenuItem selected;

	public ContextMenu(Window window) {
		this.owner = window;

		items = new ArrayList<>();
		after = new HashMap<>();
		separators = new ArrayList<>();

		root = new VBox(3);
		root.setPadding(new Insets(8));
		root.setMinWidth(188);
		root.setMaxWidth(320);

		setAutoHide(true);

		StackPane preroot = new StackPane();

		DropShadow ds = new DropShadow(15, Color.gray(0, .25));

		StackPane clipped = new StackPane();
		clipped.setEffect(ds);

		Rectangle clip = new Rectangle();
		clip.setArcHeight(4);
		clip.setArcWidth(4);
		clip.widthProperty().bind(root.widthProperty());
		clip.heightProperty().bind(root.heightProperty());

		root.setClip(clip);
		clipped.getChildren().add(root);

		preroot.getChildren().add(clipped);
		getScene().setRoot(preroot);

		root.setOnMouseExited(e -> {
			if (selected != null) {
				selected.setActive(false);
				selected = null;
			}
		});

		getScene().setOnKeyPressed(this::handlePress);
		getScene().setOnKeyReleased(this::handleRelease);

		setOnHiding(e -> {
			if (selected != null) {
				selected.setActive(false);
				selected = null;
			}
		});

		applyStyle(window.getStyl());
	}

	private void handleRelease(KeyEvent e) {
		if (checkForAccelerator(e)) {
			return;
		}
		switch (e.getCode()) {
		case ENTER, SPACE: {
			if (selected != null) {
				selected.fire();
			}
		}
			break;
		case ESCAPE:
			this.hide();
			break;
		default:
			break;
		}
		e.consume();
	}

	private void handlePress(KeyEvent e) {
		switch (e.getCode()) {
		case UP: {
			up();
		}
			break;
		case DOWN: {
			down();
		}
			break;
		default:
			break;
		}
		e.consume();
	}

	private boolean checkForAccelerator(KeyEvent e) {
		for (MenuItem item : items) {
			KeyCombination accelerator = item.getAccelerator();
			if (accelerator != null && accelerator.match(e) && !item.isDisabled()) {
				item.fire();
				return true;
			}
		}

		return false;
	}

	private void up() {
		MenuItem nextItem = selected;

		int i = (nextItem == null ? items.size() - 1 : (items.indexOf(nextItem) - 1 + items.size()) % items.size());
		nextItem = items.get(i);

		while (nextItem == null || nextItem.isDisabled()) {
			i = (nextItem == null ? items.size() - 1 : (items.indexOf(nextItem) - 1 + items.size()) % items.size());
			nextItem = items.get(i);
		}

		select(nextItem);
	}

	public void down() {
		MenuItem nextItem = selected;

		int i = (nextItem == null ? 0 : (items.indexOf(nextItem) + 1) % items.size());
		nextItem = items.get(i);

		while (nextItem == null || nextItem.isDisabled()) {
			i = (nextItem == null ? 0 : (items.indexOf(nextItem) + 1) % items.size());
			nextItem = items.get(i);
		}

		select(nextItem);
	}

	public void install(Node node) {
		node.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				setX(e.getScreenX() - 15);
				setY(e.getScreenY() - 15);
				show(owner);
			}
		});
	}

	public void addMenuItem(String item, Runnable onAction, Color fill, boolean keyed) {
		MenuItem i = new MenuItem(this, item, fill, keyed);
		i.setAction(onAction);
		addMenuItem(i);
	}

	public void addMenuItem(String item, Runnable onAction, Color fill) {
		addMenuItem(item, onAction, fill, true);
	}

	public void addMenuItem(String item, Color fill) {
		addMenuItem(item, null, fill);
	}

	public void addMenuItem(String item, Runnable onAction) {
		addMenuItem(item, onAction, null);
	}

	public void addMenuItem(String item) {
		addMenuItem(item, null, null);
	}

	public void addMenuItem(MenuItem i) {
		i.setOnMouseClicked(e -> i.fire());
		i.setOnMouseEntered(e -> select(i));
		root.getChildren().add(i);
		items.add(i);
	}

	public void disable(MenuItem item) {
		item.setDisable(true);
		after.put(item, root.getChildren().get(root.getChildren().indexOf(item) - 1));
		root.getChildren().remove(item);
	}

	public void enable(MenuItem item) {
		item.setDisable(false);
		root.getChildren().add(root.getChildren().indexOf(after.get(item)) + 1, item);
	}

	public void separate() {
		StackPane sep = new StackPane();
		sep.setMinHeight(1);
		sep.setMaxHeight(1);

		StackPane preSep = new StackPane(sep);
		preSep.setPadding(new Insets(2, 4, 2, 4));

		root.getChildren().add(preSep);
		separators.add(sep);

		applyStyle(owner.getStyl().get());
	}

	private void select(MenuItem item) {
		boolean select = selected != item;
		boolean deselect = select && selected != null;

		if (deselect) {
			selected.setActive(false);
		}

		if (select) {
			item.setActive(true);
			selected = item;
		}
	}

	public void showPop(Node node, Direction direction, int offset) {
		setOnShown(e -> {
			Bounds bounds = node.getBoundsInLocal();
			Bounds screenBounds = node.localToScreen(bounds);
			double px = 0;
			double py = 0;

			if (direction == null) {
				px = screenBounds.getMaxX() - 15 + offset;
				py = screenBounds.getMinY() - 15;
			} else {
				double[] pos = direction.calcPos(this, node, offset);

				px = pos[0];
				py = pos[1];
			}

			setX(px);
			setY(py);
		});
		this.show(owner);
	}

	public void showPop(Node node, Direction dir) {
		showPop(node, dir, 0);
	}

	public void showPop(Node node, int offset) {
		showPop(node, null, offset);
	}

	public void showPop(Node node) {
		showPop(node, null, 0);
	}

	public void showPop(Node node, ContextMenuEvent event) {
		setOnShown(e -> node.getScene().getRoot().requestFocus());

		setOnHidden(e -> node.requestFocus());

		if (event.isKeyboardTrigger()) {
			Bounds bounds = node.getBoundsInLocal();
			Bounds screenBounds = node.localToScreen(bounds);
			double px = screenBounds.getMinX();
			double py = screenBounds.getMaxY() + 5;

			this.show(node, px, py);
		} else {
			this.show(node, event.getScreenX(), event.getScreenY());
		}

	}

	public Window getOwner() {
		return owner;
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBackgroundFloating(), 5.0));
		root.setBorder(Borders.make(style.getBackgroundFloating(), 4.0));

		if (!separators.isEmpty()) {
			Background sepBac = Backgrounds.make(style.getBackgroundModifierAccent());
			for (StackPane sep : separators) {
				sep.setBackground(sepBac);
			}
		}
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
