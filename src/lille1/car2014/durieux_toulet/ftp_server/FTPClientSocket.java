package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP client socket interface
 *
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPClientSocket {

	/**
	 * Write message
	 * @param message Message to write
	 */
	public abstract void writeMessage(String message);


	/**
	 * Read message
	 */
	public abstract void readMessage();


	/**
	 * Close socket 
	 * @throws SocketException when unable to close socket
	 */
	public abstract void close() throws SocketException;


	/**
	 * Listen client
	 */
	public abstract void startListeningClient();

}
