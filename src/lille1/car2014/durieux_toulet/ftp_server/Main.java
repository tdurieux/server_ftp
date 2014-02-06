package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;

import lille1.car2014.durieux_toulet.config.Configuration;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

public class Main {
	public static void main(final String[] args) {

		// Get server port
		int defaultPort = 0;
		// Load default server config
		try {
			Configuration config = new Configuration(Configuration.class
					.getResource("ftp_config.ini").openStream());

			String port = config.getProperty("defaultPort");
			if (port != null) {
				defaultPort = Integer.parseInt(port);
			}
		} catch (IOException e1) {
			defaultPort = 2121;
		}

		// Create and start the ftp server
		final FTPServer server = new FTPServer(defaultPort);

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
