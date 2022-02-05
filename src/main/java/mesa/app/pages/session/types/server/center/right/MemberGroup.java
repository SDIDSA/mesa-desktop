package mesa.app.pages.session.types.server.center.right;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.Font;
import mesa.gui.controls.label.MultiText;
import mesa.gui.controls.label.TextTransform;
import mesa.gui.controls.label.unkeyed.Text;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class MemberGroup extends VBox implements Styleable{	
	private MultiText head;
	private Text count;
	
	private IntegerProperty listSize;
	
	public MemberGroup(SessionPage session, String name) {
		head = new MultiText(session.getWindow(), name, new Font(12, FontWeight.BOLD));
		head.setTransform(TextTransform.UPPERCASE);
		head.setPadding(new Insets(24, 8, 4, 8));
		
		head.addLabel(" – ");
		
		listSize = new SimpleIntegerProperty();
		
		count = new Text("", new Font(12, FontWeight.BOLD));
		count.textProperty().bind(listSize.asString());
		
		head.getChildren().add(count);
		
		visibleProperty().bind(listSize.isNotEqualTo(0));
		managedProperty().bind(visibleProperty());
		
		getChildren().add(head);
		applyStyle(session.getWindow().getStyl());
	}

	public void addUser(MemberDisplay user) {
		getChildren().add(user);
		listSize.set(getChildren().size() - 1);
	}
	
	public void removeUser(MemberDisplay user) {
		getChildren().remove(user);
		listSize.set(getChildren().size() - 1);
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
