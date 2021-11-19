package mesa.app.pages.session;

import java.awt.Dimension;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mesa.app.pages.Page;
import mesa.app.pages.session.content.Content;
import mesa.app.pages.session.settings.Settings;
import mesa.data.User;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class SessionPage extends Page {
	private User user;

	private double duration = .4;

	private StackPane side, main;
	
	private HBox root;
	private Settings settings;
	
	private Timeline showSettings, hideSettings;
	
	private Interpolator inter;
	
	public SessionPage(Window window) {
		super(window, new Dimension(970, 530));

		inter = new SplineInterpolator(0.68, -0.6, 0.32, 1.6);
		
		user = new User(window.getJsonData("user"));
		
		root = new HBox();
		
		settings = new Settings(this);
		settings.setOpacity(0);
		settings.getRoot().setScaleX(1.1);
		settings.getRoot().setScaleY(1.1);
		
		ServerBar servers = new ServerBar(this);
		
		side = new StackPane();
		side.setMinWidth(240);
		side.setBackground(Backgrounds.make(window.getStyl().getBack2(), new CornerRadii(8.0, 0, 0, 0, false)));
		side.setAlignment(Pos.TOP_CENTER);
		
		main = new StackPane();
		main.setBackground(Backgrounds.make(window.getStyl().getBack1()));
		main.setAlignment(Pos.TOP_CENTER);
		HBox.setHgrow(main, Priority.ALWAYS);
		
		root.getChildren().addAll(servers, side, main);
		
		showSettings = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(root.opacityProperty(), 0, inter),
				new KeyValue(root.scaleXProperty(), 0.93, inter),
				new KeyValue(root.scaleYProperty(), 0.93, inter),
				
				new KeyValue(settings.opacityProperty(), 1, inter),
				new KeyValue(settings.getRoot().scaleXProperty(), 1, inter),
				new KeyValue(settings.getRoot().scaleYProperty(), 1, inter)
			));
		showSettings.setOnFinished(e-> {
			afterTransition();
			getChildren().remove(root);
		});
		
		hideSettings = new Timeline(new KeyFrame(Duration.seconds(duration),
				new KeyValue(root.opacityProperty(), 1, inter),
				new KeyValue(root.scaleXProperty(), 1, inter),
				new KeyValue(root.scaleYProperty(), 1, inter),
				
				new KeyValue(settings.opacityProperty(), 0, inter),
				new KeyValue(settings.getRoot().scaleXProperty(), 1.1, inter),
				new KeyValue(settings.getRoot().scaleYProperty(), 1.1, inter)
			));
		
		hideSettings.setOnFinished(e-> {
			afterTransition();
			getChildren().remove(settings);
		});
		
		getChildren().add(root);
		applyStyle(window.getStyl());
	}
	
	private void prepareNode(Node node) {
		node.setCache(true);
		node.setCacheHint(CacheHint.SPEED);
	}
	
	private void clearNode(Node node) {
		node.setCache(false);
		node.setCacheHint(CacheHint.DEFAULT);
	}
	
	private void beforeTransition() {
		prepareNode(root);
		prepareNode(settings);
		prepareNode(settings.getRoot());
	}
	
	private void afterTransition() {
		clearNode(root);
		clearNode(settings);
		clearNode(settings.getRoot());
	}
	
	public void showSettings() {
		beforeTransition();
		if(!getChildren().contains(settings)) {
			getChildren().add(settings);
		}
		root.setMouseTransparent(true);
		settings.setMouseTransparent(false);
		hideSettings.stop();
		showSettings.playFromStart();
	}
	
	public void hideSettings() {
		beforeTransition();
		if(!getChildren().contains(root)) {
			getChildren().add(root);
		}
		settings.setMouseTransparent(true);
		root.setMouseTransparent(false);
		showSettings.stop();
		hideSettings.playFromStart();
	}
	
	public User getUser() {
		return user;
	}
	
	public void load(Content content) {
		side.getChildren().setAll(content.getSide());
		main.getChildren().setAll(content.getMain());
	}

	@Override
	public void applyStyle(Style style) {
		window.setFill(style.getBack3());
		window.setBorder(Color.web("#494a4d"), 1);
	}

}
