package lille1.car2014.durieux_toulet.logs;

import java.io.IOException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtilities {
	private String errorFile;
	private String logFile;
	private String debugFile;

	private final Logger errorLogger;
	private final Logger logLogger;
	private final Logger debugLogger;

	private static LoggerUtilities INSTANCE;

	private LoggerUtilities() {
		try {
			this.errorFile = LoggerUtilities.class
					.getResource("server_ftp.err").getPath();
			this.logFile = LoggerUtilities.class.getResource("server_ftp.log")
					.getPath();
			this.debugFile = LoggerUtilities.class.getResource(
					"server_ftp.debug").getPath();
		} catch (NullPointerException e) {
			this.errorFile = LoggerUtilities.class.getResource("").getPath()
					+ "/server_ftp.err";
			this.logFile = LoggerUtilities.class.getResource("").getPath()
					+ "/server_ftp.log";
			this.debugFile = LoggerUtilities.class.getResource("").getPath()
					+ "/server_ftp.debug";
		}
		this.errorLogger = Logger.getLogger("errorLogger");
		this.logLogger = Logger.getLogger("logLogger");
		this.debugLogger = Logger.getLogger("debugLogger");

		try {
			final FileHandler fhError = new FileHandler(this.errorFile);

			final FileHandler fhLog = new FileHandler(this.logFile);
			final FileHandler fhDebug = new FileHandler(this.debugFile);

			this.errorLogger.addHandler(fhError);
			this.logLogger.addHandler(fhLog);
			this.debugLogger.addHandler(fhDebug);

			fhError.setFormatter(new SimpleFormatter());
			fhLog.setFormatter(new SimpleFormatter());
			fhDebug.setFormatter(new SimpleFormatter());

		} catch (final SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static LoggerUtilities getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LoggerUtilities();
		}
		return INSTANCE;
	}

	public static void log(final String message) {
		StackTraceElement[] stackTrace = Thread.currentThread()
				.getStackTrace();
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
