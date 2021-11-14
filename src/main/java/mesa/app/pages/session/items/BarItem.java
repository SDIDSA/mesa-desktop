package mesa.app.pages.session.items;

import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.popup.tooltip.Tooltip;

public abstract class BarItem extends HBox {
	private ItemPill pill;
	private ItemIcon icon;
	
	private Runnable action;
	
	private Tooltip tip;

	public BarItem(SessionPage session) {
		super(4);
		setMinHeight(48);
		setAlignment(Pos.CENTER_LEFT);

		pill = new ItemPill(session);

		getChildren().addAll(pill);
	}

	public void setTooltip(Tooltip tip) {
		this.tip = tip;
		Tooltip.install(icon, tip);
	}
	
	public Tooltip getTooltip() {
		return tip;
	}
	
	private static BarItem selected;
	protected void setIcon(ItemIcon icon) {
		this.icon = icon;
		icon.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
			if (!icon.isSelected()) {
				pill.enter();
			}
		});

		icon.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
			if (!icon.isSelected()) {
				pill.exit();
			}
		});

		icon.setOnMouseClicked(e -> {
			if(action != null)  {
				action.run();
			}
			
			if(selected != null) {
				if(selected != this) {
					selected.icon.unselect();
					selected.pill.exit();
				}
			}
			

			icon.select();
			pill.select();
			selected = this;
		});

		getChildren().add(icon);
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
}
