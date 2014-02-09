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

public class FTPClientSocketImpl implements Runnable, FTPClientSocket {
	private Socket clientSocket;
	private FTPClient ftpClient;

	public FTPClientSocketImpl(Socket clientSocket) {
		this.clientSocket = clientSocket;
		ftpClient = new FTPClientImpl();
	}

	private OutputStreamWriter getOutputStreamWriter() throws IOException {
		return new OutputStreamWriter(this.clientSocket.getOutputStream(),
				Charset.forName("UTF-8"));
	}

	private InputStreamReader getInputStreamReader() throws IOException {
		return new InputStreamReader(this.clientSocket.getInputStream(),
				Charset.forName("UTF-8"));
	}

	/* (non-Javadoc)
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPClientSocket#writeMessage(java.lang.String)
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

	/* (non-Javadoc)
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPClientSocket#readMessage()
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
					FTPRequestHandler ftpRequestHandlerImpl = FTPRequestHandlerImpl
							.parseStringRequest(userInput, ftpClient, this);
					ftpRequestHandlerImpl.execute();
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

	/* (non-Javadoc)
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPClientSocket#close()
	 */
	@Override
	public void close() throws SocketException {
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			throw new SocketException("Unlable to close client connection", e);
		}
	}

	/* (non-Javadoc)
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPClientSocket#startListeningClient()
	 */
	@Override
	public void startListeningClient() {
		// Write welcome message
		this.writeMessage("200");

		// Wait requests
		this.readMessage();
	}

	@Override
	public void run() {
		// Log client creation
		LoggerUtilities.log("New client "
				+ clientSocket.getRemoteSocketAddress());
		startListeningClient();
	}
}
