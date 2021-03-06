package test.lille1.car2014.durieux_toulet.ftp_server;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * @author Toulet Cyrille
 */
public class JUnitRunner {

	/**
	 * Main
	 */
	public static void main(final String[] args) {
		/* Test FTPRequestHandler */
		Result result = JUnitCore.runClasses(FTPRequestHandlerTest.class);

		for (final Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		/* Test FTPServer */
		result = JUnitCore.runClasses(FTPServerTest.class);

		for (final Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		/* Test TransfertServer */
		result = JUnitCore.runClasses(FTPTransferSocketTest.class);

		for (final Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		/* Test UserDatabase */
		result = JUnitCore.runClasses(UserDatabaseTest.class);

		for (final Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
	}

}
