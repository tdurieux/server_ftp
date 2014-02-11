package lille1.car2014.durieux_toulet.logs;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtilities {
	private final Logger errorLogger;
	private final Logger logLogger;
	private final Logger debugLogger;

	private static LoggerUtilities INSTANCE;

	private LoggerUtilities() {
		this.errorLogger = Logger.getLogger("errorLogger");
		this.logLogger = Logger.getLogger("logLogger");
		this.debugLogger = Logger.getLogger("debugLogger");
	}

	private static LoggerUtilities getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LoggerUtilities();
		}
		return INSTANCE;
	}

	public static void log(final String message) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		getInstance().logLogger.logp(Level.INFO, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), message);
	}

	public static void error(final String message) {
		getInstance().errorLogger.logp(Level.SEVERE, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), message);
	}

	public static void error(final String message, final Exception exception) {
		getInstance().errorLogger.logp(Level.SEVERE, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), message, exception);

	}

	public static void error(final Exception exception) {
		getInstance().errorLogger.logp(Level.SEVERE, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), null, exception);
	}

	public static void debug(final String message) {
		getInstance().debugLogger.logp(Level.INFO, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), message);
	}
}
