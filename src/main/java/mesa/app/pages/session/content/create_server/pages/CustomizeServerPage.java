package mesa.app.pages.session.content.create_server.pages;

import java.io.File;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import mesa.api.Session;
import mesa.app.component.input.DeprecatedTextInputField;
import mesa.app.pages.session.content.create_server.MultiOverlay;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.image.ImageProxy;
import mesa.gui.controls.image.layer_icon.CircledAdd;
import mesa.gui.controls.image.layer_icon.LayerIcon;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.file.FileUtils;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class CustomizeServerPage extends MultiOverlayPage {
	private LayerIcon icon;
	private CircledAdd addIcon;
	private Text upload;

	private DeprecatedTextInputField field;

	private MultiText guidelines;

	private Button back;
	private Button create;

	private StackPane imagePane;
	private ImageView serverIcon;

	private ObjectProperty<File> iconFile;

	public CustomizeServerPage(MultiOverlay owner) {
		super(owner, "customize_server", "server_name_icon");

		iconFile = new SimpleObjectProperty<>();

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

		field = new DeprecatedTextInputField(owner.getWindow(), "server_name", 408);
		VBox.setMargin(field, new Insets(22, 0, 8, 0));

		guidelines = new MultiText(owner.getWindow());
		guidelines.addLabel("pre_guidelines", new Font(12));
		guidelines.addKeyedLink("guidelines", new Font(Font.DEFAULT_FAMILY_MEDIUM, 12));

		serverIcon = new ImageView();
		serverIcon.setFitHeight(80);
		serverIcon.setFitWidth(80);
		imagePane = new StackPane(serverIcon);

		Rectangle clip = new Rectangle(80, 80);
		clip.setArcHeight(80);
		clip.setArcWidth(80);
		imagePane.setClip(clip);

		StackPane iconPane = new StackPane(icon);
		iconPane.setMaxSize(80, 80);

		iconPane.setCursor(Cursor.HAND);
		icon.opacityProperty().bind(Bindings.when(icon.hoverProperty()).then(1).otherwise(.8));

		iconPane.setOnMouseClicked(e -> {
			File file = FileUtils.selectImage(owner.getWindow());
			if (file != null)
				iconFile.set(file);
		});

		iconFile.addListener((obs, ov, nv) -> {
			File val = iconFile.get();
			if (val != null) {
				Image image = ImageProxy.load(val.getAbsolutePath(), 80, true);
				serverIcon.setImage(image);
				iconPane.getChildren().setAll(imagePane);

				if (image.getWidth() > 80.0) {
					serverIcon.setViewport(new Rectangle2D((image.getWidth() - 80) / 2, 0, 80, 80));
				} else if (image.getHeight() > 80.0) {
					serverIcon.setViewport(new Rectangle2D(0, (image.getHeight() - 80) / 2, 80, 80));
				} else {
					serverIcon.setViewport(null);
				}
			} else {
				iconPane.getChildren().setAll(icon);
			}
		});

		center.getChildren().addAll(iconPane, field, guidelines);

		root.getChildren().add(center);

		back = new Button(owner.getWindow(), "back", 3.0, 16, 38);
		back.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		back.setUlOnHover(true);
		back.setFill(Color.TRANSPARENT);

		create = new Button(owner.getWindow(), "create", 3.0, 28, 38);
		create.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		create.disableProperty().bind(field.valueProperty().isEmpty());
		
		create.setAction(() -> {
			create.startLoading();
			Session.createServer(field.getValue(), owner.getString("template"), owner.getString("audience"), iconFile.get(), result -> {
				if(!result.has("err")) {
					owner.hide();
					iconFile.set(null);
				}
				create.stopLoading();
			});
		});

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
