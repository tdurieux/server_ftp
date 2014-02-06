package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * Transfert Client
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class TransfertClient {
	private final Socket tranfsertSocket;

	/**
	 * Constructor
	 * 
	 * @param transfertSocket
	 *            Socket to use for connection
	 */
	public TransfertClient(final Socket tranfsertSocket) {
		// Copy socket to use
		this.tranfsertSocket = tranfsertSocket;
	}

	/**
	 * Write message
	 * 
	 * @param byte Bytes of message to write
	 */
	public void writeMessage(final byte[] bytes) {
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
			// Log errors
			LoggerUtilities.error(e);
		}
	}

	/**
	 * Read message
	 * 
	 * @return Bytes of message read
	 */
	public byte[] readMessage() {
		try {
			final BufferedInputStream bi = new BufferedInputStream(
					tranfsertSocket.getInputStream());
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			// Read data
			byte[] buffer = new byte[65536];
			int l;
			while ((l = bi.read(buffer)) > 0) {
				output.write(buffer);
			}

			// Return it
			return output.toByteArray();
		} catch (final IOException e) {
			// Log errors
			LoggerUtilities.error(e);
		}

		return null;
	}

	/**
	 * Close connection with the transfert server
	 */
	public void close() {
		try {
			// Close socket
			tranfsertSocket.close();
		} catch (final IOException e) {
			// Log errors
			LoggerUtilities.error(e);
		}
	}

}
