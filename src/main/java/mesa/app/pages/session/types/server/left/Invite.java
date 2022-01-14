package mesa.app.pages.session.types.server.left;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.api.Session;
import mesa.app.pages.session.SessionPage;
import mesa.app.utils.Threaded;
import mesa.data.bean.Server;
import mesa.gui.controls.Font;
import mesa.gui.controls.alert.Overlay;
import mesa.gui.controls.check.KeyedCheck;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.keyed.Label;
import mesa.gui.controls.popup.Direction;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class Invite extends Overlay implements Styleable {
	private Server server;

	private StackPane preRoot;
	private HBox bottom;

	private ColorIcon closeIcon;
	private VBox root;

	private Label head;
	private Label share;
	private Label expires;

	private InviteLinkInput input;

	private KeyedCheck check;

	private ActionIcon linkSettings;
	
	private boolean inited = false;

	protected Invite(SessionPage session, Server server, double width) {
		super(session);
		this.server = server;

		root = new VBox();
		root.setPadding(new Insets(16));

		head = new Label(session.getWindow(), "invite_to", new Font(16, FontWeight.BOLD));
		head.addParam(0, server.getName());
		server.nameProperty().addListener((obs, ov, nv) -> head.addParam(0, nv));
		head.setTransform(TextTransform.UPPERCASE);

		share = new Label(session.getWindow(), "share_invite", new Font(14));
		expires = new Label(session.getWindow(), "link_expires_in", new Font(12));
		expires.addParam(0, "7 days");

		VBox.setMargin(head, new Insets(0, 0, 20, 0));
		VBox.setMargin(share, new Insets(0, 0, 12, 0));
		VBox.setMargin(expires, new Insets(10, 0, 4, 0));

		input = new InviteLinkInput(session);

		root.getChildren().addAll(head, share, input, expires);

		preRoot = new StackPane();
		preRoot.setAlignment(Pos.TOP_RIGHT);
		preRoot.setMaxWidth(width);

		closeIcon = new ColorIcon("close", 16, true);
		closeIcon.setPadding(8);
		closeIcon.setAction(this::hide);
		closeIcon.setCursor(Cursor.HAND);
		StackPane.setMargin(closeIcon, new Insets(8));

		root.setPickOnBounds(false);

		preRoot.getChildren().addAll(closeIcon, root);

		bottom = new HBox(8);
		bottom.setMaxWidth(width);
		bottom.setPadding(new Insets(20, 16, 16, 16));

		check = new KeyedCheck(session.getWindow(), "never_expire", 18);
		check.setFont(new Font(14));

		linkSettings = new ActionIcon(session.getWindow(), "settings", 15, 18, "link_settings", Direction.LEFT);
		linkSettings.setTipOffset(5);

		bottom.getChildren().addAll(check, new ExpandingHSpace(), linkSettings);

		setContent(preRoot, bottom);

		applyStyle(session.getWindow().getStyl());
	}

	protected Invite(SessionPage session, Server server) {
		this(session, server, 440);
	}

	@Override
	public void show() {
		if(!inited) {
			input.startLoading();
			
			Threaded.runAfter(500, ()-> {
				Session.generateInvite(server.getId(), result -> input.stopLoading(result.getString("invite_id")));
			});
			
			inited = true;
		}
		super.show();
	}

	@Override
	public void applyStyle(Style style) {
		closeIcon.applyStyle(style);
		preRoot.setBackground(Backgrounds.make(style.getBackgroundPrimary(), new CornerRadii(5, 5, 0, 0, false)));
		bottom.setBackground(Backgrounds.make(style.getBackgroundSecondary(), new CornerRadii(0, 0, 5, 5, false)));

		closeIcon.fillProperty().bind(Bindings.when(closeIcon.hoverProperty()).then(style.getHeaderPrimary())
				.otherwise(style.getHeaderSecondary()));

		head.setFill(style.getHeaderPrimary());
		share.setFill(style.getHeaderSecondary());
		expires.setFill(style.getHeaderSecondary());
		check.setTextFill(style.getHeaderSecondary());

		linkSettings.setFill(style.getHeaderSecondary());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
