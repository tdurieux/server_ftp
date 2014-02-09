package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.SocketException;

public interface FTPClientSocket {

	/**
	 * Write message
	 * 
	 * @param message
	 *            Message to write
	 * @throws SocketException
	 *             When unable to create socket
	 */
	public abstract void writeMessage(String message);

	/**
	 * Read message
	 */
	public abstract void readMessage();

	public abstract void close() throws SocketException;

	public abstract void startListeningClient();

}