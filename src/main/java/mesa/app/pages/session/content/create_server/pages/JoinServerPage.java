package mesa.app.pages.session.content.create_server.pages;

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import mesa.api.API;
import mesa.api.Session;
import mesa.app.component.Form;
import mesa.app.component.input.ModernTextInputField;
import mesa.app.pages.session.content.create_server.MultiOverlay;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class JoinServerPage extends MultiOverlayPage {

	private ModernTextInputField link;

	private Button back;
	private Button join;

	private Label exampleHead;

	private ArrayList<Text> examples;

	public JoinServerPage(MultiOverlay owner) {
		super(owner, "join_server", "enter_invite");

		VBox center = new VBox();
		center.setAlignment(Pos.CENTER_LEFT);
		center.setPadding(new Insets(0, 16, 16, 16));

		link = new ModernTextInputField(owner.getWindow(), "invite_link", 408);
		link.setPrompt(API.INVITE_BASE + API.INVITE_CODE);

		exampleHead = new Label(owner.getWindow(), "invites_format", new Font(12, FontWeight.BOLD));
		exampleHead.setTransform(TextTransform.UPPERCASE);
		exampleHead.setOpacity(.8);
		VBox.setMargin(exampleHead, new Insets(20, 0, 10, 0));

		examples = new ArrayList<>();

		examples.add(new Text(API.INVITE_CODE, new Font(14)));
		examples.add(new Text(API.INVITE_BASE + API.INVITE_CODE, new Font(14)));
		examples.add(new Text(API.INVITE_BASE + "cool-people", new Font(14)));

		examples.forEach(example -> {
			VBox.setMargin(example, new Insets(2, 0, 0, 0));
			example.setCursor(Cursor.HAND);
			example.opacityProperty().bind(Bindings.when(example.hoverProperty()).then(.9).otherwise(.7));
			example.setOnMouseClicked(e -> link.setValue(example.getText()));
		});

		ExploreJoin explore = new ExploreJoin(owner.getWindow());
		VBox.setMargin(explore, new Insets(16, 0, 4, 0));

		center.getChildren().addAll(link, exampleHead);
		center.getChildren().addAll(examples);
		center.getChildren().add(explore);

		root.getChildren().add(center);

		back = new Button(owner.getWindow(), "back", 3.0, 16, 38);
		back.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		back.setUlOnHover(true);
		back.setFill(Color.TRANSPARENT);

		back.setAction(() -> owner.load(0));

		join = new Button(owner.getWindow(), "server_join", 3.0, 28, 38);
		join.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));

		Form form = NodeUtils.getForm(center);
		form.setDefaultButton(join);

		join.setAction(() -> {
			if (form.check()) {
				String val = link.getValue();
				val = val.replace(API.INVITE_BASE, "");
				if (val.length() == 8 || val.length() == 7) {
					join.startLoading();
					Session.joinWithInvite(val, result -> {
						if (result.has("err")) {
							form.applyErrors(result.getJSONArray("err"));
						} else {
							owner.hide();
						}

						join.stopLoading();
					});
				} else {
					link.setError("invalid_invite");
				}
			}
		});

		bottom.getChildren().addAll(back, new ExpandingHSpace(), join);

		applyStyle(owner.getWindow().getStyl());
	}

	@Override
	public void setup(Window window) {
		link.clear();

		super.setup(window);
	}

	@Override
	public void applyStyle(Style style) {
		join.setFill(style.getAccent());
		join.setTextFill(Color.WHITE);

		exampleHead.setFill(style.getHeaderSecondary());
		examples.forEach(example -> example.setFill(style.getHeaderPrimary()));

		back.setTextFill(style.getTextNormal());

		super.applyStyle(style);
	}

}
