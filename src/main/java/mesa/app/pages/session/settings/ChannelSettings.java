package mesa.app.pages.session.settings;

import javafx.scene.text.FontWeight;
import mesa.api.Session;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.settings.menu.Section;
import mesa.app.pages.session.settings.menu.SectionItem;
import mesa.app.utils.Colors;
import mesa.data.bean.Channel;
import mesa.gui.controls.Font;
import mesa.gui.controls.alert.Alert;
import mesa.gui.controls.alert.AlertType;
import mesa.gui.controls.alert.ButtonType;
import mesa.gui.controls.image.ColorIcon;
import mesa.gui.style.Style;

public class ChannelSettings extends Settings {
	public ChannelSettings(SessionPage session, Channel channel) {
		super(session);
	
		Section mainSection = new Section(this, "  " + channel.getName(), true);
		mainSection.addPreTitle(new ColorIcon("channel_type_" + channel.getType(), 12));
		
		mainSection.addItem(new SectionItem(this, "Overview"));
		mainSection.addItem(new SectionItem(this, "Permissions"));
		mainSection.addItem(new SectionItem(this, "Invites"));
		mainSection.addItem(new SectionItem(this, "Integrations"));
		
		Section deleteSection = new Section(this);
		SectionItem delete = new SectionItem(this, "Delete Channel", () -> {
			Alert confirm = new Alert(session, AlertType.DELETE_CHANNEL);
			confirm.setHead("Delete Channel");
			confirm.addLabel("Are you sure you want to delete ");
			confirm.addLabel(channel.getTypeChar() + channel.getName(), new Font(15, FontWeight.BOLD));
			confirm.addLabel(" ? This canot be undone.");
			confirm.setBodyFill(Style::getHeaderPrimary, session.getWindow().getStyl().get());
			
			confirm.addAction(ButtonType.DELETE_CHANNEL, ()-> {
				confirm.startLoading(ButtonType.DELETE_CHANNEL);
				Session.deleteChannel(channel.getId(), channel.getGroup().getServer().getId(), result -> {
					confirm.stopLoading(ButtonType.DELETE_CHANNEL);
					confirm.addOnHidden(()-> session.hideSettings(this));
					confirm.hide();
				});
			});
			
			confirm.show();
		});
		delete.setTextFill(Colors.Error);
		deleteSection.addItem(delete);
		
		sideBar.addSection(mainSection);
		sideBar.separate();
		sideBar.addSection(deleteSection);
		sideBar.separate();
	}
}
