package lille1.car2014.durieux_toulet.ftp_server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Transfert Server
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPTransfertServerImpl implements FTPTransfertServer {
	private ServerSocket transfertServerSocket;
	private FTPTransfertClient transfertClient;

	/**
	 * Constructor
	 * 
	 * @throws ServerSocketException
	 *             If unable to create the transfert server socket
	 */
	public FTPTransfertServerImpl(String address, int port)
			throws ServerSocketException {
		Socket tranfsertSocket;
		try {
			tranfsertSocket = new Socket(address, port);
			// Create transfert client
			FTPTransfertServerImpl.this.transfertClient = new FTPTransfertClient(
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
	 * @throws SocketException 
	 */
	private void startServer() throws SocketException {
		try {
			final Socket tranfsertSocket = transfertServerSocket.accept();

			// Create transfert client
			FTPTransfertServerImpl.this.transfertClient = new FTPTransfertClient(
					tranfsertSocket);
		} catch (final IOException e) {
			throw new SocketException("Unable to create FTPTansfertServer",e);
		}
	}

	/**
	 * Get the public transfert server port
	 * 
	 * @return The transfer server port
	 */
	@Override
	public int getPublicPort() {
		return transfertServerSocket.getLocalPort();
	}

	/**
	 * Get the transfert client
	 * 
	 * @return The transfer client
	 */
	@Override
	public FTPTransfertClient getTransfertClient() {
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
	@Override
	public void writeContent(final String content)
			throws RequestHandlerException, SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		try {
			// Write message with ASCII encodding
			FTPTransfertServerImpl.this.transfertClient.writeMessage(content
					.getBytes("US-ASCII"));
		} catch (final UnsupportedEncodingException e) {
			// Write message without encodding
			FTPTransfertServerImpl.this.transfertClient.writeMessage(content
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
	@Override
	public void writeContent(final byte[] content)
			throws RequestHandlerException, SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Write bytes
		FTPTransfertServerImpl.this.transfertClient.writeMessage(content);
	}

	/**
	 * Read content
	 * 
	 * @return Content
	 * @throws SocketException 
	 */
	@Override
	public String readStringContent() throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content write
		return new String(
				FTPTransfertServerImpl.this.transfertClient.readMessage());
	}

	/**
	 * Read content
	 * 
	 * @return Content
	 * @throws SocketException 
	 */
	@Override
	public byte[] readContent() throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content read
		return FTPTransfertServerImpl.this.transfertClient.readMessage();
	}

	/**
	 * Close transfert server
	 * @throws SocketException 
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

	@Override
	public void writeContent(FileInputStream stream) throws SocketException {
		// Start server if it's stopped
		if (transfertClient == null) {
			this.startServer();
		}

		// Return content write
		FTPTransfertServerImpl.this.transfertClient.writeMessage(stream);
	}
}
