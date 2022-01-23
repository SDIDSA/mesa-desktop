package mesa.app.pages.login;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.function.BiConsumer;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import mesa.api.Session;
import mesa.app.pages.Page;
import mesa.app.pages.session.SessionPage;
import mesa.data.SessionManager;
import mesa.gui.controls.Loading;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;
import mesa.gui.window.Window;

public class LoginPage extends Page {

	private ArrayList<LoginSubPage> subs = new ArrayList<>();

	private Interpolator inter = SplineInterpolator.ANTICIPATEOVERSHOOT;

	private double hideY = -100;
	private double hideScale = .7;
	private double duration = .4;

	private Login login;

	public LoginPage(Window window) {

		super(window, new Dimension(970, 530));
		try {

			Register register = new Register(window);
			login = new Login(window);
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

			register.setOnLogin(data -> {
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

			BiConsumer<JSONObject, Timeline> onSuccess = (user, hide) -> {
				Session.getServers(servers -> {
					JSONArray servarr = servers.getJSONArray("servers");
					window.putServers(servarr);
					window.putData("user", user);
					if (hide != null) {
						hide.setOnFinished(e -> window.loadPage(SessionPage.class));
						hide.playFromStart();
					} else {
						window.loadPage(SessionPage.class);
					}
				});

			};

			login.setOnSuccess(user -> onSuccess.accept(user, hide(login)));
			verify.setOnSuccess(user -> onSuccess.accept(user, hide(verify)));

			getChildren().addAll(login);
			prepare(login);

			applyStyle(window.getStyl());

		} catch (Exception x) {
			x.printStackTrace();
		}
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

	private Timeline show(Node from) {
		return new Timeline(new KeyFrame(Duration.seconds(duration), new KeyValue(from.opacityProperty(), 1, inter),
				new KeyValue(from.translateYProperty(), 0, inter),

				new KeyValue(from.scaleXProperty(), 1, inter), new KeyValue(from.scaleYProperty(), 1, inter)));
	}

	@Override
	public void setup() {
		super.setup();

		Loading loading = new Loading(10);
		loading.setFill(window.getStyl().get().getBackgroundTertiary());
		getChildren().add(0, loading);
		loading.play();

		login.setMouseTransparent(true);

		String token = SessionManager.getSession();
		if (token != null) {
			Session.getUser(result -> {
				if (result.has("user")) {
					Session.getServers(servers -> {
						JSONArray servarr = servers.getJSONArray("servers");
						window.putServers(servarr);
						window.putData("user", result.getJSONObject("user"));
						window.loadPage(SessionPage.class);
						SessionManager.registerSocket(window.getMainSocket(), token);
						loading.stop();
					});
				}
			});
		} else {
			Timeline showLogin = show(login);
			showLogin.setDelay(Duration.seconds(1));
			login.preTransition();
			showLogin.playFromStart();
			showLogin.setOnFinished(e -> {
				login.setMouseTransparent(false);
				login.postTransition();
				loading.stop();
				getChildren().remove(loading);
			});
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		for (LoginSubPage sub : subs) {
			sub.destroy();
		}

		subs.clear();
	}

	@Override
	public void applyStyle(Style style) {
		window.setFill(style.getAccent());
		window.setBorder(Color.TRANSPARENT, 0);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}
}
