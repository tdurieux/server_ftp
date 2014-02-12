package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP Transfert Socket is used to read and write on data socket connection. The
 * class support the active and passive FTP mode.
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPTransferSocketImpl implements FTPTransferSocket {

	/* Attributes */
	private ServerSocket transfertServerSocket;
	private Socket transferSocket;

	/**
	 * Constructor active mode
	 * 
	 * @throws ServerSocketException
	 *             when unable to create the transfert server socket
	 */
	public FTPTransferSocketImpl(String address, int port)
			throws ServerSocketException {
		try {
			transferSocket = new Socket(address, port);
		} catch (IOException e) {
			throw new ServerSocketException(
					"Unable to create TransfertServer actif mode", e);
		}

	}

	/**
	 * Constructor passive mode
	 * 
	 * @throws SocketException
	 *             when unable to create the transfert server socket
	 */
	public FTPTransferSocketImpl() throws ServerSocketException {
		try {
			// Create socket
			transfertServerSocket = new ServerSocket(0);
		} catch (final IOException e) {
			throw new ServerSocketException(
					"Unable to create transfert socket", e);
		}
	}

	/**
	 * @see FTPTransferSocket
	 */
	public void startServer() throws ServerSocketException {
		if (transfertServerSocket == null)
			return;
		try {
			transferSocket = transfertServerSocket.accept();
		} catch (final IOException e) {
			throw new ServerSocketException(
					"Unable to create FTPTansfertServer", e);
		}
	}

	/**
	 * @see FTPTransferSocket
	 */
	@Override
	public int getPublicPort() {
		if (transfertServerSocket != null)
			return transfertServerSocket.getLocalPort();
		else
			return transferSocket.getLocalPort();
	}

	/**
	 * @see FTPTransferSocket
	 */
	@Override
	public void writeData(final byte[] bytes) throws SocketException {
		if (transferSocket == null) {
			startServer();
		}
		try {
			final BufferedOutputStream bo = new BufferedOutputStream(
					transferSocket.getOutputStream());

			// Send bytes
			bo.write(bytes);

			// Flush data
			bo.flush();

			// Close the connection
			this.close();
		} catch (final IOException e) {
			throw new SocketException("Unable to send data to client", e);
		}
	}

	/**
	 * @see FTPTransferSocket
	 */
	@Override
	public void writeData(InputStream stream) throws SocketException {
		if (transferSocket == null) {
			startServer();
		}
		try {
			final BufferedOutputStream bo = new BufferedOutputStream(
					transferSocket.getOutputStream());

			byte[] buffer = new byte[512];
			int l;
			while ((l = stream.read(buffer)) > 0) {
				bo.write(buffer, 0, l);
			}
			// Flush data
			bo.flush();

			// Close the connection
			this.close();
		} catch (final IOException e) {
			throw new SocketException("Unable to send data to client", e);
		}
	}

	/**
	 * @see FTPTransferSocket
	 */
	@Override
	public void writeData(String data) throws SocketException {
		writeData(data.getBytes());
	}

	/**
	 * @see FTPTransferSocket
	 */
	@Override
	public byte[] readData() throws SocketException {
		if (transferSocket == null) {
			startServer();
		}
		try {
			final BufferedInputStream bi = new BufferedInputStream(
					transferSocket.getInputStream());
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			// Read data
			byte[] buffer = new byte[512];
			int l;
			while ((l = bi.read(buffer)) > 0) {
				output.write(buffer, 0, l);
			}

			// Return it
			return output.toByteArray();
		} catch (final IOException e) {
			throw new SocketException("Unable to read client data", e);
		}
	}

	/**
	 * @see FTPTransferSocket
	 */
	@Override
	public String readStringData() throws SocketException {
		return new String(readData());
	}

	/**
	 * @see FTPTransferSocket
	 */
	@Override
	public void readData(OutputStream outputStream) throws SocketException {
		if (transferSocket == null) {
			startServer();
		}
		try {
			final BufferedInputStream bi = new BufferedInputStream(
					transferSocket.getInputStream());

			// Read data
			byte[] buffer = new byte[512];
			int l;
			while ((l = bi.read(buffer)) > 0) {
				outputStream.write(buffer, 0, l);
			}
		} catch (final IOException e) {
			throw new SocketException("Unable to read client data", e);
		}

	}

	/**
	 * @see FTPTransferSocket
	 */
	public void close() throws SocketException {
		try {
			// Close socket
			if (transferSocket != null)
				transferSocket.close();
			if (transfertServerSocket != null)
				transfertServerSocket.close();
		} catch (final IOException e) {
			throw new SocketException("Unable to close data connection", e);
		}
	}
}
