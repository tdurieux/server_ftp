package lille1.car2014.durieux_toulet.ftp_server;

import java.io.FileInputStream;
import java.io.FileOutputStream;

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
	 * @return The transfer server port
	 */
	int getPublicPort();


	/**
	 * Get the transfert client
	 * @return The transfer client
	 */
	FTPTransfertClient getTransfertClient();


	/**
	 * Write content
	 * @param content Content to write
	 * @throws RequestHandlerException
	 * @throws SocketException when unable to write content
	 */
	void writeContent(final String content)
		throws RequestHandlerException, SocketException;
	

	/**
	 * Write content
	 * @param content Content to write
	 * @throws RequestHandlerException
	 * @throws SocketException when unable to write content
	 */
	void writeContent(final byte[] content)
		throws RequestHandlerException, SocketException;
	

	/**
	 * Write content
	 * @param content The contect stream to write
	 * @throws RequestHandlerException
	 * @throws SocketException when unable to write content
	 */
	void writeContent(FileInputStream stream) throws SocketException;

	/**
	 * Read content
	 * @return Content
	 * @throws SocketException when unable to read content
	 */
	String readStringContent() throws SocketException;
	
	/**
	 * Read the content of the data connection and put the result on the fileOutputStream
	 * @param fileOutputStream the content of the data connection
	 * @throws SocketException when unable to read content
	 */
	void readContent(FileOutputStream fileOutputStream) throws SocketException;
	

	/**
	 * Read content
	 * @return Content
	 * @throws SocketException when unable to read content
	 */
	byte[] readContent() throws SocketException;
	

	/**
	 * Close transfert server
	 * @throws SocketException when unable to close connection
	 */
	void close() throws SocketException;
	
}
