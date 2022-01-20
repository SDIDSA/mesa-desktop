package mesa.gui.controls.popup.tooltip;

import java.util.HashMap;

import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.transform.Scale;
import mesa.gui.controls.Font;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.shape.Triangle;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class Tooltip extends StackPane implements Styleable {
	public static final Direction UP = Direction.UP;
	public static final Direction RIGHT = Direction.RIGHT;
	public static final Direction DOWN = Direction.DOWN;
	public static final Direction LEFT = Direction.LEFT;

	private static final double SCALED = .7;

	protected Window owner;
	private Pane root;
	private Text text;

	private Direction direction;

	private StackPane content;
	private Triangle arr;

	private Timeline fadeIn;
	private Timeline fadeOut;

	private Runnable onShown;

	private Scale scale;

	private double offset;

	private boolean showing = false;

	public Tooltip(Window window, String val, Direction direction, double offset) {
		this.owner = window;
		this.direction = direction;
		this.offset = offset;

		setAlignment(Pos.CENTER_LEFT);
		StackPane.setAlignment(this, Pos.TOP_LEFT);

		setMouseTransparent(true);

		DropShadow ds = new DropShadow(15, Color.gray(0, .15));

		setEffect(ds);
		setOpacity(0);

		root = direction.toPane();

		setCacheHint(CacheHint.SPEED);

		scale = new Scale(SCALED, SCALED);

		content = new StackPane();
		content.setPadding(new Insets(8, 12, 8, 12));
		content.setAlignment(Pos.CENTER_LEFT);

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

		content.maxWidthProperty()
				.bind(Bindings.createDoubleBinding(() -> text.getBoundsInLocal().getWidth()
						+ content.getPadding().getLeft() + content.getPadding().getRight(),
						text.boundsInLocalProperty()));

		if (direction.isArrowFirst()) {
			root.getChildren().addAll(arr, content);
		} else {
			root.getChildren().addAll(content, arr);
		}

		fadeIn = new Timeline(
				new KeyFrame(Duration.seconds(.1), new KeyValue(opacityProperty(), 1.0, SplineInterpolator.OVERSHOOT),
						new KeyValue(scale.xProperty(), 1.0, SplineInterpolator.OVERSHOOT),
						new KeyValue(scale.yProperty(), 1.0, SplineInterpolator.OVERSHOOT)));

		fadeIn.setOnFinished(e -> setCache(false));

		fadeOut = new Timeline(
				new KeyFrame(Duration.seconds(.05), new KeyValue(opacityProperty(), 0, Interpolator.EASE_BOTH),
						new KeyValue(scale.xProperty(), SCALED, Interpolator.EASE_BOTH),
						new KeyValue(scale.yProperty(), SCALED, Interpolator.EASE_BOTH)));

		fadeOut.setOnFinished(e -> hide());

		setMinSize(0, 0);

		maxHeightProperty().bind(root.heightProperty());
		minHeightProperty().bind(root.heightProperty());
		maxWidthProperty().bind(root.widthProperty());
		minWidthProperty().bind(root.widthProperty());

		if (direction == RIGHT) {
			((HBox) root).setAlignment(Pos.CENTER_LEFT);
		}
		if (direction == LEFT) {
			((HBox) root).setAlignment(Pos.CENTER_RIGHT);
		}
		if (direction == UP) {
			((VBox) root).setAlignment(Pos.BOTTOM_CENTER);
		}
		if (direction == DOWN) {
			((VBox) root).setAlignment(Pos.TOP_CENTER);
		}

		getChildren().add(root);

		root.widthProperty().addListener((obs, ov, nv) -> {
			if (node != null) {
				position(node);
			}
		});

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

	private Node node;

	private void position(Node node) {
		this.node = node;
		double[] pos = direction.calcPos(this, node, offset);

		scale.setPivotX(getWidth() * direction.pivotX());
		scale.setPivotY(getHeight() * direction.pivotY());

		setTranslateX(pos[0]);
		setTranslateY(pos[1]);
	}

	protected void showPop(Node node) {
		fadeOut.stop();
		Runnable adjust = () -> {
			getTransforms().clear();
			position(node);
			getTransforms().add(scale);
			setCache(true);
			fadeIn.playFromStart();
		};
		if (showing) {
			adjust.run();
		} else {
			setOnShown(adjust);
			this.show(owner.getRoot());
		}
	}

	protected void hide() {
		if (getParent() != null) {
			setCache(false);
			toBack();
			((Pane) getParent()).getChildren().remove(this);
			showing = false;
		}
	}

	protected void setOnShown(Runnable runnable) {
		this.onShown = runnable;
	}

	protected void show(Pane parent) {
		parent.getChildren().add(this);
		showing = true;
		if (onShown != null) {
			if (root.isNeedsLayout()) {
				root.needsLayoutProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> obs, Boolean ov, Boolean nv) {
						if (getWidth() != 0) {
							onShown.run();
							root.needsLayoutProperty().removeListener(this);
						}
					}
				});
			} else {
				onShown.run();
			}
		}
	}

	public void fadeOut() {
		fadeIn.stop();
		if (fadeOut.getStatus() != Status.RUNNING) {
			fadeOut.playFromStart();
		}
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

	public static void clear() {
		registered.clear();
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
		ChangeListener<Boolean> onFocusChange;

		Node node;

		public Installation(Node node, Tooltip tip) {
			this.node = node;
			this.onEnter = e -> tip.showPop(node);
			this.onExit = e -> tip.fadeOut();
			onFocusChange = (obs, ov, nv) -> {
				if (nv.booleanValue()) {
					tip.showPop(node);
				} else {
					tip.fadeOut();
				}
			};
		}

		public void install() {
			node.addEventFilter(MouseEvent.MOUSE_ENTERED, onEnter);
			node.addEventFilter(MouseEvent.MOUSE_EXITED, onExit);
			node.addEventFilter(MouseEvent.MOUSE_CLICKED, onExit);
			node.focusedProperty().addListener(onFocusChange);
		}

		public void uninstall() {
			node.removeEventFilter(MouseEvent.MOUSE_ENTERED, onEnter);
			node.removeEventFilter(MouseEvent.MOUSE_EXITED, onExit);
			node.removeEventFilter(MouseEvent.MOUSE_CLICKED, onExit);
			node.focusedProperty().removeListener(onFocusChange);
		}
	}

	public Window getOwner() {
		return owner;
	}

	public double width() {
		return root.getWidth();
	}

	@Override
	public void applyStyle(Style style) {
		content.setBackground(Backgrounds.make(style.getBackgroundFloating(), 5.0));
		text.setFill(style.getTextNormal());
		arr.setFill(style.getBackgroundFloating());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
