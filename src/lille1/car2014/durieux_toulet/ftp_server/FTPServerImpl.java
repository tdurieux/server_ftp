package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lille1.car2014.durieux_toulet.config.FTPConfiguration;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * Create on new FTP server and start waiting client connection
 * 
 * @author Thomas Durieux
 * @author Toulet Cyrille
 */
public class FTPServerImpl implements FTPServer {
	private final int port;
	private ServerSocket serverSocket;
	private ExecutorService executor;
	private boolean isStarted;

	/**
	 * Constructor with default port
	 */
	public FTPServerImpl() {
		this.port = 21;
	}

	/**
	 * Constructor with custom port
	 * 
	 * @param port
	 *            Server port
	 */
	public FTPServerImpl(final int port) {
		this.port = port;
		isStarted = false;
	}

	/**
	 * Start to waiting new client connection
	 * 
	 * @throws SocketException
	 *             Exception launched if server cannot start or if a client
	 *             connection is interrupted
	 */
	public void startServer() throws ServerSocketException {
		if (isStarted) {
			throw new ServerSocketException("The FTP server is already started");
		}
		try {
			// Create socket
			this.serverSocket = new ServerSocket(this.port);
			isStarted = true;
		} catch (final IOException e) {
			throw new ServerSocketException("Port " + this.port
					+ " already used or reserved by the system.", e);
		}

		// Log server starting
		LoggerUtilities.log("FTP server started on port: " + this.port);

		executor = Executors.newFixedThreadPool(FTPConfiguration.INSTANCE
				.getIntProperty("maxConcurrentUser"));
		// Listen for client connections
		while (true) {
			try {
				final Socket clientSocket = this.serverSocket.accept();

				// Create client thread
				executor.execute(new FTPClientImpl(clientSocket));
			} catch (final IOException e) {
				// The connection with the client is already closed, nothing to
				// do. The ftp client must stay alive and wait new client
				// connection
				LoggerUtilities.error(e);
			}
		}
	}

	/**
	 * Stop the FTP server
	 * @throws ServerSocketException if the server is not started
	 */
	public void closeServer() throws ServerSocketException {
		if (!isStarted) {
			throw new ServerSocketException("The FTP server is not started");
		}
		executor.shutdownNow();
		try {
			this.serverSocket.close();
			isStarted = false;
		} catch (IOException e) {
			throw new ServerSocketException("Unable to close the FTP server", e);
		}
	}

	public int getPort() {
		return port;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
}
