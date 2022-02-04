package mesa.app.pages.session.types.server.center.right;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.SessionPage;
import mesa.data.bean.User;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MemberGroup extends VBox implements Styleable{
	private SessionPage session;
	
	private MultiText head;
	private Text count;
	
	private StringProperty listSize;
	
	public MemberGroup(SessionPage session, String name) {
		this.session = session;
		
		head = new MultiText(session.getWindow(), name, new Font(12, FontWeight.BOLD));
		head.setTransform(TextTransform.UPPERCASE);
		head.setPadding(new Insets(24, 8, 4, 8));
		
		head.addLabel(" – ");
		
		listSize = new SimpleStringProperty();
		
		count = new Text("", new Font(12, FontWeight.BOLD));
		count.textProperty().bind(listSize);
		
		head.getChildren().add(count);
		
		getChildren().add(head);
		applyStyle(session.getWindow().getStyl());
	}

	public void addUser(User user) {
		getChildren().add(new MemberDisplay(session, user));
		listSize.set(Integer.toString(getChildren().size() - 1));
	}
	
	@Override
	public void applyStyle(Style style) {
		head.setFill(style.getChannelsDefault());
		count.setFill(style.getChannelsDefault());
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
