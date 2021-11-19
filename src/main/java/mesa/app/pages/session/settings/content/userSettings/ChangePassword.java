package mesa.app.pages.session.settings.content.userSettings;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.settings.Settings;
import mesa.gui.controls.Button;
import mesa.gui.controls.Font;
import mesa.gui.controls.Overlay;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class ChangePassword extends Overlay implements Styleable {
	private VBox topCenter;
	private Label head, subHead;
	private HBox bottom;

	Button cancel, done;
	
	private TextInputField currPass, newPass, confNewPass;
	
	private ColorIcon closeIcon;

	public ChangePassword(Settings settings) {
		topCenter = new VBox();
		topCenter.setMaxWidth(440);

		StackPane preTop = new StackPane();
		preTop.setPadding(new Insets(16));
		preTop.setAlignment(Pos.TOP_RIGHT);

		VBox top = new VBox(8);
		top.setPadding(new Insets(8, 0, 12, 0));
		top.setAlignment(Pos.CENTER);
		
		closeIcon = new ColorIcon(settings.getWindow(), "close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(this::hide);
		closeIcon.setCursor(Cursor.HAND);
		
		head = new Label(settings.getWindow(), "change_attr", new Font(24, FontWeight.BOLD));
		head.addParam(0, "&password");
		head.setTransform(TextTransform.CAPITALIZE_PHRASE);
		
		subHead = new Label(settings.getWindow(), "enter_attr", new Font(15));
		subHead.addParam(0, "&password");
		subHead.setTransform(TextTransform.CAPITALIZE_PHRASE);
		
		top.getChildren().addAll(head, subHead);
		preTop.getChildren().addAll(top, closeIcon);
	
		VBox center = new VBox(16);
		center.setPadding(new Insets(0, 16, 16, 16));
		
		currPass = new TextInputField(settings.getWindow(), "current_password", 408, true);
		newPass = new TextInputField(settings.getWindow(), "new_pass", 408, true);
		confNewPass = new TextInputField(settings.getWindow(), "confirm_new_pass", 408, true);

		center.getChildren().addAll(currPass, newPass, confNewPass);
		
		topCenter.getChildren().addAll(preTop, center);
		
		bottom = new HBox(8);
		bottom.setMaxWidth(440);
		bottom.setPadding(new Insets(16));
		
		cancel = new Button(settings.getWindow(), "cancel", 3, 24, 38);
		cancel.setFont(new Font(14, FontWeight.BOLD));
		cancel.setUlOnHover(true);
		cancel.setAction(this::hide);

		done = new Button(settings.getWindow(), "done", 3, 24, 38);
		done.setFont(new Font(14, FontWeight.BOLD));

		bottom.getChildren().addAll(new ExpandingHSpace(), cancel, done);
		
		setContent(topCenter, bottom);
		
		applyStyle(settings.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		topCenter.setBackground(Backgrounds.make(style.getBack1(), new CornerRadii(5, 5, 0, 0, false)));
		bottom.setBackground(Backgrounds.make(style.getBack2(), new CornerRadii(0, 0, 5, 5, false)));
		head.setFill(style.getText1());
		subHead.setFill(style.getInteractiveNormal());

		cancel.setFill(Color.TRANSPARENT);
		cancel.setTextFill(style.getText1());

		done.setFill(style.getAccent());
		done.setTextFill(style.getText1());

		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getText1()).otherwise(style.getText1().deriveColor(0, 1, 1, .5)));
	}
}
