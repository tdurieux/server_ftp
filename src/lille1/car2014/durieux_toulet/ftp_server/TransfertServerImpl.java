package lille1.car2014.durieux_toulet.ftp_server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Transfert Server
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class TransfertServerImpl implements TransfertServer {
	private ServerSocket transfertServerSocket;
	private TransfertClient transfertClient;

	/**
	 * Constructor
	 * 
	 * @throws ServerSocketException
	 *             If unable to create the transfert server socket
	 */
	public TransfertServerImpl(String address, int port)
			throws ServerSocketException {
		Socket tranfsertSocket;
		try {
			tranfsertSocket = new Socket(address, port);
			// Create transfert client
			TransfertServerImpl.this.transfertClient = new TransfertClient(
					tranfsertSocket);
		} catch (IOException e) {
			throw new ServerSocketException(
					"Unable to create TransfertServer actif mode", e);
		}

	}

	/**
	 * Constructor
	 * 
	 * @throws SocketException
	 *             If unable to create the transfert server socket
	 */
	public TransfertServerImpl() throws SocketException {
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
			TransfertServerImpl.this.transfertClient = new TransfertClient(
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
	 * @throws SocketException
	 */
	public void writeContent(final String content)
			throws RequestHandlerException, SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		try {
			// Write message with ASCII encodding
			TransfertServerImpl.this.transfertClient.writeMessage(content
					.getBytes("US-ASCII"));
		} catch (final UnsupportedEncodingException e) {
			// Write message without encodding
			TransfertServerImpl.this.transfertClient.writeMessage(content
					.getBytes());
		}
	}

	/**
	 * Write content
	 * 
	 * @param content
	 *            Content to write
	 * @throws RequestHandlerException
	 * @throws SocketException
	 */
	public void writeContent(final byte[] content)
			throws RequestHandlerException, SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Write bytes
		TransfertServerImpl.this.transfertClient.writeMessage(content);
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

		// Return content write
		return new String(
				TransfertServerImpl.this.transfertClient.readMessage());
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
		return TransfertServerImpl.this.transfertClient.readMessage();
	}

	/**
	 * Close transfert server
	 */
	public void close() {
		try {
			if (transfertServerSocket != null)
				// Close socket
				transfertServerSocket.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeContent(FileInputStream stream) throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content write
		TransfertServerImpl.this.transfertClient.writeMessage(stream);
	}
}
