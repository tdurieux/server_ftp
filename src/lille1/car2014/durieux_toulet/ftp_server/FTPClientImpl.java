package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.exception.FTPClientException;
import lille1.car2014.durieux_toulet.exception.UserDatabaseException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * All commands of the client is interpreted by the class
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPClientImpl implements FTPClient {
	private TransfertServerImpl transfertServer;
	private boolean isConnected = false;
	private String typeCharactor;
	private String username;
	private String currentDir;
	private final Map<String, String> options = new HashMap<String, String>();
	public String fileToRename;

	/**
	 * Constructor
	 * 
	 * @param clientSocket
	 *            The FTP client socket
	 */
	public FTPClientImpl() {
		// Create relative path
		final Path currentRelativePath = Paths.get("/");

		// Set current dirrectory
		this.setCurrentDir(currentRelativePath.toAbsolutePath().toString());
	}

	/**
	 * Tell if client is connected
	 * 
	 * @return true if a client is connected, false else
	 */
	public boolean isConnected() {
		return this.isConnected;
	}

	/**
	 * Set type charactor
	 * 
	 * @param typeCharactor
	 *            Type char
	 */
	public void setTypeCharactor(final String typeCharactor) {
		this.typeCharactor = typeCharactor;

	}

	/**
	 * Set uname
	 * 
	 * @param username
	 *            User name
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Try to connect user
	 * 
	 * @param password
	 *            User password
	 * @throws FTPClientException
	 *             when unable to load user database
	 * @return true if is valid user, false else
	 */
	public boolean connect(final String password) {
		// If correct password
		if (UserDatabase.INSTANCE.loginUser(this.username, password)) {
			// Connect user
			this.isConnected = true;
			return true;
		} else {
			// Sorry, bye !
			this.username = null;
			this.isConnected = false;
			return false;
		}
	}

	/**
	 * Get options
	 * 
	 * @return Options map
	 */
	public Map<String, String> getOptions() {
		return this.options;
	}

	/**
	 * Create transfert server
	 * 
	 * @param port
	 * @param address
	 * 
	 * @throws SocketException
	 *             when unable to create connection
	 * @return Transfert server port
	 */
	public int createNewTransfert() throws SocketException {
		// Create transfert server
		final TransfertServerImpl transfertHandler = new TransfertServerImpl();
		this.transfertServer = transfertHandler;

		// Return server port
		return transfertHandler.getPublicPort();
	}

	/**
	 * Create transfert server
	 * 
	 * @throws SocketException
	 *             when unable to create connection
	 * @return Transfert server port
	 */
	public int createNewTransfert(String address, int port)
			throws SocketException {
		// Create transfert server
		TransfertServerImpl transfertHandler;
		try {
			transfertHandler = new TransfertServerImpl(address, port);
			this.transfertServer = transfertHandler;

			// Return server port
			return port;
		} catch (ServerSocketException e) {
			throw new SocketException("The current client is not found", e);
		}
	}

	/**
	 * Get transfert server
	 * 
	 * @return Transfert server
	 */
	public TransfertServerImpl getTransfertServer() {
		return this.transfertServer;
	}

	/**
	 * Get current dirrectory
	 * 
	 * @return Current dirrectory
	 */
	public String getCurrentDir() {
		return this.currentDir;
	}

	/**
	 * Set current dirrectory
	 * 
	 * @param currentDir
	 *            Current dirrectory
	 */
	public void setCurrentDir(final String currentDir) {
		this.currentDir = currentDir;
	}

	@Override
	public void setFileToRename(String path) {
		this.fileToRename = path;
	}

	@Override
	public String getFileToRename() {
		return fileToRename;
	}
}
