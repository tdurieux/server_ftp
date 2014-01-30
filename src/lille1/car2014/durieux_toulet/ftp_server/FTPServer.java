package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * 
 * @author durieux
 * 
 */
public class FTPServer {
	private final int port;
	private ServerSocket serverSocket;

	public FTPServer(int port) {
		this.port = port;
	}

	public void startServer() throws SocketException {
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				new FTPClient(clientSocket);
			}

		} catch (IOException e) {
			LoggerUtilities.error(e);
			throw new SocketException(e);
		}
	}
}
