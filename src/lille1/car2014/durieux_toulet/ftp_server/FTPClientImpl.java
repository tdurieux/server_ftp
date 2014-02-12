package lille1.car2014.durieux_toulet.ftp_server;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lille1.car2014.durieux_toulet.common.UserDatabase;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP client used to store the session data of a ftp client
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPClientImpl implements FTPClient {
	/* Transfert server */
	private FTPTransferSocket transfertSocket;

	/* Parameters */
	private boolean isConnected = false;
	private String typeCharactor;
	private String username;
	private String currentDir;
	public String fileToRename;
	private final Map<String, String> options = new HashMap<String, String>();

	/**
	 * Constructor
	 */
	public FTPClientImpl() {
		// Create relative path
		final Path currentRelativePath = Paths.get("/");

		// Set current dirrectory
		this.setCurrentDir(currentRelativePath.toAbsolutePath().toString());
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public boolean isConnected() {
		return this.isConnected;
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public void setTypeCharactor(final String typeCharactor) {
		this.typeCharactor = typeCharactor;

	}

	/**
	 * @see FTPClient
	 */
	@Override
	public String getTypeCharactor() {
		return typeCharactor;
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * @see FTPClient
	 */
	@Override
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
	 * @see FTPClient
	 */
	@Override
	public Map<String, String> getOptions() {
		return this.options;
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public int createNewTransfer() throws SocketException {
		// Create transfert server
		this.transfertSocket = new FTPTransferSocketImpl();

		// Return server port
		return transfertSocket.getPublicPort();
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public int createNewTransfer(String address, int port)
			throws SocketException {
		// Create transfert server
		try {
			this.transfertSocket = new FTPTransferSocketImpl(address, port);
			;

			// Return server port
			return port;
		} catch (ServerSocketException e) {
			throw new SocketException("The current client is not found", e);
		}
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public FTPTransferSocket getTransferServer() {
		return this.transfertSocket;
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public String getCurrentDir() {
		return this.currentDir;
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public void setCurrentDir(final String currentDir) {
		this.currentDir = currentDir;
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public void setFileToRename(String path) {
		this.fileToRename = path;
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public String getFileToRename() {
		return fileToRename;
	}

	/**
	 * @see FTPClient
	 */
	@Override
	public String getUsername() {
		return username;
	}
}
