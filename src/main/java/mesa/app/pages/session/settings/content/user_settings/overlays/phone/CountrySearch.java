package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.SearchClearIcon;
import mesa.gui.factory.Backgrounds;
import mesa.gui.locale.Locale;
import mesa.gui.locale.Localized;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class CountrySearch extends StackPane implements Styleable, Localized {

	private TextField field;
	private SearchClearIcon icon;
	
	private Consumer<String> search;
	private Runnable reset;
	
	public CountrySearch() {
		setPadding(new Insets(1));
		setAlignment(Pos.CENTER_RIGHT);

		field = new TextField();
		field.setBackground(Background.EMPTY);
		field.setBorder(Border.EMPTY);
		field.setPadding(new Insets(0, 27, 0, 5));
		field.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 13).getFont());
		field.setMinHeight(22);

		field.textProperty().addListener((obs, ov, nv) -> {
			if (nv.isBlank()) {
				icon.search();

				if(reset != null) {
					reset.run();
				}
			} else {
				icon.clear();

				if(search != null) {
					search.accept(nv);
				}
			}
		});

		icon = new SearchClearIcon(12);
		icon.setTranslateX(-6);
		icon.setOnMouseClicked(e -> field.clear());

		getChildren().addAll(field, icon);
	}
	
	public void setReset(Runnable reset) {
		this.reset = reset;
	}
	
	public void setSearch(Consumer<String> search) {
		this.search = search;
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBack3(), 4.0));

		Color tx = style.getText2();
		field.setStyle("-fx-text-fill: " + Styleable.colorToCss(tx) + ";-fx-prompt-text-fill: "
				+ Styleable.colorToCss(tx.deriveColor(0, 1, 1, .35))
				+ ";-fx-background-color:transparent;-fx-text-box-border: transparent;");

		icon.setFill(style.getText2());
	}

	@Override
	public void applyLocale(Locale locale) {
		field.setPromptText(locale.get("search_country"));
	}

}
