package mesa.app.pages.session.types.home;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class HomeSideTop extends StackPane implements Styleable {
	private StackPane ins;
	private Label lab;
	
	private Runnable action;
	public HomeSideTop(SessionPage session) {
		setPadding(new Insets(10));

		ins = new StackPane();
		ins.setMinHeight(28);
		ins.setPadding(new Insets(0, 6, 0, 6));
		ins.setAlignment(Pos.CENTER_LEFT);
		
		ins.setCursor(Cursor.HAND);
		ins.setOnMouseClicked(e-> {
			if(action != null) {
				action.run();
			}
		});
		
		lab = new Label(session.getWindow(), "find_start", new Font(13.333));
		
		ins.getChildren().add(lab);
		
		getChildren().add(ins);
		
		
		applyStyle(session.getWindow().getStyl());
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}

	@Override
	public void applyStyle(Style style) {
		ins.setBackground(Backgrounds.make(style.getBack3(), 4.0));
		lab.setFill(style.getTextMuted());
	}
}
