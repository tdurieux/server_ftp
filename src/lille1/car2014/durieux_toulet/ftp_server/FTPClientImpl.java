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
public class FTPClientImpl implements FTPClient, Runnable {
	private final Socket clientSocket;
	private RequestHandler requestHandler;
	private BufferedReader in;
	private TransfertServer transfertServer;
	private boolean isConnected = false;
	private String typeCharactor;
	private String username;
	private String currentDir;
	private final Map<String, String> options = new HashMap<String, String>();
	public String fileToRename;

	/**
	 * Constructor
	 *
	 * @param clientSocket The FTP client socket
	 */
	public FTPClientImpl(final Socket clientSocket) {
		// Copy client socket
		this.clientSocket = clientSocket;
	}

	/**
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPClient
	 * @Override
	 */
	public void writeMessage(final String message) {
		OutputStreamWriter writer;

		try {
			// Create writer with UTF-8 encoding
			writer = new OutputStreamWriter(
					this.clientSocket.getOutputStream(),
					Charset.forName("UTF-8"));

			// Write data
			writer.write(message + " \n");

			// Send data
			writer.flush();
		} catch (final IOException e) {
			// Log errors
			LoggerUtilities.error(e);

			// throw new SocketException("Unable to write message", e);
		}
	}

	/**
	 * Parse client message and call the function associate to command
	 *
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPClient
	 * @Override
	 */
	public void readMessage() {
		try {
			// Create read buffer
			this.in = new BufferedReader(new InputStreamReader(
					this.clientSocket.getInputStream()));

			String userInput;

			// Read user input
			while ((userInput = this.in.readLine()) != null) {
				try {
					// Print it
					System.out.println(userInput);

					// Try to parse request
					this.requestHandler.parseStringRequest(userInput);
				} catch (final RequestHandlerException e) {
					// Log errors
					LoggerUtilities.error(e);

					// Print errors
					this.writeMessage("202 " + e.getMessage());
				}
			}
		} catch (final IOException e) {
			// Log errors
			LoggerUtilities.error(e);
		}
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
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPClient
	 * @Override
	 */
	public void close() {
		// Print close message
		System.out.println("quit quit");
		this.writeMessage("426 Close connection");

		try {
			// Close buffer reader
			this.in.close();

			// Close socket
			this.clientSocket.close();
		} catch (final IOException e) {
			// Log errors
			LoggerUtilities.error(e);
		}
	}

	/**
	 * Set type charactor
	 *
	 * @param typeCharactor Type char
	 */
	public void setTypeCharactor(final String typeCharactor) {
		this.typeCharactor = typeCharactor;

	}

	/**
	 * Set uname
	 *
	 * @param username User name
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * Try to connect user
	 *
	 * @param password User password
	 * @throws FTPClientException when unable to load user database
	 * @return true if is valid user, false else
	 */
	public boolean connect(final String password) throws FTPClientException {
		try {
			// If correct password
			if (UserDatabase.getInstance ().signin (this.username, password)) {
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
		catch (UserDatabaseException e) {
			throw new FTPClientException ("Unable to load user database", e);
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
	 * @param port 
	 * @param address 
	 *
	 * @throws SocketException when unable to create connection
	 * @return Transfert server port
	 */
	public int createNewTransfert() throws SocketException {
		// Create transfert server
		final TransfertServer transfertHandler = new TransfertServer();
		this.transfertServer = transfertHandler;

		// Return server port
		return transfertHandler.getPublicPort();
	}
	
	/**
	 * Create transfert server
	 *
	 * @throws SocketException when unable to create connection
	 * @return Transfert server port
	 */
	public int createNewTransfert(String address,int port) throws SocketException {
		// Create transfert server
		TransfertServer transfertHandler;
		try {
			transfertHandler = new TransfertServer(address,port);
			this.transfertServer = transfertHandler;

			// Return server port
			return transfertHandler.getPublicPort();
		} catch (UnknownHostException e) {
			throw new SocketException("The current client is not found", e);
		} catch (IOException e) {
			throw new SocketException("Unable to create data connection", e);
		}
	}

	/**
	 * Get transfert server
	 *
	 * @return Transfert server
	 */
	public TransfertServer getTransfertServer() {
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
	 * @param currentDir Current dirrectory
	 */
	public void setCurrentDir(final String currentDir) {
		this.currentDir = currentDir;
	}

	/**
	 * Run client
	 *
	 * @Override
	 */
	public void run() {
		// Log client creation
		LoggerUtilities.log("New client "+clientSocket.getRemoteSocketAddress());

		// Create request handler
		this.requestHandler = new RequestHandler(this);

		// Create relative path
		final Path currentRelativePath = Paths.get("/");

		// Set current dirrectory
		this.setCurrentDir (currentRelativePath.toAbsolutePath().toString());

		// Write welcome message
		this.writeMessage("200");

		// Wait requests
		this.readMessage();
	}
}
