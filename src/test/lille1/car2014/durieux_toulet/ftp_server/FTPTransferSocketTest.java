package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;

import lille1.car2014.durieux_toulet.config.FTPConfiguration;
import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.ftp_server.FTPTransferSocket;
import lille1.car2014.durieux_toulet.ftp_server.FTPTransferSocketImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FTPTransferSocketTest {

	@Before
	public void setUp() {

	}

	@After
	public void tearDown() {
	}

	@Test
	public void testTransfertServerActiveModeNotValid() {
		try {
			FTPTransferSocket transfertServer = new FTPTransferSocketImpl(
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

			FTPTransferSocket transfertServer = new FTPTransferSocketImpl(
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
			FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			transfertServer.close();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testGetPublicPort() {
		try {
			FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			transfertServer.getPublicPort();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testwriteDataString() throws IOException,
			RequestHandlerException {
		try {
			ServerSocket server = new ServerSocket(0);

			FTPTransferSocket transfertServer = new FTPTransferSocketImpl(
					"127.0.0.1", server.getLocalPort());
			transfertServer.writeData("Test");
			transfertServer.close();
			server.close();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testwriteDataByteArray() throws IOException,
			RequestHandlerException {
		try {
			ServerSocket server = new ServerSocket(0);

			FTPTransferSocket transfertServer = new FTPTransferSocketImpl(
					"127.0.0.1", server.getLocalPort());
			transfertServer.writeData("Test".getBytes());
			transfertServer.close();
			server.close();
		} catch (SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testReadStringContent() {
		try {
			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			final FTPTransferSocket transfertClient = new FTPTransferSocketImpl(
					"127.0.0.1", transfertServer.getPublicPort());
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
				String content = transfertServer.readStringData();
				assertEquals("Test", content);
			} catch (Exception e) {
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (SocketException e3) {
			fail("Request error");
		}
	}

	@Test
	public void testReadContent() throws SocketException, IOException {
		try {
			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			final FTPTransferSocket transfertClient = new FTPTransferSocketImpl(
					"127.0.0.1", transfertServer.getPublicPort());
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
				String content = new String(transfertServer.readData());

				assertEquals("Test", content);
			} catch (Exception e) {
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (SocketException e3) {
			fail("Request error");
		}
	}

	@Test
	public void testClose() {
		try {
			FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			transfertServer.close();
		} catch (SocketException e) {
			fail("Unable to start transfert server");
		} 
	}

	@Test
	public void testwriteDataFileInputStream() {
		try {
			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			final FTPTransferSocket transfertClient = new FTPTransferSocketImpl(
					"127.0.0.1", transfertServer.getPublicPort());

			new Thread("testwriteDataFileInputStream") {
				@Override
				public void run() {
					try {
						transfertClient.readData();
					} catch (Exception e) {
						fail("Unable to write content");
					}
				}
			}.start();

			try {
				transfertServer.writeData(FTPConfiguration.class.getResource(
						"db_user.ini").openStream());

			} catch (Exception e) {
				transfertServer.close();
				transfertClient.close();
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (SocketException e3) {
			fail("Request error");
		}
	}

}
