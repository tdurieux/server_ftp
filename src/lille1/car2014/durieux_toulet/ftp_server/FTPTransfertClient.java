package lille1.car2014.durieux_toulet.ftp_server;

import java.io.InputStream;
import java.io.OutputStream;

import lille1.car2014.durieux_toulet.exception.SocketException;

public interface FTPTransfertClient {

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
	 * Read on data connection and write the content into a stream
	 * 
	 * @param fileOutputStream
	 *            the output stream
	 * @throws SocketException
	 *             Unable to read client data
	 */
	void readData(OutputStream fileOutputStream) throws SocketException;

	/**
	 * Close connection with the client
	 * 
	 * @throws SocketException
	 *             Unable to close data connection
	 */
	void close() throws SocketException;

}