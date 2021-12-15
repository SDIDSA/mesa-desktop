package mesa.gui.controls.popup.context;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mesa.gui.controls.popup.Direction;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class ContextMenu extends PopupControl implements Styleable {
	protected Window owner;
	protected VBox root;
	protected ArrayList<ContextMenuItem> items;

	private ArrayList<StackPane> separators;

	protected ContextMenuItem selected;

	public ContextMenu(Window window) {
		this.owner = window;
		items = new ArrayList<>();
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

		getScene().setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP: {
				int i = (selected == null ? items.size() - 1
						: (items.indexOf(selected) - 1 + items.size()) % items.size());
				select(items.get(i));
			}
				break;
			case DOWN: {
				int i = (selected == null ? 0 : (items.indexOf(selected) + 1) % items.size());
				select(items.get(i));
			}
				break;
			case SPACE: {
				if (selected != null) {
					selected.fire();
				}
			}
				break;
			default:
				break;
			}

			e.consume();
		});

		applyStyle(window.getStyl());
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
		ContextMenuItem i = new ContextMenuItem(this, item, fill, keyed);
		i.setAction(onAction);

		i.setOnMouseClicked(e -> i.fire());

		i.setOnMouseEntered(e -> select(i));
		root.getChildren().add(i);
		items.add(i);
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

	private void select(ContextMenuItem item) {
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
