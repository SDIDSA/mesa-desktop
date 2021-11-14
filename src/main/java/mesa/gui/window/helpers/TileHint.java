package mesa.gui.window.helpers;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.window.Window;

public class TileHint extends Stage {
	private static final int padding = 10;
	private static Rectangle SS;

	private DoubleProperty opac;

	private Timeline show, hide;

	private Tile tile;

	public TileHint(Window owner) {
		initOwner(owner);
		if (SS == null) {
			SS = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		}

		initStyle(StageStyle.TRANSPARENT);

		StackPane root = new StackPane();
		root.setBackground(Backgrounds.make(Color.gray(0.7, .1)));
		root.setBorder(Borders.make(Color.LIGHTGRAY));

		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		setScene(scene);

		setOnShown(e -> {
			owner.setAlwaysOnTop(true);
		});
		
		setOnHidden(e-> {
			owner.setAlwaysOnTop(false);
		});

		opac = new SimpleDoubleProperty(0);
		opac.addListener((obs, ov, nv) -> {
			setOpacity(nv.doubleValue());
		});

		show = new Timeline(60.0, new KeyFrame(Duration.seconds(.1), new KeyValue(opac, 1)));
		hide = new Timeline(60.0, new KeyFrame(Duration.seconds(.1), new KeyValue(opac, 0)));
	}

	public void show(State state) {
		Runnable run = () -> {
			tile = tileForState(state);
			Rectangle rect = tile.tile;
			setX(rect.x + padding);
			setY(rect.y + padding);
			setWidth(rect.width - padding * 2);
			setHeight(rect.height - padding * 2);

			if (!isShowing())
				super.show();

			if (show.getStatus().equals(Status.RUNNING)) {
				return;
			}
			show.playFromStart();
		};

		if (hide.getStatus().equals(Status.RUNNING)) {
			hide.stop();
		}
		run.run();
	}
	
	public static Tile tileForState(State state) {
		double x = 0, y = 0, w = 0, h = 0;
		double cx = SS.getCenterX();
		double cy = SS.getCenterY();
		double mw = SS.getWidth();
		double mh = SS.getHeight();
		switch (state) {
		case N:
			w = mw;
			h = mh;
			break;
		case E:
			x = cx;
			w = mw / 2;
			h = mh;
			break;
		case W:
			w = mw / 2;
			h = mh;
			break;
		case NE:
			w = mw / 2;
			h = mh / 2;
			x = cx;
			break;
		case NW:
			w = mw / 2;
			h = mh / 2;
			break;
		case SE:
			w = mw / 2;
			h = mh / 2;
			x = cx;
			y = cy;
			break;
		case SW:
			w = mw / 2;
			h = mh / 2;
			y = cy;
			break;
		default:
			break;
		}

		return new Tile((int) x, (int) y, (int) w, (int) h, state != State.N);
	}

	public State getState(double x, double y) {
		return State.StateForCoords(x, y, SS.getWidth(), SS.getHeight(), 100, 15);
	}

	public void hide() {
		tile = null;
		hide.setOnFinished(e -> {
			super.hide();
		});
		if (!isShowing()) {
			return;
		}
		if (hide.getStatus().equals(Status.RUNNING)) {
			return;
		}
		hide.playFromStart();
	}

	public Tile getTile() {
		return tile;
	}
	
	public static class Tile {
		Rectangle tile;
		boolean padded;
		
		public Tile(int x, int y, int w, int h, boolean padded) {
			this.tile = new Rectangle(x, y, w, h);
			this.padded = padded;
		}
	}
}
