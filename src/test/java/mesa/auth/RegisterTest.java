package mesa.auth;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import javafx.beans.property.Property;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import mesa.app.component.input.InputField;
import mesa.app.pages.login.LoginPage;
import mesa.gui.controls.button.AbstractButton;
import mesa.gui.window.Window;

@ExtendWith(ApplicationExtension.class)
class RegisterTest {

	@Start
	private void start(Stage stage) {
		Window window = (Window) stage;
		window.setTitle("mesa");
		window.show();
		window.loadPage(LoginPage.class);
		window.centerOnScreen();
	}
	
	@BeforeEach
	public void beforeEach(FxRobot robot) {
		preRegister(robot);
	}

	@Test
	void emptyFields(FxRobot robot) {
		contin(robot);

		Set<InputField> fields = robot.lookup(".field").queryAll();

		for (InputField field : fields) {
			assertThatErrorIs(field, "field_required");
		}
	}

	@Test
	void usedEmail(FxRobot robot) {
		fillEmail(robot, "zinou.teyar@gmail.com");
		fillUserName(robot, "SDIDSAD");
		fillPassword(robot, "a1b2.a1b2");
		fillBirthDate(robot, 22, 4, 1999);

		contin(robot);

		assertThatErrorIs(robot, ".field.email", "email_used");
	}

	@Test
	void shortPassword(FxRobot robot) {
		fillEmail(robot, "abc.def@ghi.jkl");
		fillUserName(robot, "SDIDSAD");
		fillPassword(robot, "a1b2");
		fillBirthDate(robot, 22, 4, 1999);

		contin(robot);

		assertThatErrorIs(robot, ".field.password", "password_short");
	}

	@Test
	void invalidDate(FxRobot robot) {
		fillEmail(robot, "abc.def@ghi.jkl");
		fillUserName(robot, "SDIDSAD");
		fillPassword(robot, "a1b2.a1b2");
		fillBirthDate(robot, 30, 2, 1999);

		contin(robot);

		assertThatErrorIs(robot, ".field.birth_date", "birth_date_invalid");
	}

	@Test
	void invalidEmail(FxRobot robot) {
		fillEmail(robot, "zinou.teyar");
		fillUserName(robot, "SDIDSAD");
		fillPassword(robot, "a1b2.a1b2");
		fillBirthDate(robot, 22, 04, 1999);

		contin(robot);

		assertThatErrorIs(robot, ".field.email", "email_invalid");
	}

	@Test
	void validRegister(FxRobot robot) {
		fillEmail(robot, "register.test" + (int) (Math.random() * 1000) + "@domain.com");
		fillUserName(robot, "User_Name");
		fillPassword(robot, "a1b2.a1b2");
		fillBirthDate(robot, 25, 1, 1999);

		contin(robot);

		Set<InputField> fields = robot.lookup(".field").queryAll();
		for (InputField field : fields) {
			assertThatErrorIs(field, null);
		}

		Assertions.assertThat(robot.lookup(".butt.login").queryAllAs(AbstractButton.class).size()).isEqualTo(1);
	}
	
	@Stop
	public void stop() throws TimeoutException {
		FxToolkit.hideStage();
	}

	private void preRegister(FxRobot robot) {
		clickLink(robot, "register");
	}

	private void clickLink(FxRobot robot, String name) {
		robot.clickOn(".link." + name, MouseButton.PRIMARY);
	}

	private void contin(FxRobot robot) {
		AbstractButton contin = robot.lookup(".butt.continue").query();
		robot.clickOn(contin, MouseButton.PRIMARY);

		waitFor(contin.loadingProperty(), false);
	}

	private void assertThatErrorIs(FxRobot robot, String query, String expectedError) {
		Assertions.assertThat(robot.lookup(query).queryAs(InputField.class).getErrorKey()).isEqualTo(expectedError);
	}

	private void assertThatErrorIs(InputField field, String expectedError) {
		Assertions.assertThat(field.getErrorKey()).isEqualTo(expectedError);
	}

	private void fillEmail(FxRobot robot, String email) {
		fill(robot, "email", email);
	}

	private void fillUserName(FxRobot robot, String username) {
		fill(robot, "username", username);
	}

	private void fillPassword(FxRobot robot, String password) {
		fill(robot, "password", password);
	}

	private void fillBirthDate(FxRobot robot, int day, int month, int year) {
		fill(robot, "birth_month", zerofy(month));
		fill(robot, "birth_day", zerofy(day));
		fill(robot, "birth_year", zerofy(year));
	}

	private void fill(FxRobot robot, String key, String value) {
		robot.clickOn(".input." + key, MouseButton.PRIMARY);
		robot.write(value);
	}

	private String zerofy(int val) {
		return (val < 10 ? "0" : "") + val;
	}

	private <T> void waitFor(Property<T> property, T value) {
		try {
			WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return property.getValue().equals(value);
				}
			});
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

}
