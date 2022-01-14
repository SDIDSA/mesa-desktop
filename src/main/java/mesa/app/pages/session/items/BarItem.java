package mesa.app.pages.session.items;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.popup.tooltip.Tooltip;

public abstract class BarItem extends HBox {
	private ItemPill pill;
	private ItemIcon icon;
	
	private Runnable action;
	
	private Tooltip tip;
	
	private boolean selectable = true;

	protected BarItem(SessionPage session) {
		super(4);
		setMinHeight(48);
		setAlignment(Pos.CENTER_LEFT);

		setFocusTraversable(true);
		
		focusedProperty().addListener((obs, ov, nv) -> {
			if(nv.booleanValue()) {
				icon.focus();
			}else {
				icon.unfocus();
			}
		});
		
		setOnKeyPressed(e-> {
			if(e.getCode().equals(KeyCode.SPACE)) {
				fire();
			}
		});
		
		pill = new ItemPill(session);

		getChildren().addAll(pill);
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
	public void setTooltip(Tooltip tip) {
		this.tip = tip;
		tip.setOffset(15);
		Tooltip.install(icon, tip);
	}
	
	public Tooltip getTooltip() {
		return tip;
	}
	
	private static BarItem selected;
	protected void setIcon(ItemIcon icon) {
		this.icon = icon;
		icon.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
			if (!icon.isSelected() && selectable) {
				pill.enter();
			}
		});

		icon.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
			if (!icon.isSelected() && selectable) {
				pill.exit();
			}
		});

		icon.setOnMouseClicked(e -> fire());

		getChildren().add(icon);
	}
	
	private void fire() {
		if(action != null)  {
			action.run();
		}
		
		icon.hover();
		
		if(selectable) {
			if(selected != null && selected != this) {
				selected.icon.unselect();
				selected.pill.exit();
			}
			
			icon.select();
			pill.select();
			selected = this;
		}
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public static void clear() {
		selected = null;
	}
}
