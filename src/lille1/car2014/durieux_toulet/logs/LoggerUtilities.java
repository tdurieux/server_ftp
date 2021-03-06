package lille1.car2014.durieux_toulet.logs;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logs manager
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class LoggerUtilities {
	/* Loggers */
	private final Logger errorLogger;
	private final Logger logLogger;
	private final Logger debugLogger;

	/* Instance */
	private static LoggerUtilities INSTANCE;

	/**
	 * Constructor
	 */
	private LoggerUtilities() {
		errorLogger = Logger.getLogger("errorLogger");
		logLogger = Logger.getLogger("logLogger");
		debugLogger = Logger.getLogger("debugLogger");
	}

	/**
	 * Get instance of LoggerUtilities
	 * 
	 * @return LoggerUtilities singleton
	 */
	private static LoggerUtilities getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LoggerUtilities();
		}
		return INSTANCE;
	}

	/**
	 * Log message
	 * 
	 * @param message
	 *            Message to log
	 */
	public static void log(final String message) {
		getInstance().logLogger.logp(Level.INFO, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), "[ LOG ] " + message);
	}

	/**
	 * Log error
	 * 
	 * @param message
	 *            Error message to log
	 */
	public static void error(final String message) {
		getInstance().errorLogger.logp(Level.SEVERE, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), "[ERROR] " + message);
	}

	/**
	 * Log error
	 * 
	 * @param message
	 *            Error message to log
	 * @param exception
	 *            Exception related to error message
	 */
	public static void error(final String message, final Exception exception) {
		getInstance().errorLogger.logp(Level.SEVERE, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), "[ERROR] " + message,
				exception);

	}

	/**
	 * Log error
	 * 
	 * @param exception
	 *            Exception to log
	 */
	public static void error(final Exception exception) {
		getInstance().errorLogger.logp(Level.SEVERE, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), "[ERROR] N/A", exception);
	}

	/**
	 * Log debug message
	 * 
	 * @param message
	 *            Debug message to log
	 */
	public static void debug(final String message) {
		getInstance().debugLogger.logp(Level.INFO, Thread.currentThread()
				.getStackTrace()[2].getClassName(), Thread.currentThread()
				.getStackTrace()[2].getMethodName(), "[DEBUG] " + message);
	}

}
