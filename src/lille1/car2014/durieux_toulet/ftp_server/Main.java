package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.config.Configuration;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

public class Main {

	public static void main(final String[] args) {
		final Configuration config = new Configuration(Configuration.class
				.getResource("ftp_config.ini").getPath());

		final int defaultPort = Integer.parseInt(config
				.getProperty("defaultPort"));

		// Create and start the ftp server
		final FTPServer server = new FTPServer(defaultPort);
		try {
			server.startServer();
		} catch (final ServerSocketException e) {
			LoggerUtilities.error(e);
			// The socket is stopped
			System.exit(0);
		}
	}
}
