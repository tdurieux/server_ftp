package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.ftp_server.FTPTransfertServer;
import lille1.car2014.durieux_toulet.ftp_server.FTPTransfertServerImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransfertServerTest {

	@Before
	public void setUp() {

	}

	@After
	public void tearDown() {
	}

	@Test
	public void testTransfertServerActiveModeNotValid() {
		try {
			FTPTransfertServer transfertServer = new FTPTransfertServerImpl(
					"127.0.0.1", 8743);
			fail("Server does not exist");
		} catch (ServerSocketException e) {

		}
	}

	@Test
	public void testTransfertServerActiveMode() throws IOException,
			SocketException {
		try {
			ServerSocket server = new ServerSocket(0);

			FTPTransfertServer transfertServer = new FTPTransfertServerImpl(
					"127.0.0.1", server.getLocalPort());
			transfertServer.close();
			server.close();
		} catch (ServerSocketException e) {
			fail("Server does not exist");
		}
	}

	@Test
	public void testTransfertServerPassiveMode() throws SocketException {
		FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
		transfertServer.close();
	}

	@Test
	public void testGetPublicPort() throws SocketException {
		FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
		transfertServer.getPublicPort();
	}

	@Test
	public void testGetTransfertClient() throws SocketException {
		FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
		transfertServer.getTransfertClient();
		transfertServer.close();
	}

	@Test
	public void testWriteContentString() throws IOException, SocketException,
			RequestHandlerException {
		ServerSocket server = new ServerSocket(0);

		FTPTransfertServer transfertServer = new FTPTransfertServerImpl(
				"127.0.0.1", server.getLocalPort());
		transfertServer.writeContent("Test");
		transfertServer.close();
		server.close();
	}

	@Test
	public void testWriteContentByteArray() throws IOException,
			SocketException, RequestHandlerException {
		ServerSocket server = new ServerSocket(0);

		FTPTransfertServer transfertServer = new FTPTransfertServerImpl(
				"127.0.0.1", server.getLocalPort());
		transfertServer.writeContent("Test".getBytes());
		transfertServer.close();
		server.close();
	}

	@Test
	public void testReadStringContent() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadContent() {
		fail("Not yet implemented");
	}

	@Test
	public void testClose() throws SocketException {
		FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
		transfertServer.close();
	}

	@Test
	public void testWriteContentFileInputStream() {
		fail("Not yet implemented");
	}

}
