package lille1.car2014.durieux_toulet.ftp_server;

import java.util.Map;

import lille1.car2014.durieux_toulet.exception.FTPClientException;
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
	 * @param message
	 *            Message to write
	 * @throws SocketException
	 *             When unable to create socket
	 */
	void writeMessage(String message);

	/**
	 * Read message
	 */
	void readMessage();

	/**
	 * Close connection with server
	 */
	void close();

	void setUsername(String username);

	boolean connect(String password);

	boolean isConnected();

	void setTypeCharactor(String type);

	Map<String, String> getOptions();

	String getCurrentDir();

	void setCurrentDir(String currentDir);

	int createNewTransfert(String ip, int port) throws SocketException;

	int createNewTransfert() throws SocketException;

	TransfertServerImpl getTransfertServer();

	void setFileToRename(String path);

	String getFileToRename();
}
