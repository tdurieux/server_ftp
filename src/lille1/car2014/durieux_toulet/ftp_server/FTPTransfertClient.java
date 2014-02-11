package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Transfert Client
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPTransfertClient {
	private final Socket tranfsertSocket;


	/**
	 * Constructor
	 * @param transfertSocket Socket to use for connection
	 */
	public FTPTransfertClient(final Socket tranfsertSocket) {
		// Copy socket to use
		this.tranfsertSocket = tranfsertSocket;
	}


	/**
	 * Write message
	 * @param byte Bytes of message to write
<<<<<<< HEAD
	 * @throws SocketException
=======
	 * @throws SocketException when unable to write message
>>>>>>> 6c8febf5d12bfa43ecc8b2ea56a143c8cb1bb334
	 */
	public void writeMessage(final byte[] bytes) throws SocketException {
		try {
			final BufferedOutputStream bo = new BufferedOutputStream(
					tranfsertSocket.getOutputStream());

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
	 * Write message
	 * @param stream The input stream to write
	 * @throws SocketException when unable to write message
	 */
	public void writeMessage(FileInputStream stream) throws SocketException {
		try {
			final BufferedOutputStream bo = new BufferedOutputStream(
					tranfsertSocket.getOutputStream());

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
	 * Read message
	 * @return Bytes of message read
	 * @throws SocketException
	 *             Unable to read client data
	 */
	public byte[] readMessage() throws SocketException {
		try {
			final BufferedInputStream bi = new BufferedInputStream(
					tranfsertSocket.getInputStream());
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
	 * Read on data connection and write the content into a stream
	 * 
	 * @param fileOutputStream
	 *            the output stream
	 * @throws SocketException
	 *             Unable to read client data
	 */
	public void readMessage(FileOutputStream fileOutputStream)
			throws SocketException {
		try {
			final BufferedInputStream bi = new BufferedInputStream(
					tranfsertSocket.getInputStream());

			// Read data
			byte[] buffer = new byte[1];
			int l;
			while ((l = bi.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, l);
			}
		} catch (final IOException e) {
			throw new SocketException("Unable to read client data", e);
		}

	}


	/**
	 * Close connection with the client
	 * 
	 * @throws SocketException
	 *             Unable to close data connection
	 */
	public void close() throws SocketException {
		try {
			// Close socket
			tranfsertSocket.close();
		} catch (final IOException e) {
			throw new SocketException("Unable to close data connection", e);
		}
	}
}
