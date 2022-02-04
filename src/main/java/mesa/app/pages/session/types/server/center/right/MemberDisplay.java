package mesa.app.pages.session.types.server.center.right;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import mesa.app.component.StatedPfp;
import mesa.app.component.StatedPfp.PfpStatus;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.User;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MemberDisplay extends HBox implements Styleable {
	private Text userName;
	
	public MemberDisplay(SessionPage session, User user) {
		super(12);
		setAlignment(Pos.CENTER_LEFT);
		setPadding(new Insets(0, 8, 0, 8));
		setMinHeight(44);
		
		userName = new Text("", new Font(Font.DEFAULT_FAMILY_MEDIUM, 16));
		userName.textProperty().bind(user.usernameProperty());
		
		StatedPfp pfp = new StatedPfp(session, user.getAvatar(), StatedPfp.SMALL);
		pfp.setMouseTransparent(true);
		pfp.statusProperty().bind(Bindings.when(user.onlineProperty()).then(PfpStatus.ONLINE).otherwise(PfpStatus.OFFLINE));
		
		opacityProperty().bind(Bindings.when(user.onlineProperty()).then(1).otherwise(.4));
		
		getChildren().addAll(pfp, userName);
		
		applyStyle(session.getWindow().getStyl());
	}

	@Override
	public void applyStyle(Style style) {
		userName.setFill(style.getChannelsDefault());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
