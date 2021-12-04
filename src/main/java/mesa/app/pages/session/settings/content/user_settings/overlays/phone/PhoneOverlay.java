package mesa.app.pages.session.settings.content.user_settings.overlays.phone;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mesa.app.pages.session.SessionPage;
import mesa.gui.controls.SplineInterpolator;
import mesa.gui.controls.alert.Overlay;
import mesa.gui.controls.image.IsoPhone;
import mesa.gui.factory.Backgrounds;
import mesa.gui.style.Style;
import mesa.gui.style.Styleable;

public class PhoneOverlay extends Overlay implements Styleable {
	private EnterPhone content;
	private VerifyPhone verify;
	
	private StackPane root;
	private IsoPhone isoPhone;

	public PhoneOverlay(SessionPage owner) {
		super(owner);

		root = new StackPane();
		root.setMaxWidth(472);
		root.setAlignment(Pos.TOP_CENTER);

		root.setClip(new Rectangle(0, -100, 472, 800));

		isoPhone = new IsoPhone(160);
		isoPhone.setTranslateY(-64);

		content = new EnterPhone(owner);

		verify = new VerifyPhone(owner);

		Runnable onInvalid = isoPhone::showError;
		Runnable onValid = isoPhone::showNormal;

		Timeline showNext = new Timeline(new KeyFrame(Duration.seconds(.2),
				new KeyValue(content.opacityProperty(), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(content.scaleXProperty(), .5, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(content.scaleYProperty(), .5, SplineInterpolator.ANTICIPATEOVERSHOOT),

				new KeyValue(verify.opacityProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(verify.scaleXProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(verify.scaleYProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT)));

		Timeline showPrevious = new Timeline(new KeyFrame(Duration.seconds(.2),
				new KeyValue(content.opacityProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(content.scaleXProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(content.scaleYProperty(), 1, SplineInterpolator.ANTICIPATEOVERSHOOT),

				new KeyValue(verify.opacityProperty(), 0, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(verify.scaleXProperty(), .5, SplineInterpolator.ANTICIPATEOVERSHOOT),
				new KeyValue(verify.scaleYProperty(), .5, SplineInterpolator.ANTICIPATEOVERSHOOT)));

		showNext.setOnFinished(e -> {
			content.setCache(false);
			content.setCacheHint(CacheHint.DEFAULT);

			verify.setCache(false);
			verify.setCacheHint(CacheHint.DEFAULT);
		});

		showPrevious.setOnFinished(e -> {
			content.setCache(false);
			content.setCacheHint(CacheHint.DEFAULT);

			verify.setCache(false);
			verify.setCacheHint(CacheHint.DEFAULT);
		});

		Runnable next = () -> {
			verify.clear();

			isoPhone.showSms();
			content.setMouseTransparent(true);
			verify.setMouseTransparent(false);

			content.setCache(true);
			content.setCacheHint(CacheHint.SPEED);
			verify.setCache(true);
			verify.setCacheHint(CacheHint.SPEED);

			showNext.playFromStart();
		};

		Runnable previous = () -> {
			isoPhone.showNormal();
			content.setMouseTransparent(false);
			verify.setMouseTransparent(true);

			content.setCache(true);
			content.setCacheHint(CacheHint.SPEED);
			verify.setCache(true);
			verify.setCacheHint(CacheHint.SPEED);

			showPrevious.playFromStart();
		};

		content.setAction(onValid, onInvalid, next);

		verify.setPrevious(previous);
		verify.setHide(this::hide);

		verify.setAction(isoPhone::showCorrect, isoPhone::showIncorrect, content::getPending);

		root.getChildren().addAll(isoPhone, content, verify);
		
		VBox.setMargin(root, new Insets(40, 0, 0, 0));
		setContent(root);

		applyStyle(owner.getWindow().getStyl());
	}

	public void setOnSuccess(Runnable action) {
		verify.setOnSuccess(action);
	}
	
	@Override
	public void show() {
		content.load();
		super.show();
	}

	@Override
	public void hide() {
		content.unload();
		super.hide();
	}

	@Override
	public void applyStyle(Style style) {
		root.setBackground(Backgrounds.make(style.getBackgroundPrimary(), 5.0));
		isoPhone.applyStyle(style);
	}

	@Override
	public void applyStyle(ObjectProperty<Style> style) {
		Styleable.bindStyle(this, style);
	}

}
