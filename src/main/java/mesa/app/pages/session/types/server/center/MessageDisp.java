package mesa.app.pages.session.types.server.center;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mesa.app.pages.session.SessionPage;
import mesa.app.utils.Threaded;
import mesa.data.bean.Message;
import mesa.data.bean.User;
import mesa.gui.controls.Font;
import mesa.gui.controls.image.ImageProxy;
import mesa.gui.controls.input.RichText;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MessageDisp extends HBox implements Styleable {
	private Message message;

	private ImageView pfp;
	private Text senderName;
	private Text time;
	private MultiText messageContent;

	public MessageDisp(SessionPage session, Message message) {
		super(16);
		this.message = message;

		pfp = new ImageView();
		pfp.setFitWidth(40);
		pfp.setFitHeight(40);

		senderName = new Text("", new Font(Font.DEFAULT_FAMILY_MEDIUM, 16));

		time = new Text("", new Font(Font.DEFAULT_FAMILY_MEDIUM, 12));

		messageContent = new MultiText(session.getWindow(), "", new Font(15));
		RichText.applyText(messageContent, message.getContent(), new Font(15));

		HBox header = new HBox(8);
		header.setAlignment(Pos.BOTTOM_LEFT);

		header.getChildren().addAll(senderName, time);

		VBox content = new VBox(6);
		content.getChildren().addAll(header, messageContent);

		getChildren().addAll(pfp, content);

		User.getForId(message.getSender(), user -> {
			ImageProxy.asyncLoad(user.getAvatar(), 40, img -> pfp.setImage(img));
			senderName.textProperty().bind(user.usernameProperty());
		});
		
		message.timeProperty().addListener(e -> {
			if(timeUpdater != null) {
				timeUpdater.interrupt();
			}
			updateTime();
		});
		
		updateTime();

		applyStyle(session.getWindow().getStyl());
	}

	private Thread timeUpdater;
	private synchronized void updateTime() {
		time.setText(TextTransform.FORMAT_TIME.apply(message.getTime()));
		timeUpdater = Threaded.runAfter(5000, this::updateTime);
	}

	@Override
	public void applyStyle(Style style) {
		senderName.setFill(style.getHeaderPrimary());
		time.setFill(style.getTextMuted());
		messageContent.setFill(style.getTextNormal());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}