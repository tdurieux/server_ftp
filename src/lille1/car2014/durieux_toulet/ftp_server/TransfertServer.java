package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;

public class TransfertServer {
	private final ServerSocket transfertServerSocket;
	private TransfertClient transfertClient;

	public TransfertServer() throws SocketException {
		try {
			this.transfertServerSocket = new ServerSocket(0);
		} catch (final IOException e) {
			throw new SocketException("Unable to create transfert socket", e);
		}
	}

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

	public int getPublicPort() {
		return this.transfertServerSocket.getLocalPort();
	}

	public TransfertClient getTransfertClient() {
		return this.transfertClient;
	}

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

	public String readContent() {
		if (this.transfertClient == null) {
			this.startServer();
		}
		return new String(TransfertServer.this.transfertClient.readMessage());
	}

	public void writeContent(final byte[] content)
			throws RequestHandlerException {
		if (this.transfertClient == null) {
			this.startServer();
		}
		TransfertServer.this.transfertClient.writeMessage(content);
	}

	public void close() {
		try {
			this.transfertServerSocket.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
