package mesa.app.pages.session.settings;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mesa.app.pages.session.SessionPage;
import mesa.app.pages.session.settings.content.SettingsContent;
import mesa.app.pages.session.settings.menu.SettingsMenu;
import mesa.data.User;
import mesa.gui.controls.Font;
import mesa.gui.controls.scroll.ScrollBar;
import mesa.gui.controls.space.ExpandingHSpace;
import mesa.gui.controls.space.FixedHSpace;
import mesa.gui.controls.space.FixedVSpace;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class Settings extends StackPane implements Styleable {
	private SessionPage session;

	private ExpandingHSpace leftBack, rightBack;

	private StackPane content;
	private SettingsMenu sideBar;

	private ScrollBar sideSb, mainSb;
	private HBox root;
	
	private Text ESC;

	public Settings(SessionPage session) {
		this.session = session;
		setMinHeight(0);
		maxHeightProperty().bind(session.heightProperty());

		HBox back = new HBox();
		leftBack = new ExpandingHSpace();
		rightBack = new ExpandingHSpace();
		back.getChildren().setAll(leftBack, rightBack);
		getChildren().add(back);
		root = new HBox();
		root.setMinHeight(0);
		root.maxHeightProperty().bind(heightProperty());
		root.setAlignment(Pos.TOP_CENTER);
		root.setPadding(new Insets(0, 8, 0, 0));

		content = new StackPane();
		HBox.setHgrow(content, Priority.ALWAYS);
		content.setPadding(new Insets(60, 40, 80, 40));
		content.setMaxWidth(740);
		content.setMinHeight(1200);

		sideBar = new SettingsMenu(this);

		sideSb = new ScrollBar(6, 1);
		sideSb.install(root, sideBar);

		sideSb.bindOpacityToHover(sideBar);

		VBox exitCont = new VBox();
		exitCont.setAlignment(Pos.TOP_CENTER);

		CloseIcon close = new CloseIcon(this);
		
		ESC = new Text("ESC");
		ESC.setFont(new Font(Font.DEFAULT_FAMILY_MEDIUM, 13).getFont());
		
		exitCont.getChildren().addAll(new FixedVSpace(60), close, new FixedVSpace(8), ESC);

		mainSb = new ScrollBar(16, 4);
		mainSb.install(root, content);
		
		StackPane.setAlignment(mainSb, Pos.CENTER_RIGHT);

		root.getChildren().addAll(sideBar, sideSb, content, exitCont, new FixedHSpace(31));

		getChildren().addAll(root, mainSb);

		applyStyle(session.getWindow().getStyl());
	}
	
	public HBox getRoot() {
		return root;
	}
	
	public void loadContent(SettingsContent content) {
		this.content.getChildren().setAll(content);
	}

	public SessionPage getSession() {
		return session;
	}
	
	public Window getWindow() {
		return session.getWindow();
	}
	
	public User getUser() {
		return session.getUser();
	}

	@Override
	public void applyStyle(Style style) {
		rightBack.setBackground(Backgrounds.make(style.getBack1()));
		leftBack.setBackground(Backgrounds.make(style.getBack2()));

		sideSb.setThumbFill(style.getBack3());
		mainSb.setThumbFill(style.getBack3());
		mainSb.setTrackFill(style.getBack2());

		content.setBackground(rightBack.getBackground());
		
		ESC.setFill(style == Style.DARK ? Color.web("#72767d"):null);
	}
}
