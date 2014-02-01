package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.xml.sax.ErrorHandler;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

public class TransfertServer {
	private final ServerSocket transfertServerSocket;
	private TransfertClient transfertClient;

	public TransfertServer() throws SocketException {
		try {
			transfertServerSocket = new ServerSocket(0);
		} catch (IOException e) {
			throw new SocketException("Unable to create transfert socket", e);
		}
	}

	private void startServer() {
		try {
			Socket tranfsertSocket = transfertServerSocket.accept();
			TransfertServer.this.transfertClient = new TransfertClient(
					tranfsertSocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPublicPort() {
		return transfertServerSocket.getLocalPort();
	}

	public TransfertClient getTransfertClient() {
		return transfertClient;
	}

	public void writeContent(final String content)
			throws RequestHandlerException {
		if (transfertClient == null) {
			startServer();
		}
		TransfertServer.this.transfertClient.writeMessage(content.getBytes());
	}
	
	public String readContent() {
		if (transfertClient == null) {
			startServer();
		}
		return new String(TransfertServer.this.transfertClient.readMessage());
	}

	public void writeContent(final byte[] content)
			throws RequestHandlerException {
		if (transfertClient == null) {
			startServer();
		}
		TransfertServer.this.transfertClient.writeMessage(content);
	}

	public void close() {
		try {
			transfertServerSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
