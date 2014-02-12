package test.lille1.car2014.durieux_toulet.ftp_server;

import java.util.ArrayList;
import java.util.Collection;

import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.ftp_server.FTPClientSocket;

public class FTPClientSocketMockup implements FTPClientSocket {

	private final Collection<FTPClientSocketListener> ftpClientSocketListeners = new ArrayList<FTPClientSocketListener>();

	public FTPClientSocketMockup() {

	}

	@Override
	public void writeMessage(final String message) {
		newWriteMessage(message);
	}

	@Override
	public void readMessage() {

	}

	@Override
	public void close() throws SocketException {
		closeSocket();
	}

	@Override
	public void startListeningClient() {
		openSocket();
	}

	protected void newWriteMessage(final String message) {
		if (ftpClientSocketListeners.size() > 0) {
			System.out.println(message);
		}
		for (final FTPClientSocketListener listener : ftpClientSocketListeners) {
			listener.newWriteMessage(message);
		}
	}

	protected void closeSocket() {
		for (final FTPClientSocketListener listener : ftpClientSocketListeners) {
			listener.sokcetClosed();
		}
	}

	protected void openSocket() {
		for (final FTPClientSocketListener listener : ftpClientSocketListeners) {
			listener.socketOpened();
		}
	}

	public void addFTPClientSocketListener(
			final FTPClientSocketListener listener) {
		ftpClientSocketListeners.add(listener);
	}

	public void removeFTPClientSocketListener(
			final FTPClientSocketListener listener) {
		ftpClientSocketListeners.remove(listener);
	}

}
