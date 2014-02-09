package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;

public interface FTPServer {

	/**
	 * Start to waiting new client connection
	 * 
	 * @throws SocketException
	 *             Exception launched if server cannot start or if a client
	 *             connection is interrupted
	 */
	void startServer() throws ServerSocketException;

	void closeServer() throws ServerSocketException;
	
	int getPort();
	
	boolean isStarted();
}
