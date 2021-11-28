package mesa.app.utils;

import java.util.function.BooleanSupplier;

import mesa.gui.exception.ErrorHandler;

public class Threaded {
	private Threaded() {
		
	}
	
	public static void waitWhile(BooleanSupplier condition) {
		while(condition.getAsBoolean()) {
			sleep(10);
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
}
