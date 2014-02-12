package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Transfert Server
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPTransfertServerImpl implements FTPTransfertServer {
	/* Parameters */
	private ServerSocket transfertServerSocket;
	private FTPTransfertClient transfertClient;

	/**
	 * Constructor
	 * 
	 * @throws ServerSocketException
	 *             when unable to create the transfert server socket
	 */
	public FTPTransfertServerImpl(String address, int port)
			throws ServerSocketException {
		Socket tranfsertSocket;
		try {
			tranfsertSocket = new Socket(address, port);
			// Create transfert client
			this.transfertClient = new FTPTransfertClientImpl(tranfsertSocket);
		} catch (IOException e) {
			throw new ServerSocketException(
					"Unable to create TransfertServer actif mode", e);
		}

	}

	/**
	 * Constructor
	 * 
	 * @throws SocketException
	 *             when unable to create the transfert server socket
	 */
	public FTPTransfertServerImpl() throws SocketException {
		try {
			// Create socket
			transfertServerSocket = new ServerSocket(0);
		} catch (final IOException e) {
			throw new SocketException("Unable to create transfert socket", e);
		}
	}

	/**
	 * Start the transfert server
	 * 
	 * @throws SocketException
	 */
	private void startServer() throws SocketException {
		try {
			final Socket tranfsertSocket = transfertServerSocket.accept();

			// Create transfert client
			FTPTransfertServerImpl.this.transfertClient = new FTPTransfertClientImpl(
					tranfsertSocket);
		} catch (final IOException e) {
			throw new SocketException("Unable to create FTPTansfertServer", e);
		}
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public int getPublicPort() {
		return transfertServerSocket.getLocalPort();
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public FTPTransfertClient getTransfertClient() {
		return transfertClient;
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public void writeContent(final String content) throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		try {
			// Write message with ASCII encodding
			transfertClient.writeData(content.getBytes("US-ASCII"));
		} catch (final UnsupportedEncodingException e) {
			// Write message without encodding
			transfertClient.writeData(content.getBytes());
		}
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public void writeContent(final byte[] content) throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Write bytes
		transfertClient.writeData(content);
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public void writeContent(InputStream stream) throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content write
		transfertClient.writeData(stream);
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public String readStringContent() throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content write
		return new String(transfertClient.readData());
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public byte[] readContent() throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content read
		return transfertClient.readData();
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public void readContent(OutputStream outputStream) throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}
		transfertClient.readData(outputStream);
	}

	/**
	 * @see FTPTransfertServer
	 */
	@Override
	public void close() throws SocketException {
		try {
			if (transfertServerSocket != null)
				// Close socket
				transfertServerSocket.close();
		} catch (final IOException e) {
			throw new SocketException("Unable to close TransfertServer", e);
		}
	}
}
