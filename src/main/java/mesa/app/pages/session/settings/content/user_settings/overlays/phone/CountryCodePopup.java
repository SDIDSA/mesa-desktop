package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.PopupControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.util.Duration;
import mesa.data.CountryCode;
import mesa.gui.NodeUtils;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.controls.scroll.ScrollBar;
import mesa.gui.controls.space.Separator;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.file.FileUtils;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class CountryCodePopup extends PopupControl implements Styleable, Localized {
	protected Window owner;
	private VBox root;

	private Separator separator;
	private ScrollBar scrollBar;
	
	private Consumer<CountryCode> onSelect;

	private Scale scale;
	private Timeline scaleShow;
	public CountryCodePopup(Window owner) {
		this.owner = owner;
		setAutoHide(true);

		root = new VBox();
		root.setPadding(new Insets(10, 10, 0, 10));
		root.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
		root.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
		root.setPrefSize(240, 250);
		root.setCache(true);
		root.setCacheHint(CacheHint.SPEED);

		CountrySearch search = new CountrySearch();

		separator = new Separator(Orientation.HORIZONTAL);
		VBox.setMargin(separator, new Insets(8, 0, 8, 0));

		StackPane listCont = new StackPane();
		listCont.setMinHeight(0);
		listCont.setMaxHeight(196);
		listCont.setAlignment(Pos.TOP_RIGHT);
		listCont.setMaxWidth(220);
		listCont.setClip(new Rectangle(400, 196));

		VBox items = new VBox(4);
		items.setMaxWidth(200);
		items.setPadding(new Insets(4, 20, 10, 0));

		Map<CountryCode, CountryCodeItem> cache = new HashMap<>();
		Consumer<List<CountryCode>> display = codes -> {
			items.getChildren().clear();
			for (CountryCode code : codes) {
				CountryCodeItem item = cache.get(code);
				if(item == null) {
					item = new CountryCodeItem(code);
					item.setOnMouseClicked(e-> onSelect.accept(code));
					cache.put(code, item);
				}
				
				items.getChildren().add(item);
			}
		};

		List<CountryCode> all = FileUtils.readCountryCodes();

		Runnable reset = () -> display.accept(all);

		Consumer<String> searchFor = text -> new Thread() {
			@Override 
			public void run() {
				ArrayList<CountryCode> found = new ArrayList<>();

				for (CountryCode code : all) {
					if (code.match(text)) {
						found.add(code);
					}
				}

				Platform.runLater(() -> display.accept(found));
			}
		}.start();

		search.setReset(reset);
		search.setSearch(searchFor);

		reset.run();

		scrollBar = new ScrollBar(16, 4);
		scrollBar.install(listCont, items);

		listCont.getChildren().addAll(items, scrollBar);

		root.getChildren().addAll(search, separator, listCont);

		StackPane preRoot = new StackPane();
		preRoot.setPadding(new Insets(6));

		root.setEffect(new DropShadow(8, Color.gray(0, .25)));

		getScene().setRoot(root);

		scale = new Scale(1, 1, 0, 0);
		root.getTransforms().add(scale);
		
		scaleShow = new Timeline(
				new KeyFrame(Duration.seconds(.1), 
						new KeyValue(scale.xProperty(), 1, SplineInterpolator.OVERSHOOT), 
						new KeyValue(scale.yProperty(), 1, SplineInterpolator.OVERSHOOT), 
						new KeyValue(root.opacityProperty(), 1, SplineInterpolator.OVERSHOOT)
					));
		
		applyStyle(owner.getStyl());
		applyLocale(owner.getLocale());
	}
	
	public void setOnSelect(Consumer<CountryCode> onSelect) {
		this.onSelect = onSelect;
	}

	public void showPop(Node node) {
		scale.setX(.7);
		scale.setY(.7);
		root.setOpacity(0);
		setOnShown(e -> {
			Bounds bounds = node.getBoundsInLocal();
			Bounds screenBounds = node.localToScreen(bounds);

			double x = screenBounds.getMinX();
			double y = screenBounds.getMaxY() - 8;

			Screen screen = Screen.getScreensForRectangle(new Rectangle2D(x, y, 10, 10)).get(0);

			if (y + getHeight() >= screen.getVisualBounds().getHeight() + 16) {
				y = screenBounds.getMinY() - getHeight() + 8;
				scale.setPivotY(getHeight());
			}else {
				scale.setPivotY(0);
			}

			setX(x);
			setY(y);
			
			scaleShow.stop();
			scaleShow.playFromStart();
		});
		this.show(owner);
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBack1(), 5.0));
		root.setBorder(Borders.make(style == Style.DARK ? Color.web("#20222599") : null, 4.0));

		separator.setFill(style.getBackAccent());

		scrollBar.setThumbFill(style.getBack3());
		scrollBar.setTrackFill(style.getBack2());

		NodeUtils.applyStyle(root, style);
	}

	@Override
	public void applyLocale(Locale locale) {
		NodeUtils.applyLocale(root, locale);
	}
}
