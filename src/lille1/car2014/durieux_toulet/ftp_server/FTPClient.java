package lille1.car2014.durieux_toulet.ftp_server;

import java.util.Map;

import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP client interface
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
	 * Tell if client is connected
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
	 * Get options
	 * 
	 * @return Options map
	 */
	Map<String, String> getOptions();

	/**
	 * Get current dirrectory
	 * 
	 * @return Current dirrectory
	 */
	String getCurrentDir();

	/**
	 * Set current dirrectory
	 * 
	 * @param currentDir
	 *            Current dirrectory
	 */
	void setCurrentDir(String currentDir);

	/**
	 * Create transfert server
	 * 
	 * @param port
	 *            Server port
	 * @param ip
	 *            IP Adresse
	 * @throws SocketException
	 *             when unable to create connection
	 * @return Transfert server port
	 */
	int createNewTransfert(String ip, int port) throws SocketException;

	/**
	 * Create transfert server
	 * 
	 * @throws SocketException
	 *             when unable to create connection
	 * @return Transfert server port
	 */
	int createNewTransfert() throws SocketException;

	/**
	 * Get transfert server
	 * 
	 * @return Transfert server
	 */
	FTPTransfertServer getTransfertServer();

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
