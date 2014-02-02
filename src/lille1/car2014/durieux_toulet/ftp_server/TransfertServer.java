package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Transfert Server
 *
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class TransfertServer {
	private final ServerSocket transfertServerSocket;
	private TransfertClient transfertClient;

	/**
	 * Constructor
	 *
	 * @throws SocketException If unable to create the transfert server socket
	 */
	public TransfertServer() throws SocketException {
		try {
			this.transfertServerSocket = new ServerSocket(0);
		} catch (final IOException e) {
			throw new SocketException("Unable to create transfert socket", e);
		}
	}

	/**
	 * Start the transfert server
	 */
	private void startServer() {
		try {
			final Socket tranfsertSocket = this.transfertServerSocket.accept();
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
		return this.transfertServerSocket.getLocalPort();
	}

	/**
	 * Get the transfert client
	 *
	 * @return The transfer client
	 */
	public TransfertClient getTransfertClient() {
		return this.transfertClient;
	}

	/**
	 * Write content
	 *
	 * @param content Content to write
	 * @throws RequestHandlerException
	 */
	public void writeContent(final String content)
			throws RequestHandlerException {
		if (this.transfertClient == null) {
			this.startServer();
		}
		try {
			TransfertServer.this.transfertClient.writeMessage(content
					.getBytes("US-ASCII"));
		} catch (final UnsupportedEncodingException e) {
			TransfertServer.this.transfertClient.writeMessage(content
					.getBytes());
		}
	}

	/**
	 * Write content
	 *
	 * @param content Content to write
	 * @throws RequestHandlerException
	 */
	public void writeContent(final byte[] content)
			throws RequestHandlerException {
		if (this.transfertClient == null) {
			this.startServer();
		}
		TransfertServer.this.transfertClient.writeMessage(content);
	}

	/**
	 * Read content
	 *
	 * @return Content
	 */
	public String readContent() {
		if (this.transfertClient == null) {
			this.startServer();
		}
		return new String(TransfertServer.this.transfertClient.readMessage());
	}

	/**
	 * Close transfert server
	 */
	public void close() {
		try {
			this.transfertServerSocket.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
