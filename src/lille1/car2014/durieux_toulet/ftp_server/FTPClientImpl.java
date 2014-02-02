package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * All commands of the client is interpreted by the class
 * 
 * @author Durieux Thomas
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

	public FTPClientImpl(final Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void writeMessage(final String message) {
		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter(
					this.clientSocket.getOutputStream(),
					Charset.forName("UTF-8"));
			writer.write(message + " \n");
			writer.flush();
		} catch (final IOException e) {
			LoggerUtilities.error(e);
			// throw new SocketException("Unable to write message", e);
		}
	}

	/**
	 * Parse client message and call the function associate to command
	 */
	@Override
	public void readMessage() {
		try {
			this.in = new BufferedReader(new InputStreamReader(
					this.clientSocket.getInputStream()));
			String userInput;

			while ((userInput = this.in.readLine()) != null) {
				try {
					System.out.println(userInput);
					this.requestHandler.parseStringRequest(userInput);
				} catch (final RequestHandlerException e) {
					LoggerUtilities.error(e);
					this.writeMessage("202 " + e.getMessage());
				}
			}
		} catch (final IOException e) {
			LoggerUtilities.error(e);
		}
	}

	public boolean isConnected() {
		return this.isConnected;
	}

	@Override
	public void close() {
		System.out.println("quit quit");
		this.writeMessage("426 Close connection");
		try {
			this.in.close();
			this.clientSocket.close();
		} catch (final IOException e) {
			LoggerUtilities.error(e);
		}
	}

	public void setTypeCharactor(final String typeCharactor) {
		this.typeCharactor = typeCharactor;

	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public boolean connect(final String password) {
		if (password.compareTo("pass") == 0) {
			this.isConnected = true;
			return true;
		} else {
			this.username = null;
			this.isConnected = false;
			return false;
		}
	}

	public Map<String, String> getOptions() {
		return this.options;
	}

	public int createNewTransfert() throws SocketException {
		final TransfertServer transfertHandler = new TransfertServer();
		this.transfertServer = transfertHandler;
		return transfertHandler.getPublicPort();
	}

	public TransfertServer getTransfertServer() {
		return this.transfertServer;
	}

	public String getCurrentDir() {
		return this.currentDir;
	}

	public void setCurrentDir(final String currentDir) {
		this.currentDir = currentDir;
	}

	@Override
	public void run() {
		LoggerUtilities.log("New client "+clientSocket.getRemoteSocketAddress());
		this.requestHandler = new RequestHandler(this);
		final Path currentRelativePath = Paths.get("/");
		this.currentDir = currentRelativePath.toAbsolutePath().toString();
		this.writeMessage("200");
		this.readMessage();
	}
}
