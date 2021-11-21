package mesa.app.pages.session.content;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mesa.app.component.StatedPfp;
import mesa.app.component.StatedPfp.PfpStatus;
import mesa.app.pages.session.SessionPage;
import mesa.data.User;
import mesa.gui.controls.Font;
import mesa.gui.controls.space.FixedHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class UserBar extends HBox implements Styleable {
	private StatedPfp pfp;
	
	private Text username;
	private Text userid;

	public UserBar(SessionPage session) {
		setAlignment(Pos.CENTER);
		setPadding(new Insets(0, 8, 0, 8));

		User user = session.getUser();

		pfp = new StatedPfp(session, user.getAvatar() == null ? null : user.getAvatar(), StatedPfp.SMALL);
		pfp.setStatus(PfpStatus.ONLINE);

		VBox nameId = new VBox();
		nameId.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(nameId, Priority.ALWAYS);
		
		Font f1 = new Font(Font.DEFAULT_FAMILY_MEDIUM, 14);
		Font f2 = new Font(12);
		
		username = new Text();
		username.textProperty().bind(user.usernameProperty());
		username.setFont(f1.getFont());
		
		userid = new Text("#" + user.getId());
		userid.setFont(f2.getFont());

		nameId.getChildren().addAll(username, userid);
		
		UserBarIcon mute =  new UserBarIcon(session, "microphone", "mute_mic");
		UserBarIcon deafen =  new UserBarIcon(session, "headset", "deafen");
		UserBarIcon settings =  new UserBarIcon(session, "settings", "user_settings");
		
		settings.setAction(session::showSettings);
		
		getChildren().addAll(pfp, new FixedHSpace(8), nameId,mute, deafen, settings);

		setMinHeight(52);

		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBack4()));
		username.setFill(style.getText1());
		userid.setFill(style.getText2());
	}
}
