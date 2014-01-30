package lille1.car2014.durieux_toulet.logs;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtilities {
	private final String errorFile;
	private final String logFile;
	private final String debugFile;

	private final Logger errorLogger;
	private final Logger logLogger;
	private final Logger debugLogger;

	private static LoggerUtilities INSTANCE;

	private LoggerUtilities() {
		errorFile = "src/lille1/car2014/durieux_toulet/logs/server_ftp.err";
		logFile = "src/lille1/car2014/durieux_toulet/logs/server_ftp.log";
		debugFile = "src/lille1/car2014/durieux_toulet/logs/server_ftp.debug";

		errorLogger = Logger.getLogger("errorLogger");
		logLogger = Logger.getLogger("logLogger");
		debugLogger = Logger.getLogger("debugLogger");

		try {
			FileHandler fhError = new FileHandler(errorFile);
			FileHandler fhLog = new FileHandler(logFile);
			FileHandler fhDebug = new FileHandler(debugFile);

			errorLogger.addHandler(fhError);
			logLogger.addHandler(fhLog);
			debugLogger.addHandler(fhDebug);

			fhError.setFormatter(new SimpleFormatter());
			fhLog.setFormatter(new SimpleFormatter());
			fhDebug.setFormatter(new SimpleFormatter());

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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

	public static void log(String message) {
		getInstance().logLogger.info(message);
	}

	public static void error(String message) {
		getInstance().logLogger.info(message);
	}

	public static void error(Exception exception) {
		getInstance().logLogger.log(Level.SEVERE, null, exception);
	}

	public static void debug(String message) {
		getInstance().debugLogger.info(message);
	}
}
