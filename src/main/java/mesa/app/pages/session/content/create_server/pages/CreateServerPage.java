package mesa.app.pages.session.content.create_server.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.content.create_server.MultiOverlay;
import mesa.gui.NodeUtils;
import mesa.gui.controls.Font;
import mesa.gui.controls.button.Button;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.scroll.ScrollBar;
import mesa.gui.style.Style;

public class CreateServerPage extends MultiOverlayPage {

	private VBox center;
	private Label temps;

	private ScrollBar sb;

	private Label haveInvite;
	private Button join;

	public CreateServerPage(MultiOverlay owner) {
		super(owner, "create_server", "create_server_sub");

		StackPane preCenter = new StackPane();
		preCenter.setAlignment(Pos.TOP_CENTER);
		preCenter.setMinHeight(0);
		preCenter.setMaxHeight(330);
		preCenter.setClip(new Rectangle(440, 330));

		center = new VBox(8);
		center.setPadding(new Insets(0, 16, 16, 16));
		NodeUtils.nestedFocus(center);

		sb = new ScrollBar(8, 2);
		sb.install(preCenter, center);

		temps = new Label(owner.getWindow(), "start_from_template");
		temps.setTransform(TextTransform.UPPERCASE);
		temps.setFont(new Font(12, FontWeight.BOLD));
		temps.setTranslateX(4);
		VBox.setMargin(temps, new Insets(8, 0, 0, 0));

		center.getChildren().add(
				new ServerOption(owner.getWindow(), "create_my_own", "kh", "ks").setAction(owner::next));

		center.getChildren().add(temps);

		center.getChildren().addAll(
				new ServerOption(owner.getWindow(), "gaming", "gpp", "gpb").setAction(owner::next),
				new ServerOption(owner.getWindow(), "school_club", "scp", "scb"),
				new ServerOption(owner.getWindow(), "study_group", "bpb", "bpa"),
				new ServerOption(owner.getWindow(), "friends", "friends2", "friends1"),
				new ServerOption(owner.getWindow(), "artists_and_creators", "createb", "createf"),
				new ServerOption(owner.getWindow(), "local_community", "commb", "commf"));

		preCenter.getChildren().addAll(center, sb);

		root.getChildren().addAll(preCenter);

		VBox bot = new VBox(8);
		bot.setAlignment(Pos.CENTER);
		HBox.setHgrow(bot, Priority.ALWAYS);

		haveInvite = new Label(owner.getWindow(), "have_invite");
		haveInvite.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 20));

		join = new Button(owner.getWindow(), "join_server", 3.0, 408, 38);
		join.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 13.5));

		bot.getChildren().addAll(haveInvite, join);

		bottom.getChildren().add(bot);

		applyStyle(owner.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		temps.setFill(style.getHeaderSecondary());
		sb.setThumbFill(style.getScrollbarThinThumb());
		sb.setTrackFill(style.getScrollbarThinTrack());

		haveInvite.setFill(style.getHeaderPrimary());

		join.setFill(style.getSecondaryButtonBack());
		join.setTextFill(Color.WHITE);

		super.applyStyle(style);
	}

}
