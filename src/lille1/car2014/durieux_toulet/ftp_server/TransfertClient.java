package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
	 * @param transfertSocket Socket to use for connection
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
					this.tranfsertSocket.getOutputStream());

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
		final ArrayList<Integer> bytes = new ArrayList<Integer>();

		try {
			final BufferedInputStream bi = new BufferedInputStream(
					this.tranfsertSocket.getInputStream());
			int b;

			// Read data
			while ((b = bi.read()) != -1) {
				bytes.add(b);
			}

			final byte[] bytesB = new byte[bytes.size()];

			// Convert buffer
			for (int i = 0; i < bytes.size(); i++) {
				final int a = bytes.get(i);
				bytesB[i] = (byte) a;
			}

			// Print buffered message
			System.out.println(new String(bytesB));

			// Return it
			return bytesB;
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
			this.tranfsertSocket.close();
		} catch (final IOException e) {
			// Log errors
			LoggerUtilities.error(e);
		}
	}

}
