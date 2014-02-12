package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * FTP client socket used to read and write the client socket
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPClientSocketImpl implements Runnable, FTPClientSocket {
	private Socket clientSocket;
	private FTPClient ftpClient;

	/**
	 * Constructor
	 * 
	 * @param clientSocket
	 *            The client socket
	 */
	public FTPClientSocketImpl(Socket clientSocket) {
		this.clientSocket = clientSocket;
		ftpClient = new FTPClientImpl();
	}

	/**
	 * Get output stream writer
	 * 
	 * @return The output stream writer
	 * @throws IOException
	 *             when unable to get the stream
	 */
	private OutputStreamWriter getOutputStreamWriter() throws IOException {
		return new OutputStreamWriter(this.clientSocket.getOutputStream(),
				Charset.forName("UTF-8"));
	}

	/**
	 * Get input stream reader
	 * 
	 * @return The input stream reader
	 * @throws IOException
	 *             when unable to get the stream
	 */
	private InputStreamReader getInputStreamReader() throws IOException {
		return new InputStreamReader(this.clientSocket.getInputStream(),
				Charset.forName("UTF-8"));
	}

	/**
	 * @see FTPClientSocket
	 */
	@Override
	public void writeMessage(final String message) {
		OutputStreamWriter writer;

		try {
			// Create writer with UTF-8 encoding
			writer = getOutputStreamWriter();
			// Write data
			writer.write(message + " \n");
			// Send data
			writer.flush();
		} catch (IOException e) {
			// Log errors
			LoggerUtilities.error(e);
		}
	}

	/**
	 * @see FTPClientSocket
	 */
	@Override
	public void readMessage() {
		try {
			// Create read buffer
			BufferedReader in = new BufferedReader(getInputStreamReader());

			String userInput;

			// Read user input
			while ((userInput = in.readLine()) != null) {
				try {
					// Print it
					System.out.println(userInput);

					// Try to parse request
					FTPRequestHandler ftpRequestHandler = FTPRequestHandlerImpl
							.parseStringRequest(userInput, ftpClient, this);
					ftpRequestHandler.execute();
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
	 * @see FTPClientSocket
	 */
	@Override
	public void close() throws SocketException {
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			throw new SocketException("Unlable to close client connection", e);
		}
	}

	/**
	 * @see FTPClientSocket
	 */
	@Override
	public void startListeningClient() {
		// Write welcome message
		this.writeMessage("200");

		// Wait requests
		this.readMessage();
	}

	/**
	 * @see FTPClientSocket
	 */
	@Override
	public void run() {
		// Log client creation
		LoggerUtilities.log("New client "
				+ clientSocket.getRemoteSocketAddress());
		startListeningClient();
	}

}
