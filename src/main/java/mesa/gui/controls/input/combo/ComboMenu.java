package mesa.gui.controls.input.combo;

import javafx.scene.layout.VBox;
import mesa.gui.controls.popup.NodePopup;
import mesa.gui.controls.scroll.Scrollable;
import mesa.gui.window.Window;

public class ComboMenu extends NodePopup {
	
	private ComboInput input;
	private VBox items;
	
	public ComboMenu(Window window, ComboInput input) {
		super(window);
		
		this.input = input;
		
		items = new VBox();
		
		Scrollable sc = new Scrollable();
		sc.setContent(items);
		
		sc.setMaxHeight(300);
		
		root.getChildren().add(sc);
	}
	
	public void showPop() {
		super.showPop(input);
	}
	
	public void addItem(ComboMenuItem item) {
		item.setOnMouseClicked(e-> {
			input.setValue(item);
			hide();
		});
		items.getChildren().add(item);
	}

}
