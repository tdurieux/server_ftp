package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Transfert Server
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class TransfertServer {
	private ServerSocket transfertServerSocket;
	private TransfertClient transfertClient;

	/**
	 * Constructor
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * 
	 * @throws SocketException
	 *             If unable to create the transfert server socket
	 */
	public TransfertServer(String address, int port) throws UnknownHostException, IOException {
		final Socket tranfsertSocket = new Socket(address, port);
		// Create transfert client
		TransfertServer.this.transfertClient = new TransfertClient(
				tranfsertSocket);
	}

	/**
	 * Constructor
	 * 
	 * @throws SocketException
	 *             If unable to create the transfert server socket
	 */
	public TransfertServer() throws SocketException {
		try {
			// Create socket
			transfertServerSocket = new ServerSocket(0);
		} catch (final IOException e) {
			throw new SocketException("Unable to create transfert socket", e);
		}
	}

	/**
	 * Start the transfert server
	 */
	private void startServer() {
		try {
			final Socket tranfsertSocket = transfertServerSocket.accept();

			// Create transfert client
			TransfertServer.this.transfertClient = new TransfertClient(
					tranfsertSocket);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get the public transfert server port
	 * 
	 * @return The transfer server port
	 */
	public int getPublicPort() {
		return transfertServerSocket.getLocalPort();
	}

	/**
	 * Get the transfert client
	 * 
	 * @return The transfer client
	 */
	public TransfertClient getTransfertClient() {
		return transfertClient;
	}

	/**
	 * Write content
	 * 
	 * @param content
	 *            Content to write
	 * @throws RequestHandlerException
	 */
	public void writeContent(final String content)
			throws RequestHandlerException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		try {
			// Write message with ASCII encodding
			TransfertServer.this.transfertClient.writeMessage(content
					.getBytes("US-ASCII"));
		} catch (final UnsupportedEncodingException e) {
			// Write message without encodding
			TransfertServer.this.transfertClient.writeMessage(content
					.getBytes());
		}
	}

	/**
	 * Write content
	 * 
	 * @param content
	 *            Content to write
	 * @throws RequestHandlerException
	 */
	public void writeContent(final byte[] content)
			throws RequestHandlerException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Write bytes
		TransfertServer.this.transfertClient.writeMessage(content);
	}

	/**
	 * Read content
	 * 
	 * @return Content
	 */
	public String readStringContent() {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content read
		return new String(TransfertServer.this.transfertClient.readMessage());
	}

	/**
	 * Read content
	 * 
	 * @return Content
	 */
	public byte[] readContent() {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content read
		return TransfertServer.this.transfertClient.readMessage();
	}

	/**
	 * Close transfert server
	 */
	public void close() {
		try {
			// Close socket
			transfertServerSocket.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
