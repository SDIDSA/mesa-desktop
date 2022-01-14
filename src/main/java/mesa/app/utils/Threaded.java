package mesa.app.utils;

import java.util.function.BooleanSupplier;

import javafx.application.Platform;
import mesa.gui.exception.ErrorHandler;

public class Threaded {
	private Threaded() {
		
	}
	
	public static void waitWhile(BooleanSupplier condition) {
		while(condition.getAsBoolean()) {
			sleep(5);
		}
	}
	
	public static void sleep(long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException x) {
			ErrorHandler.handle(x, "wait");
			Thread.currentThread().interrupt();
		}
	}

	public static Thread runAfter(int duration, Runnable action) {
		Thread t = new Thread(()-> {
			try {
				Thread.sleep(duration);
				Platform.runLater(action);
			} catch (InterruptedException x) {
				Thread.currentThread().interrupt();
			}
		});
		
		t.start();
		
		return t;
	}
}
