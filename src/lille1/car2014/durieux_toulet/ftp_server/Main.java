package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.config.Configuration;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

public class Main {
	public static void main(final String[] args) {
		// Load default server config
		final Configuration config = new Configuration(Configuration.class
				.getResource("ftp_config.ini").getPath());

		String port = config
				.getProperty("defaultPort");
		// Get server port
		int defaultPort = 2121;		

		if(port!=null) 
			defaultPort = Integer.parseInt(port);
		
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
