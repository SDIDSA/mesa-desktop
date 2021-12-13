package mesa.app.pages.session.settings.menu;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import mesa.app.pages.session.settings.Settings;
import mesa.app.pages.session.settings.content.SettingsContent;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.Label;
import mesa.gui.exception.ErrorHandler;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class SectionItem extends StackPane implements Styleable {
	private static HashMap<Class<? extends SettingsContent>, SettingsContent> cache = new HashMap<>();
	private static SectionItem selected;

	public static void clearCache() {
		cache.clear();
		selected = null;
	}

	private boolean fillSet = false;

	private Label lab;

	private BooleanProperty selectedProperty;

	private Runnable onSelected;
	private Runnable action;

	public SectionItem(Settings settings, String key) {
		setAlignment(Pos.CENTER_LEFT);
		setCursor(Cursor.HAND);
		setPadding(new Insets(8, 10, 8, 10));

		selectedProperty = new SimpleBooleanProperty(false);
		lab = new Label(settings.getWindow(), key, new Font(Font.DEFAULT_FAMILY_MEDIUM, 15));

		setFocusTraversable(true);

		setOnMouseClicked(e -> {
			if (action != null) {
				action.run();
			}
		});
		setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.SPACE) && action != null) {
				action.run();
			}
		});

		getChildren().add(lab);
		applyStyle(settings.getWindow().getStyl());
	}

	public SectionItem(Settings settings, String key, Class<? extends SettingsContent> contentClass) {
		this(settings, key);

		this.action = () -> selectMe(this);

		setOnSelected(() -> {
			if (contentClass != null) {
				SettingsContent found = cache.get(contentClass);
				if (found == null) {
					try {
						found = contentClass.getConstructor(Settings.class).newInstance(settings);
						cache.put(contentClass, found);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException x) {
						ErrorHandler.handle(x, "create settings content for " + key);
					}
				}

				settings.loadContent(found);
			}
		});
	}

	public SectionItem(Settings settings, String key, Runnable onAction) {
		this(settings, key);
		this.action = onAction;
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

	public void setTextFill(Color fill) {
		fillSet = true;
		lab.fillProperty().unbind();
		lab.setFill(fill);
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
		Background hover = Backgrounds.make(style.getBackgroundModifierHover(), 4.0);
		Background active = Backgrounds.make(style.getBackgroundModifierActive(), 4.0);
		Background selectedBack = Backgrounds.make(style.getBackgroundModifierSelected(), 4.0);

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

		NodeUtils.focusBorder(this, style.getTextLink());

		if (!fillSet)
			lab.fillProperty().bind(Bindings.createObjectBinding(() -> {
				if (isSelected() || isPressed()) {
					return style.getInteractiveActive();
				} else if (isHover()) {
					return style.getInteractiveHover();
				}
				return style.getInteractiveNormal();
			}, hoverProperty(), selectedProperty, pressedProperty()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
