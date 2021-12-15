package mesa.app.pages.session.content.create_server.pages;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mesa.app.pages.session.content.create_server.MultiOverlay;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.label.MultiText;
import mesa.gui.style.Style;

public class TellUsMorePage extends MultiOverlayPage {

	private Button back;

	private MultiText skip;

	public TellUsMorePage(MultiOverlay owner) {
		super(owner, "server_tell_us_more", "server_scale");
		
		VBox center = new VBox(8);
		center.setPadding(new Insets(0, 16, 16, 16));

		Font font = new Font(14);

		skip = new MultiText(owner.getWindow());
		skip.addLabel("not_sure", font);
		skip.addKeyedLink("skip_question", font);
		skip.addLabel("for_now", font);
		
		skip.center();
		VBox.setMargin(skip, new Insets(10, 0, 2, 0));
		
		center.getChildren().addAll(
				new ServerOption(owner.getWindow(), "for_me_and_friends", "friends2", "friends1").setAction(owner::next),
				new ServerOption(owner.getWindow(), "for_club_or_community", "commb", "commf").setAction(owner::next)
			);

		center.getChildren().add(skip);
		
		root.getChildren().add(center);

		back = new Button(owner.getWindow(), "back", 3.0, 16, 38);
		back.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 14));
		back.setUlOnHover(true);
		back.setFill(Color.TRANSPARENT);
		
		back.setAction(owner::back);

		bottom.getChildren().add(back);

		applyStyle(owner.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		skip.setFill(style.getHeaderSecondary());
		skip.setFill(style.getHeaderSecondary());
		
		back.setTextFill(style.getTextNormal());

		super.applyStyle(style);
	}

}
