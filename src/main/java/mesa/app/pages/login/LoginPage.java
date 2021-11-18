package mesa.app.pages.login;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.json.JSONObject;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mesa.app.pages.Page;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.style.Style;
import mesa.gui.window.Window;

public class LoginPage extends Page {

	private ArrayList<LoginSubPage> subs = new ArrayList<LoginSubPage>();;

	private Interpolator inter;

	private double hideY = -100, hideScale = .7, duration = .4;

	public LoginPage(Window window) {
		super(window, new Dimension(970, 530));

		inter = new SplineInterpolator(0.68, -0.6, 0.32, 1.6);

		Register register = new Register(window);
		Login login = new Login(window);
		Verify verify = new Verify(window);

		subs.add(register);
		subs.add(login);
		subs.add(verify);

		prepare(register);
		prepare(verify);

		Timeline showRegister = fromTo(login, register);
		showRegister.setOnFinished(e -> {
			login.postTransition();
			register.postTransition();
			getChildren().remove(login);
		});

		Timeline hideRegister = fromTo(register, login);
		hideRegister.setOnFinished(e -> {
			login.postTransition();
			register.postTransition();
			getChildren().remove(register);
		});

		Timeline showVerify = fromTo(login, verify);
		showVerify.setOnFinished(e -> {
			login.postTransition();
			verify.postTransition();
			getChildren().remove(login);
		});

		Timeline hideVerify = fromTo(verify, login);
		hideVerify.setOnFinished(e -> {
			login.postTransition();
			verify.postTransition();
			getChildren().remove(verify);
		});

		login.setOnRegister(() -> {
			register.preTransition();
			login.preTransition();
			
			getChildren().add(register);
			register.setMouseTransparent(false);
			login.setMouseTransparent(true);
			showVerify.stop();
			hideVerify.stop();
			hideRegister.stop();
			showRegister.playFromStart();
		});

		register.setOnLogin((data) -> {
			register.preTransition();
			login.preTransition();
			getChildren().add(login);
			login.setMouseTransparent(false);
			register.setMouseTransparent(true);
			showVerify.stop();
			hideVerify.stop();
			showRegister.stop();
			hideRegister.playFromStart();

			if (data != null) {
				login.loadData(data);
			}
		});

		login.setOnVerify(user -> {
			verify.preTransition();
			login.preTransition();
			
			getChildren().add(verify);
			verify.setMouseTransparent(false);
			register.setMouseTransparent(true);
			hideVerify.stop();
			showRegister.stop();
			hideRegister.stop();
			showVerify.playFromStart();

			verify.loadData(user);
		});

		verify.setOnLogout(() -> {
			verify.preTransition();
			login.preTransition();
			getChildren().add(login);
			login.setMouseTransparent(false);
			verify.setMouseTransparent(true);
			showVerify.stop();
			showRegister.stop();
			hideRegister.stop();
			hideVerify.playFromStart();
		});

		ParallelTransition pt = new ParallelTransition();
		pt.getChildren().addAll(hide(login), hide(verify));

		Consumer<JSONObject> onSuccess = (user) -> {
			window.putData("user", user);
			pt.setOnFinished(e -> {
				window.loadPage(new SessionPage(window));
			});
			pt.playFromStart();
		};

		login.setOnSuccess(onSuccess);
		verify.setOnSuccess(onSuccess);

		getChildren().addAll(login);

		applyStyle(window.getStyl());
	}

	private void prepare(Node node) {
		node.setMouseTransparent(true);
		node.setOpacity(0);
		node.setTranslateY(hideY);
		node.setScaleX(hideScale);
		node.setScaleY(hideScale);
	}

	private Timeline fromTo(Node from, Node to) {
		return new Timeline(new KeyFrame(Duration.seconds(duration), new KeyValue(from.opacityProperty(), 0, inter),
				new KeyValue(to.opacityProperty(), 1, inter), new KeyValue(from.translateYProperty(), hideY, inter),
				new KeyValue(to.translateYProperty(), 0, inter),

				new KeyValue(from.scaleXProperty(), hideScale, inter),
				new KeyValue(from.scaleYProperty(), hideScale, inter), new KeyValue(to.scaleXProperty(), 1.0, inter),
				new KeyValue(to.scaleYProperty(), 1.0, inter)));
	}

	private Timeline hide(Node from) {
		return new Timeline(new KeyFrame(Duration.seconds(duration), new KeyValue(from.opacityProperty(), 0, inter),
				new KeyValue(from.translateYProperty(), hideY, inter),

				new KeyValue(from.scaleXProperty(), hideScale, inter),
				new KeyValue(from.scaleYProperty(), hideScale, inter)));
	}

	@Override
	public void applyStyle(Style style) {
		window.setFill(style.getAccent());
		window.setBorder(Color.TRANSPARENT, 0);
	}

	@Override
	public void destroy() {
		super.destroy();

		for (LoginSubPage sub : subs) {
			sub.destroy();
		}

		subs.clear();
	}
}
