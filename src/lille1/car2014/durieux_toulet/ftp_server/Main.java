package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.config.FTPConfiguration;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

public class Main {
	public static void main(final String[] args) {

		// Get server port
		int defaultPort = 0;
		// Load default server config
		try {
			defaultPort = FTPConfiguration.INSTANCE
					.getIntProperty("defaultPort");
		} catch (Exception e) {
			defaultPort = 2121;
		}

		// Create and start the ftp server
		final FTPServer server = new FTPServerImpl(defaultPort);

		// Start server
		try {
			server.startServer();
		} catch (final ServerSocketException e) {
			// Log error
			LoggerUtilities.error(e);

			// The socket is stopped
			System.exit(0);
		}
	}
}
