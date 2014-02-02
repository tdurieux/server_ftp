package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * Create on new FTP server and start waiting client connection
 * 
 * @author Thomas Durieux
 * 
 */
public class FTPServer {
	private final int port;
	private ServerSocket serverSocket;

	public FTPServer() {
		this.port = 21;
	}

	public FTPServer(final int port) {
		this.port = port;
	}

	/**
	 * Start to waiting new client connection
	 * 
	 * @throws SocketException
	 *             Exception launched if server cannot start or if a client
	 *             connection is interrupted
	 */
	public void startServer() throws ServerSocketException {
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (final IOException e) {
			throw new ServerSocketException("Port " + this.port
					+ " already used or reserved by the system.", e);
		}
		LoggerUtilities.log("FTP server started on port: " + this.port);
		while (true) {
			try {
				final Socket clientSocket = this.serverSocket.accept();
				new Thread(new FTPClientImpl(clientSocket)).start();
			} catch (final IOException e) {
				// The connection with the client is already closed, nothing to
				// do. The ftp client must stay alive and wait new client
				// connection
				LoggerUtilities.error(e);
			}
		}

	}
}
