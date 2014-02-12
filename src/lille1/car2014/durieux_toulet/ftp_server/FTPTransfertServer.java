package lille1.car2014.durieux_toulet.ftp_server;

import java.io.FileOutputStream;
import java.io.InputStream;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Transfert server
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPTransfertServer {

	/**
	 * Get the public transfert server port
	 * 
	 * @return The transfer server port
	 */
	int getPublicPort();

	/**
	 * Get the transfert client
	 * 
	 * @return The transfer client
	 */
	FTPTransfertClient getTransfertClient();

	/**
	 * Write content
	 * 
	 * @param content
	 *            Content to write
	 * @throws SocketException
	 *             when unable to write content
	 */
	void writeContent(final String content) throws SocketException;

	/**
	 * Write content
	 * 
	 * @param content
	 *            Content to write
	 * @throws SocketException
	 *             when unable to write content
	 */
	void writeContent(final byte[] content) throws SocketException;

	/**
	 * Write stream on the socket
	 * 
	 * @param stream
	 *            The content stream to write on the socket
	 * @throws RequestHandlerException
	 * @throws SocketException
	 *             when unable to write content
	 */
	void writeContent(InputStream stream) throws SocketException;

	/**
	 * Read a string content
	 * 
	 * @return Content
	 * @throws SocketException
	 *             when unable to read content
	 */
	String readStringContent() throws SocketException;

	/**
	 * Read the content of the data connection and put the result on the
	 * fileOutputStream
	 * 
	 * @param fileOutputStream
	 *            the content of the data connection
	 * @throws SocketException
	 *             when unable to read content
	 */
	void readContent(FileOutputStream fileOutputStream) throws SocketException;

	/**
	 * Read content
	 * 
	 * @return Content
	 * @throws SocketException
	 *             when unable to read content
	 */
	byte[] readContent() throws SocketException;

	/**
	 * Close transfert server
	 * 
	 * @throws SocketException
	 *             when unable to close connection
	 */
	void close() throws SocketException;

}
