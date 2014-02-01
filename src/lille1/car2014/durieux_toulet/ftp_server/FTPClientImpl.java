package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
public class FTPClientImpl implements FTPClient{
	private final Socket clientSocket;
	private RequestHandler requestHandler;
	private BufferedReader in;
	private TransfertServer transfertServer;
	private boolean isConnected = false;
	private String typeCharactor;
	private String username;
	private String currentDir;
	
	private Map<String, String> options = new HashMap<String, String>();

	
	public FTPClientImpl(Socket clientSocket) {
		this.clientSocket = clientSocket;
		requestHandler = new RequestHandler(this);
		Path currentRelativePath = Paths.get("");
		currentDir = currentRelativePath.toAbsolutePath().toString();
		writeMessage("200");
		readMessage();
	}

	public void writeMessage(String message) {
		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter(clientSocket.getOutputStream(),
					Charset.forName("UTF-8"));
			writer.write(message + " \n");
			writer.flush();
		} catch (IOException e) {
			LoggerUtilities.error(e);
			//throw new SocketException("Unable to write message", e);
		}
	}

	/**
	 * Parse client message and call the function associate to command
	 */
	public void readMessage() {
		try {
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			String userInput;
			
			while ((userInput = in.readLine()) != null) {
				System.out.println(userInput);
				try {
					requestHandler.parseStringRequest(userInput);
				} catch (RequestHandlerException e) {
					LoggerUtilities.error(e);
					this.writeMessage("421 " + e.getMessage());
				}
			}
		} catch (IOException e) {
			LoggerUtilities.error(e);
		}
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void close() {
		this.writeMessage("426 Close connection");
		try {
			this.in.close();
			this.clientSocket.close();
		} catch (IOException e) {
			LoggerUtilities.error(e);
		}
	}

	public void setTypeCharactor(String typeCharactor) {
		this.typeCharactor = typeCharactor;

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean connect(String password) {
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
		return options;
	}

	public int createNewTransfert() throws SocketException {
		TransfertServer transfertHandler = new TransfertServer();
		this.transfertServer = transfertHandler;
		return transfertHandler.getPublicPort();
	}
	
	public TransfertServer getTransfertServer() {
		return transfertServer;
	}
	public String getCurrentDir() {
		return currentDir;
	}
	public void setCurrentDir(String currentDir) {
		this.currentDir = currentDir;
	}
}
