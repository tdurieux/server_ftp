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
    /* Parameters */
	private ServerSocket transfertServerSocket;
	private FTPTransfertClient transfertClient;


	/**
	 * Constructor
	 * @throws ServerSocketException when unable to create the transfert server socket
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
	 * @throws SocketException when unable to create the transfert server socket
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
     * @see FTPTransfertServer
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
     * @see FTPTransfertServer
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
     * @see FTPTransfertServer
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


    /**
     * @see FTPTransfertServer
     */
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
