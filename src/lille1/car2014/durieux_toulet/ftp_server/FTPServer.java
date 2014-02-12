package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Create on new FTP server and start waiting client connection
 * 
 * @author Thomas Durieux
 * @author Toulet Cyrille
 */
public interface FTPServer {

	/**
	 * Start to waiting new client connection
	 * 
	 * @throws SocketException
	 *             Exception launched if server cannot start or if a client
	 *             connection is interrupted
	 */
	void startServer() throws ServerSocketException;

	/**
	 * Stop to waiting new client connection
	 * 
	 * @throws SocketException
	 *             Exception launched if server cannot stop or if a client
	 *             connection is interrupted
	 */
	void closeServer() throws ServerSocketException;

	/**
	 * Get server port
	 * 
	 * @return The server port
	 */
	int getPort();

	/**
	 * Tell if server is started or not
	 * 
	 * @return True if it's started, else false
	 */
	boolean isStarted();
}
