package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.common.Configuration;
import lille1.car2014.durieux_toulet.exception.SocketException;

public class Main {

	public static void main(String[] args) {
		Configuration config = new Configuration(
				"src/lille1/car2014/durieux_toulet/config/ftp_config.ini");

		int defaultPort = Integer.parseInt(config.getProperty("defaultPort"));

		// Create and start the ftp server
		FTPServer server = new FTPServer(defaultPort);
		try {
			server.startServer();
		} catch (SocketException e) {
			// The socket is stopped
			System.exit(0);
		}
	}
}
