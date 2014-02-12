package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * Class used to simplify the reading and writing bytes on socket
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPTransfertClientImpl implements FTPTransfertClient {
	private final Socket transfertSocket;

	/**
	 * Constructor
	 * 
	 * @param transfertSocket
	 *            Socket to use for connection
	 */
	public FTPTransfertClientImpl(final Socket transfertSocket) {
		// Copy socket to use
		this.transfertSocket = transfertSocket;
	}

	/**
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPTransfertClient#writeData(byte[])
	 */
	@Override
	public void writeData(final byte[] bytes) throws SocketException {
		try {
			final BufferedOutputStream bo = new BufferedOutputStream(
					transfertSocket.getOutputStream());

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
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPTransfertClient#writeData(java.io.FileInputStream)
	 */
	@Override
	public void writeData(InputStream stream) throws SocketException {
		try {
			final BufferedOutputStream bo = new BufferedOutputStream(
					transfertSocket.getOutputStream());

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
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPTransfertClient#readData()
	 */
	@Override
	public byte[] readData() throws SocketException {
		try {
			final BufferedInputStream bi = new BufferedInputStream(
					transfertSocket.getInputStream());
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
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPTransfertClient#readData(java.io.FileOutputStream)
	 */
	@Override
	public void readData(OutputStream outputStream)
			throws SocketException {
		try {
			final BufferedInputStream bi = new BufferedInputStream(
					transfertSocket.getInputStream());

			// Read data
			byte[] buffer = new byte[1];
			int l;
			while ((l = bi.read(buffer)) > 0) {
				outputStream.write(buffer, 0, l);
			}
		} catch (final IOException e) {
			throw new SocketException("Unable to read client data", e);
		}

	}

	/**
	 * @see lille1.car2014.durieux_toulet.ftp_server.FTPTransfertClient#close()
	 */
	@Override
	public void close() throws SocketException {
		try {
			// Close socket
			transfertSocket.close();
		} catch (final IOException e) {
			throw new SocketException("Unable to close data connection", e);
		}
	}
}
