package mesa.gui.controls.button;

import javafx.beans.binding.Bindings;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontWeight;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.window.Window;

public class Button extends AbstractButton {
	private Label label;

	private boolean ulOnHover = false;

	public Button(Window window, String key, double radius, double width, double height) {
		super(window, radius, height);
		
		label = new Label(window, key);
		
		setFont(new Font(16, FontWeight.BOLD));
		
		if (width < 50) {
			prefWidthProperty()
					.bind(Bindings.createDoubleBinding(() -> label.getBoundsInLocal().getWidth() + (width * 2),
							label.textProperty(), label.fontProperty()));
		} else {
			setPrefWidth(width);
		}

		label.opacityProperty().bind(back.opacityProperty());
		
		add(label);
	}
	
	@Override
	protected void onEnter(MouseEvent event) {
		if (ulOnHover) {
			label.setUnderline(true);
		}
		super.onEnter(event);
	}
	
	@Override
	protected void onExit(MouseEvent event) {
		if (ulOnHover) {
			label.setUnderline(false);
		}
		super.onExit(event);
	}

	public void setUlOnHover(boolean ulOnHover) {
		this.ulOnHover = ulOnHover;
	}

	public Button(Window window, String key, int width) {
		this(window, key, 4.0, width, 44);
	}

	public Button(Window window, String string, double radius, int width) {
		this(window, string, radius, width, 44);
	}

	public void setFont(Font font) {
		label.setFont(font);
	}
	
	public void setKey(String key) {
		label.setKey(key);
	}

	@Override
	public void setTextFill(Paint fill) {
		label.setFill(fill);
		super.setTextFill(fill);
	}

	@Override
	public void setFill(Color fill) {
		back.setFill(fill);
		super.setFill(fill);
	}
}
