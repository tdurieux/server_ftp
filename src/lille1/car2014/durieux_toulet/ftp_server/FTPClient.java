package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.Charset;

import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * All commands of the client is interpreted by the class
 * 
 * @author durieux
 */
public class FTPClient {
	private final Socket clientSocket;
	private final boolean isConnected = false;

	public FTPClient(Socket clientSocket) {
		this.clientSocket = clientSocket;
		writeMessage("200");
		readMessage();
	}

	private void writeMessage(String message) {
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
				String[] clientCommandeArray = userInput.split(" ");
				String command = clientCommandeArray[0];

				try {
					Method[] methods = this.getClass().getMethods();
					this.getClass()
							.getMethod(command.toLowerCase(), String[].class)
							.invoke(this, clientCommandeArray);
				} catch (IllegalAccessException e) {
					System.out.println(userInput + " IllegalAccessException");
					writeMessage("421 IllegalAccessException");
				} catch (IllegalArgumentException e) {
					System.out.println(userInput + " IllegalArgumentException");
					writeMessage("421 IllegalAccessException");
				} catch (InvocationTargetException e) {
					System.out.println(userInput + " IllegalAccessException");
					writeMessage("421 InvocationTargetException");
				} catch (NoSuchMethodException e) {
					System.out.println(userInput + " IllegalAccessException");
					writeMessage("421 NoSuchMethodException");
				} catch (SecurityException e) {
					System.out.println(userInput + " IllegalAccessException");
					writeMessage("421 SecurityException");
				}
			}
		} catch (IOException e) {
			LoggerUtilities.error(e);
		}
	}

	/**
	 * Check the login of the user
	 * 
	 * @param args
	 *            username
	 */
	public void user(String[] args) {
		System.out.println("user");
	}
}
