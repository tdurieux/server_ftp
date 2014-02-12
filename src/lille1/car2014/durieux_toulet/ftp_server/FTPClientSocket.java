package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP client socket used to read and write the client socket
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPClientSocket {

	/**
	 * Write message to the client
	 * 
	 * @param message
	 *            Message to write
	 */
	void writeMessage(String message);

	/**
	 * Read message from a client
	 */
	void readMessage();

	/**
	 * Close the client socket 
	 * 
	 * @throws SocketException
	 *             when unable to close socket
	 */
	void close() throws SocketException;

	/**
	 * Listen client socket
	 */
	void startListeningClient();

}
