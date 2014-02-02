package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP client interface
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPClient {
	/**
	 * Write message
	 *
	 * @param message Message to write
	 * @throws SocketException When unable to create socket
	 */
	void writeMessage(String message) throws SocketException;

	/**
	 * Read message
	 */
	void readMessage();

	/**
	 * Close connection with server
	 */
	void close();
}
