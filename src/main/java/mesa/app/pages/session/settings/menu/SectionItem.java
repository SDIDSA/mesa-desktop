package mesa.app.pages.session.settings.menu;

import java.lang.reflect.InvocationTargetException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.SettingsContent;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class SectionItem extends StackPane implements Styleable {
	private static SectionItem selected;

	private Label lab;

	private BooleanProperty selectedProperty;

	private Runnable onSelected;

	public SectionItem(Settings settings, String key, Class<? extends SettingsContent> contentClass) {
		setAlignment(Pos.CENTER_LEFT);
		setCursor(Cursor.HAND);
		setPadding(new Insets(6, 10, 6, 10));

		selectedProperty = new SimpleBooleanProperty(false);
		lab = new Label(settings.getWindow(), key, new Font(Font.DEFAULT_FAMILY_MEDIUM, 16));

		getChildren().add(lab);

		setOnMouseClicked(e -> selectMe(this));

		if (contentClass != null) {
			try {
				SettingsContent content = contentClass.getConstructor(Settings.class).newInstance(settings);
				setOnSelected(() -> settings.loadContent(content));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException x) {
				ErrorHandler.handle(x, "create settings content for " + key);
			}
		}

		applyStyle(settings.getWindow().getStyl());
	}

	private static synchronized void selectMe(SectionItem item) {
		boolean select = selected != item;
		boolean deselect = select && selected != null;

		if (deselect) {
			selected.setSelected(false);
		}

		if (select) {
			item.setSelected(true);
			selected = item;

			if (item.onSelected != null) {
				item.onSelected.run();
			}
		}
	}
	
	public SectionItem(Settings settings, String key) {
		this(settings, key, null);
	}

	public void setOnSelected(Runnable onSelected) {
		this.onSelected = onSelected;
	}

	public Label getLab() {
		return lab;
	}

	private boolean isSelected() {
		return selectedProperty.get();
	}

	private void setSelected(boolean selected) {
		selectedProperty.set(selected);
	}

	@Override
	public void applyStyle(Style style) {
		Background hover = Backgrounds.make(style.getBackHover(), 4.0);
		Background active = Backgrounds.make(style.getBackActive(), 4.0);
		Background selectedBack = Backgrounds.make(style.getBackSelected(), 4.0);

		backgroundProperty().bind(Bindings.createObjectBinding(() -> {
			if (isSelected()) {
				return selectedBack;
			} else if (isPressed()) {
				return active;
			} else if (isHover()) {
				return hover;
			}
			return Background.EMPTY;
		}, hoverProperty(), selectedProperty, pressedProperty()));

		lab.fillProperty().bind(Bindings.createObjectBinding(() -> {
			if (isSelected() || isPressed()) {
				return style.getInteractiveActive();
			} else if (isHover()) {
				return style.getInteractiveHover();
			}
			return style.getInteractiveNormal();
		}, hoverProperty(), selectedProperty, pressedProperty()));
	}
}
