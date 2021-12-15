package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import mesa.app.pages.session.SessionPage;

public class PhoneOverlayContent extends VBox {
	protected SessionPage owner;
	
	public PhoneOverlayContent(SessionPage owner) {
		this.owner = owner;
		setPickOnBounds(false);
		setAlignment(Pos.BOTTOM_CENTER);
		setPadding(new Insets(106, 16, 16, 16));
		setMinHeight(310);
		setMaxWidth(472);
	}
}
