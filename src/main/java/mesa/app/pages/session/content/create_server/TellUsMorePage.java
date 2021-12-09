package mesa.app.pages.session.content.create_server;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.label.Label;
import mesa.gui.controls.label.Link;
import mesa.gui.style.Style;

public class TellUsMorePage extends MultiOverlayPage {

	private Button back;

	private Label preSkip;
	private Link skip;
	private Label postSkip;

	public TellUsMorePage(MultiOverlay owner) {
		super(owner, "server_tell_us_more", "server_scale");

		setMinHeight(0);
		setMaxHeight(392);
		
		VBox center = new VBox(8);
		center.setPadding(new Insets(0, 16, 16, 16));

		Font font = new Font(14);
		preSkip = new Label(owner.getWindow(), "not_sure", font);
		skip = new Link(owner.getWindow(), "skip_question", font);
		postSkip = new Label(owner.getWindow(), "for_now", font);

		TextFlow textLine = new TextFlow(preSkip, skip, postSkip);
		textLine.setTextAlignment(TextAlignment.CENTER);
		VBox.setMargin(textLine, new Insets(10, 0, 2, 0));
		
		center.getChildren().add(new ServerOption(owner.getWindow(), "for_me_and_friends", "friends2", "friends1"));
		center.getChildren().add(new ServerOption(owner.getWindow(), "for_club_or_community", "commb", "commf"));

		center.getChildren().add(textLine);
		
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
		preSkip.setFill(style.getHeaderSecondary());
		postSkip.setFill(style.getHeaderSecondary());
		
		back.setTextFill(style.getTextNormal());

		super.applyStyle(style);
	}

}
