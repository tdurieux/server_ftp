package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.Charset;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * All commands of the client is interpreted by the class
 * 
 * @author durieux
 */
public class FTPClient {
	private final Socket clientSocket;
	private RequestHandler requestHandler;
	private boolean isConnected = false;
	private String typeCharactor;
	private String username;

	public FTPClient(Socket clientSocket) {
		this.clientSocket = clientSocket;
		requestHandler = new RequestHandler(this);
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
			// TODO Auto-generated catch block
			LoggerUtilities.error(e);
		}
	}

	/**
	 * Parse client message and call the function associate to command
	 */
	private void readMessage() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
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
		this.writeMessage("QUIT");
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
