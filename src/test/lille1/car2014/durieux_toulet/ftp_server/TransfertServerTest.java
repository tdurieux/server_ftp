package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import lille1.car2014.durieux_toulet.config.FTPConfiguration;
import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.ftp_server.FTPTransfertClient;
import lille1.car2014.durieux_toulet.ftp_server.FTPTransfertClientImpl;
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
			fail("Server exist");
		}
	}

	@Test
	public void testTransfertServerPassiveMode() {
		try {
			FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
			transfertServer.close();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testGetPublicPort() {
		try {
			FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
			transfertServer.getPublicPort();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testGetTransfertClient() {
		try {
			FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
			transfertServer.getTransfertClient();
			transfertServer.close();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testWriteContentString() throws IOException,
			RequestHandlerException {
		try {
			ServerSocket server = new ServerSocket(0);

			FTPTransfertServer transfertServer = new FTPTransfertServerImpl(
					"127.0.0.1", server.getLocalPort());
			transfertServer.writeContent("Test");
			transfertServer.close();
			server.close();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testWriteContentByteArray() throws IOException,
			RequestHandlerException {
		try {
			ServerSocket server = new ServerSocket(0);

			FTPTransfertServer transfertServer = new FTPTransfertServerImpl(
					"127.0.0.1", server.getLocalPort());
			transfertServer.writeContent("Test".getBytes());
			transfertServer.close();
			server.close();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testReadStringContent() {
		try {
			final FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
			Socket socket = new Socket("127.0.0.1",
					transfertServer.getPublicPort());
			final FTPTransfertClient transfertClient = new FTPTransfertClientImpl(
					socket);
			new Thread("TestReadStingContent") {
				@Override
				public void run() {
					try {
						transfertClient.writeData("Test".getBytes());
					} catch (Exception e) {
						fail("Unable to read content");
					}
				}
			}.start();

			try {
				String content = transfertServer.readStringContent();
				assertEquals("Test", content);
			} catch (Exception e) {
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (SocketException e3) {
			fail("Request error");
		} catch (UnknownHostException e1) {
			fail("Unable to create socket");
		} catch (IOException e1) {
			fail("Unable to create socket");
		}
	}

	@Test
	public void testReadContent() throws SocketException, IOException {
		try {
			final FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
			Socket socket = new Socket("127.0.0.1",
					transfertServer.getPublicPort());
			final FTPTransfertClient transfertClient = new FTPTransfertClientImpl(
					socket);
			new Thread("testReadContent") {
				@Override
				public void run() {
					try {
						transfertClient.writeData("Test".getBytes());
					} catch (Exception e) {
						fail("Unable to read content");
					}
				}
			}.start();

			try {
				String content = new String(transfertServer.readContent());

				assertEquals("Test", content);
			} catch (Exception e) {
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (SocketException e3) {
			fail("Request error");
		} catch (UnknownHostException e1) {
			fail("Unable to create socket");
		} catch (IOException e1) {
			fail("Unable to create socket");
		}
	}

	@Test
	public void testClose() throws SocketException {
		FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
		transfertServer.close();
	}

	@Test
	public void testWriteContentFileInputStream() {
		try {
			final FTPTransfertServer transfertServer = new FTPTransfertServerImpl();
			Socket socket = new Socket("127.0.0.1",
					transfertServer.getPublicPort());
			final FTPTransfertClient transfertClient = new FTPTransfertClientImpl(
					socket);
			new Thread("testWriteContentFileInputStream") {
				@Override
				public void run() {
					try {
						transfertServer.writeContent(FTPConfiguration.class
								.getResource("db_user.ini").openStream());
					} catch (Exception e) {
						fail("Unable to write content");
					}
				}
			}.start();

			try {
				transfertClient.readData();
			} catch (Exception e) {
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (SocketException e3) {
			fail("Request error");
		} catch (UnknownHostException e1) {
			fail("Unable to create socket");
		} catch (IOException e1) {
			fail("Unable to create socket");
		}
	}

}
