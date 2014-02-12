package lille1.car2014.durieux_toulet.ftp_server;

import java.util.Map;

import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP client used to store the session data of a ftp client
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPClient {

	/**
	 * Set uname
	 * 
	 * @param username
	 *            User name
	 */
	void setUsername(String username);

	/**
	 * Try to connect user
	 * 
	 * @param password
	 *            User password
	 * @throws FTPClientException
	 *             when unable to load user database
	 * @return true if is valid user, false else
	 */
	boolean connect(String password);

	/**
	 * Checks if client is connected
	 * 
	 * @return true if a client is connected, false else
	 */
	boolean isConnected();

	/**
	 * Set type charactor
	 * 
	 * @param type
	 *            Type char
	 */
	void setTypeCharactor(String type);

	/**
	 * Get type charactor
	 * 
	 */
	String getTypeCharactor();

	/**
	 * Get options map
	 * 
	 * @return Options map
	 */
	Map<String, String> getOptions();

	/**
	 * Get current directory
	 * 
	 * @return Current directory
	 */
	String getCurrentDir();

	/**
	 * Set current directory
	 * 
	 * @param currentDir
	 *            Current directory
	 */
	void setCurrentDir(String currentDir);

	/**
	 * Create transfer server
	 * 
	 * @param port
	 *            Server port
	 * @param ip
	 *            IP Address
	 * @throws SocketException
	 *             when unable to create connection
	 * @return Transfer server port
	 */
	int createNewTransfer(String ip, int port) throws SocketException;

	/**
	 * Create transfer server
	 * 
	 * @throws SocketException
	 *             when unable to create connection
	 * @return Transfer server port
	 */
	int createNewTransfer() throws SocketException;

	/**
	 * Get transfer server
	 * 
	 * @return Transfer server
	 */
	FTPTransferSocket getTransferServer();

	/**
	 * Set file to rename
	 * 
	 * @param path
	 *            File to rename
	 */
	void setFileToRename(String path);

	/**
	 * Get file to rename
	 * 
	 * @return String File to rename
	 */
	String getFileToRename();

	/**
	 * Get username
	 * 
	 * @return String Username
	 */
	String getUsername();
}
