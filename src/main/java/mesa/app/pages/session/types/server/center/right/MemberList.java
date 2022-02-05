package mesa.app.pages.session.types.server.center.right;

import java.util.HashMap;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.Server;
import mesa.data.bean.User;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MemberList extends StackPane implements Styleable {
	private HashMap<User, MemberDisplay> displayCache = new HashMap<>();
	
	private SessionPage session;

	private VBox list;

	private HashMap<String, MemberGroup> groups;

	public MemberList(SessionPage session, Server server) {
		this.session = session;
		groups = new HashMap<>();

		list = new VBox();
		list.setPadding(new Insets(0, 8, 0, 8));

		server.getMembers().forEach(userId -> User.getForId(userId, this::preAddUser));
		
		getChildren().add(list);
		applyStyle(session.getWindow().getStyl());
	}
	
	private void updateUser(User user) {
		MemberDisplay disp = displayMember(user);
		
		groups.forEach((key, value) -> value.removeUser(disp));
		
		String state = user.isOnline() ? "online" : "offline";

		MemberGroup group = groups.get(state);

		if (group == null) {
			group = new MemberGroup(session, state);
			list.getChildren().add(group);
			groups.put(state, group);
		}
		
		group.addUser(disp);
	}
	
	public void preAddUser(User user) {
		user.onlineProperty().addListener(e -> updateUser(user));
		updateUser(user);
	}
	
	private MemberDisplay displayMember(User user) {
		MemberDisplay found = displayCache.get(user);
		
		if(found == null) {
			found = new MemberDisplay(session, user);
			displayCache.put(user, found);
		}
		
		return found;
	}

	@Override
	public void applyStyle(Style style) {
		setBackground(Backgrounds.make(style.getBackgroundSecondary()));
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
