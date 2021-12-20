package mesa.app.pages.session.content.create_server.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.app.component.input.TextInputField;
import mesa.app.pages.session.content.create_server.MultiOverlay;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.image.layer_icon.CircledAdd;
import mesa.gui.controls.image.layer_icon.LayerIcon;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class CustomizeServerPage extends MultiOverlayPage {
	private LayerIcon icon;
	private CircledAdd addIcon;
	private Text upload;

	private TextInputField field;

	private MultiText guidelines;

	private Button back;
	private Button create;
	
	private StackPane pane;
	private ImageView serverIcon;

	public CustomizeServerPage(MultiOverlay owner) {
		super(owner, "customize_server", "server_name_icon");

		VBox center = new VBox(8);
		center.setAlignment(Pos.CENTER);
		center.setPadding(new Insets(0, 16, 16, 16));

		icon = new LayerIcon(80, "uploadc");
		icon.addLayer("camera", 20);

		icon.setTranslateY(1, -12);

		upload = new Text("upload", new Font(12, FontWeight.BOLD));
		upload.setTransform(TextTransform.UPPERCASE);
		upload.setTranslateY(12);

		addIcon = new CircledAdd(24);
		StackPane.setAlignment(addIcon, Pos.TOP_RIGHT);

		icon.getChildren().addAll(upload, addIcon);

		field = new TextInputField(owner.getWindow(), "server_name", 408);
		VBox.setMargin(field, new Insets(22, 0, 8, 0));
		
		icon.setCursor(Cursor.HAND);
		icon.setOnMouseClicked(getOnDragDetected());

		guidelines = new MultiText(owner.getWindow());
		guidelines.addLabel("pre_guidelines", new Font(12));
		guidelines.addKeyedLink("guidelines", new Font(Font.DEFAULT_FAMILY_MEDIUM, 12));

		StackPane iconPane = new StackPane(icon);
		iconPane.setMaxSize(80, 80);
		
		
		
		center.getChildren().addAll(iconPane, field, guidelines);

		root.getChildren().add(center);

		back = new Button(owner.getWindow(), "back", 3.0, 16, 38);
		back.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		back.setUlOnHover(true);
		back.setFill(Color.TRANSPARENT);

		create = new Button(owner.getWindow(), "create", 3.0, 28, 38);
		create.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		back.setAction(owner::back);

		bottom.getChildren().addAll(back, new ExpandingHSpace(), create);

		applyStyle(owner.getWindow().getStyl());
	}

	@Override
	public void setup(Window window) {
		field.clear();
		field.requestFocus();
		field.setValue(window.getLoggedUser().getUsername() + "'s Server");
		field.positionCaret(field.getValue().length());

		super.setup(window);
	}

	@Override
	public void applyStyle(Style style) {
		icon.setFill(0, style.getHeaderSecondary());
		icon.setFill(1, style.getHeaderSecondary());

		addIcon.setCircleFill(style.getAccent());
		addIcon.setSignFill(Color.WHITE);

		upload.setFill(style.getHeaderSecondary());

		guidelines.setFill(style.getTextMuted());

		create.setFill(style.getAccent());
		create.setTextFill(Color.WHITE);

		back.setTextFill(style.getTextNormal());

		super.applyStyle(style);
	}

}
