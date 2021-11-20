package mesa.app.component;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import mesa.app.pages.session.SessionPage;
import mesa.app.utils.Colors;
import mesa.app.utils.DoubleParamFunc;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.image.Icon;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class StatedPfp extends StackPane {
	public static final PfpSize BIG = new PfpSize(80, 28, 16, 2);
	public static final PfpSize SMALL = new PfpSize(32, 16, 10, 3);

	private StackPane statusPane;

	private ObjectProperty<PfpStatus> status;

	private Icon pfp;

	public StatedPfp(SessionPage session, String path, PfpSize size) {
		setAlignment(Pos.BOTTOM_RIGHT);

		status = new SimpleObjectProperty<>(null);

		pfp = null;
		if (path != null) {
			pfp = new Icon(session.getWindow(), path, size.size, true);
		} else {
			pfp = new Icon(session.getWindow(), "user", size.size);
			pfp.setBrightness(session.getWindow().getStyl() == Style.DARK ? 1 : -1);
		}

		Rectangle keep = new Rectangle(size.size, size.size);
		double offset = size.size - size.clip + size.offset;
		Rectangle remove = new Rectangle(offset, offset, size.clip, size.clip);
		remove.setArcHeight(size.clip);
		remove.setArcWidth(size.clip);

		Shape clip = Shape.subtract(keep, remove);

		pfp.setClip(clip);

		setMinSize(size.size, size.size);
		setMaxSize(size.size, size.size);

		statusPane = new StackPane();
		statusPane.setTranslateX(size.offset);
		statusPane.setTranslateY(size.offset);

		statusPane.setMinSize(size.clip, size.clip);
		statusPane.setMaxSize(size.clip, size.clip);

		status.addListener((obs, ov, nv) -> {
			statusPane.getChildren().clear();
			if (nv == null) {
				pfp.setClip(null);
			} else {
				statusPane.getChildren().add(nv.getDisplay(session.getWindow(), size));
				pfp.setClip(clip);
			}
		});

		ColorAdjust ca = new ColorAdjust();
		setEffect(ca);

		setCursor(Cursor.HAND);

		ca.brightnessProperty().bind(Bindings.when(hoverProperty()).then(-.2).otherwise(0));

		getChildren().addAll(pfp, statusPane);
	}

	public void setStatus(PfpStatus status) {
		this.status.set(status);
	}

	public enum PfpStatus {
		ONLINE((w,s) -> {
			Circle res = new Circle(s.status / 2);
			res.setFill(Colors.GREEN);
			return res;
		}), IDLE((w,s) -> {
			ColorIcon res = new ColorIcon("moon", s.status);
			res.setFill(Colors.YELLEOW);
			return res;
		}), INVISIBLE((w,s) -> {
			ColorIcon res = new ColorIcon("invisible", s.status);
			res.setFill(Colors.INVISIBLE);
			return res;
		});

		private DoubleParamFunc<Window, PfpSize, Node> display;

		private PfpStatus(DoubleParamFunc<Window, PfpSize, Node> init) {
			display = init;
		}

		public Node getDisplay(Window window, PfpSize size) {
			return display.execute(window, size);
		}
	}
	
	static class PfpSize {
		double size;
		double clip;
		double status;
		double offset;

		public PfpSize(int size, int clip, int status, int offset) {
			this.size = size;
			this.clip = clip;
			this.status = status;
			this.offset = offset;
		}
	}
}
