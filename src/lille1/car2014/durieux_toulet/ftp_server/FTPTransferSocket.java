package lille1.car2014.durieux_toulet.ftp_server;

import java.io.InputStream;
import java.io.OutputStream;

import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP Transfert Socket is used to read and write on data socket connection.
 * 
 * @author Thomas Durieux
 * @author Toulet Cyrille
 */
public interface FTPTransferSocket {

	/**
	 * Get the public transfert server port
	 * 
	 * @return The transfer server port
	 */
	int getPublicPort();

	/**
	 * Start the transfert data server (passive mode)
	 * 
	 * @throws SocketException
	 */

	void startServer() throws ServerSocketException;

	/**
	 * Write data on the socket
	 * 
	 * @param bytes
	 *            Bytes of message to write
	 * @throws SocketException
	 *             when unable to write message
	 */
	void writeData(byte[] bytes) throws SocketException;

	/**
	 * Write data on the socket
	 * 
	 * @param data
	 *            Message to write
	 * @throws SocketException
	 *             when unable to write message
	 */
	void writeData(String data) throws SocketException;

	/**
	 * Write data on the socket
	 * 
	 * @param stream
	 *            The input stream to write
	 * @throws SocketException
	 *             when unable to write message
	 */
	void writeData(InputStream stream) throws SocketException;

	/**
	 * Read data on socket
	 * 
	 * @return Bytes of message read
	 * @throws SocketException
	 *             Unable to read client data
	 */
	byte[] readData() throws SocketException;

	/**
	 * Read data on socket
	 * 
	 * @return String of message read
	 * @throws SocketException
	 *             Unable to read client data
	 */
	String readStringData() throws SocketException;

	/**
	 * Read on data connection and write the content into a stream
	 * 
	 * @param fileOutputStream
	 *            the output stream
	 * @throws SocketException
	 *             Unable to read client data
	 */
	void readData(OutputStream fileOutputStream) throws SocketException;

	/**
	 * Close transfert server
	 * 
	 * @throws SocketException
	 *             when unable to close connection
	 */
	void close() throws SocketException;

}
