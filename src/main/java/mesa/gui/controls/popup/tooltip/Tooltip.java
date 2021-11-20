package mesa.gui.controls.popup.tooltip;

import java.util.HashMap;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import mesa.gui.controls.Font;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.shape.Triangle;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class Tooltip extends PopupControl implements Styleable {
	public static final Direction UP = Direction.UP;
	public static final Direction RIGHT = Direction.RIGHT;
	public static final Direction DOWN = Direction.DOWN;
	public static final Direction LEFT = Direction.LEFT;

	private static DropShadow ds = new DropShadow(15, Color.gray(0, .25));

	protected Window owner;
	private Pane root;
	private Text text;

	private Direction direction;

	private StackPane content;
	private Triangle arr;

	private Timeline fadeIn;
	private Timeline fadeOut;

	private double offset;

	public Tooltip(Window window, String val, Direction direction, double offset) {
		this.owner = window;
		this.direction = direction;
		this.offset = offset;

		StackPane preroot = new StackPane();
		preroot.setPadding(new Insets(15));

		root = direction.toPane();
		root.setEffect(ds);
		root.setOpacity(0);
		root.setScaleX(.7);
		root.setScaleY(.7);

		content = new StackPane();
		content.setPadding(new Insets(8, 12, 8, 12));

		text = new Text(val);
		text.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 16).getFont());

		arr = new Triangle(10);
		switch (direction) {
		case LEFT:
			arr.setRotate(180);
			break;
		case UP:
			arr.setRotate(-90);
			arr.setTranslateY(-2.5);
			break;
		case DOWN:
			arr.setRotate(90);
			arr.setTranslateY(2.5);
			break;
		default:
			break;
		}

		content.getChildren().add(text);

		if (direction.isArrowFirst()) {
			root.getChildren().addAll(arr, content);
		} else {
			root.getChildren().addAll(content, arr);
		}

		fadeIn = new Timeline(
				new KeyFrame(Duration.seconds(.05), new KeyValue(root.opacityProperty(), 1.0, Interpolator.EASE_BOTH),
						new KeyValue(root.scaleXProperty(), 1.0, Interpolator.EASE_BOTH),
						new KeyValue(root.scaleYProperty(), 1.0, Interpolator.EASE_BOTH)));

		fadeOut = new Timeline(
				new KeyFrame(Duration.seconds(.05), new KeyValue(root.opacityProperty(), 0, Interpolator.EASE_BOTH),
						new KeyValue(root.scaleXProperty(), .7, Interpolator.EASE_BOTH),
						new KeyValue(root.scaleYProperty(), .7, Interpolator.EASE_BOTH)));

		fadeOut.setOnFinished(e -> hide());

		preroot.getChildren().add(root);
		
		getScene().setRoot(preroot);

		applyStyle(window.getStyl());
	}

	public void setFont(Font font) {
		text.setFont(font.getFont());
	}
	
	public void setOffset(double offset) {
		this.offset = offset;
	}

	public Tooltip(Window window, String val, Direction direction) {
		this(window, val, direction, 0);
	}

	public void setText(String txt) {
		text.setText(txt);
	}

	protected void showPop(Node node) {
		fadeOut.stop();
		Runnable adjust = () -> {
			Bounds bounds = node.getBoundsInLocal();
			Bounds screenBounds = node.localToScreen(bounds);
			
			double[] pos = calcPos(screenBounds);

			double px = pos[0];
			double py = pos[1];

			setX(px);
			setY(py);
			fadeIn.playFromStart();
		};
		if (isShowing()) {
			adjust.run();
		} else {
			setOnShown(e -> adjust.run());
			this.show(owner);
		}
	}
	
	private double[] calcPos(Bounds screenBounds) {
		double[] res = new double[2];
		
		double xHor = (direction.isArrowFirst() ? (screenBounds.getMaxX() + offset) : (screenBounds.getMinX() - offset));
		double x = direction.isHorizontal()
				? xHor
				: screenBounds.getCenterX();

		double yVer = (direction.isArrowFirst() ? (screenBounds.getMaxY() + offset) : (screenBounds.getMinY() - offset));
		double y = direction.isVertical()
				? yVer
				: screenBounds.getCenterY();

		double pxHor = (direction.isArrowFirst() ? 0 : getWidth());
		double pyVer = (direction.isArrowFirst() ? 0 : getHeight());
		
		res[0] = x - (direction.isHorizontal() ? pxHor : getWidth() / 2);
		res[1] = y - (direction.isVertical() ? pyVer : getHeight() / 2);

		return res;
	}

	public void fadeOut() {
		fadeIn.stop();
		fadeOut.playFromStart();
	}

	private static HashMap<Node, Installation> registered = new HashMap<>();

	public static void install(Node node, Tooltip tooltip) {
		Installation evs = registered.get(node);
		if (evs != null) {
			evs.uninstall();
		}
		evs = new Installation(node, tooltip);
		evs.install();
		registered.put(node, evs);
	}

	public static void install(Node node, Direction dir, String value, double offset, boolean key) {
		if (node.getScene() == null) {
			node.sceneProperty().addListener(new ChangeListener<Scene>() {
				@Override
				public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
					if (newValue != null) {
						Window w = (Window) newValue.getWindow();
						Tooltip tip = key ? new KeyedTooltip(w, value, dir, offset)
								: new Tooltip(w, value, dir, offset);
						install(node, tip);
						node.sceneProperty().removeListener(this);
					}
				}
			});
		} else {
			Window w = (Window) node.getScene().getWindow();
			Tooltip tip = key ? new KeyedTooltip(w, value, dir, offset) : new Tooltip(w, value, dir, offset);
			install(node, tip);
		}
	}

	public static void install(Node node, Direction dir, String value, boolean key) {
		install(node, dir, value, 0, key);
	}

	public static void install(Node node, Direction dir, String value, double offset) {
		install(node, dir, value, offset, false);
	}

	public static void install(Node node, Direction dir, String value) {
		install(node, dir, value, 0, false);
	}

	static class Installation {
		EventHandler<MouseEvent> onEnter;
		EventHandler<MouseEvent> onExit;
		Node node;

		public Installation(Node node, Tooltip tip) {
			this.node = node;
			this.onEnter = e -> tip.showPop(node);
			this.onExit = e -> tip.fadeOut();
		}

		public void install() {
			node.addEventFilter(MouseEvent.MOUSE_ENTERED, onEnter);
			node.addEventFilter(MouseEvent.MOUSE_EXITED, onExit);
			node.addEventFilter(MouseEvent.MOUSE_CLICKED, onExit);
		}

		public void uninstall() {
			node.removeEventFilter(MouseEvent.MOUSE_ENTERED, onEnter);
			node.removeEventFilter(MouseEvent.MOUSE_EXITED, onExit);
			node.removeEventFilter(MouseEvent.MOUSE_CLICKED, onExit);
		}
	}

	@Override
	public void applyStyle(Style style) {
		content.setBackground(Backgrounds.make(style.getBack5(), 5.0));
		text.setFill(style.getText2());
		arr.setFill(style.getBack5());
	}

}
