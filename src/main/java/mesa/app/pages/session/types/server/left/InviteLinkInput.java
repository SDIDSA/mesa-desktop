package mesa.app.pages.session.types.server.left;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import mesa.api.API;
import mesa.app.pages.session.SessionPage;
import mesa.app.utils.Colors;
import mesa.app.utils.Threaded;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.factory.Backgrounds;
import mesa.gui.factory.Borders;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class InviteLinkInput extends HBox implements Styleable {
	private Thread ongoing;
	
	private TextField field;
	private Button copy;

	private Timeline greenify;
	private Timeline ungreenify;

	private ObjectProperty<Paint> borderColor;

	public InviteLinkInput(SessionPage session) {
		setAlignment(Pos.CENTER);

		borderColor = new SimpleObjectProperty<>();
		borderProperty().bind(Bindings.createObjectBinding(() -> Borders.make(borderColor.get(), 3.0), borderColor));

		field = new TextField();
		field.setBackground(Background.EMPTY);
		field.setBorder(Border.EMPTY);
		field.setPadding(new Insets(10));
		field.setFont(new Font(16).getFont());
		field.setEditable(false);
		HBox.setHgrow(field, Priority.ALWAYS);

		copy = new Button(session.getWindow(), "copy", 3.0, 16, 32);
		copy.setTextFill(Color.WHITE);
		copy.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		HBox.setMargin(copy, new Insets(0, 4, 0, 0));

		copy.setAction(()-> {
			field.selectAll();
			field.copy();
			greenify();
		});

		getChildren().addAll(field, copy);

		applyStyle(session.getWindow().getStyl());
	}

	private void greenify() {
		copy.setKey("copied");
		if(ongoing != null) {
			ongoing.interrupt();
		}
		greenify.stop();
		ungreenify.stop();
		copy.setIgnoreHover(true);
		greenify.playFromStart();
		
		ongoing = Threaded.runAfter(1500, this::ungreenify);
	}
	
	private void ungreenify() {
		copy.setKey("copy");
		greenify.stop();
		ungreenify.stop();
		ungreenify.setOnFinished(e -> copy.setIgnoreHover(false));
		ungreenify.playFromStart();
	}

	public void startLoading() {
		field.setText(API.INVITE_BASE + "...");
		copy.startLoading();
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getDeprecatedTextInputBg(), 3.0));
		borderColor.set(style.getDeprecatedTextInputBorder());

		field.setStyle("-fx-text-fill: " + Styleable.colorToCss(style.getTextNormal())
				+ ";-fx-background-color:transparent;-fx-text-box-border: transparent;");

		copy.setFill(style.getAccent());

		greenify = new Timeline(new KeyFrame(Duration.seconds(.15),
				new KeyValue(copy.fillProperty(), Colors.GREEN),
				new KeyValue(borderColor, Colors.GREEN)));

		ungreenify = new Timeline(
				new KeyFrame(Duration.seconds(.15),
						new KeyValue(copy.fillProperty(), style.getAccent()),
						new KeyValue(borderColor, style.getDeprecatedTextInputBorder())));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

	public void stopLoading(String value) {
		field.setText("https://mesa-invite.tk/" + value);
		copy.stopLoading();
	}
}
