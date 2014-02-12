package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl(
					"127.0.0.1", 8743);
			fail("Server does not exist");
		} catch (final ServerSocketException e) {

		}
	}

	@Test
	public void testTransfertServerActiveMode() throws IOException,
			SocketException {
		try {
			final ServerSocket server = new ServerSocket(0);

			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl(
					"127.0.0.1", server.getLocalPort());
			transfertServer.close();
			server.close();
		} catch (final ServerSocketException e) {
			fail("Server exist");
		}
	}

	@Test
	public void testTransfertServerPassiveMode() {
		try {
			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			transfertServer.close();
		} catch (final SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testGetPublicPort() {
		try {
			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			transfertServer.getPublicPort();
		} catch (final SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testwriteDataString() throws IOException,
			RequestHandlerException {
		try {
			final ServerSocket server = new ServerSocket(0);

			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl(
					"127.0.0.1", server.getLocalPort());
			transfertServer.writeData("Test");
			transfertServer.close();
			server.close();
		} catch (final SocketException e) {
			fail("Server can be started");
		}
	}

	@Test
	public void testwriteDataByteArray() throws IOException,
			RequestHandlerException {
		try {
			final ServerSocket server = new ServerSocket(0);

			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl(
					"127.0.0.1", server.getLocalPort());
			transfertServer.writeData("Test".getBytes());
			transfertServer.close();
			server.close();
		} catch (final SocketException e) {
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
					} catch (final Exception e) {
						fail("Unable to read content");
					}
				}
			}.start();

			try {
				final String content = transfertServer.readStringData();
				assertEquals("Test", content);
			} catch (final Exception e) {
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (final SocketException e3) {
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
					} catch (final Exception e) {
						fail("Unable to read content");
					}
				}
			}.start();

			try {
				final String content = new String(transfertServer.readData());

				assertEquals("Test", content);
			} catch (final Exception e) {
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (final SocketException e3) {
			fail("Request error");
		}
	}

	@Test
	public void testClose() {
		try {
			final FTPTransferSocket transfertServer = new FTPTransferSocketImpl();
			transfertServer.close();
		} catch (final SocketException e) {
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
					} catch (final Exception e) {
						fail("Unable to write content");
					}
				}
			}.start();

			try {
				transfertServer.writeData(FTPConfiguration.class.getResource(
						"db_user.ini").openStream());

			} catch (final Exception e) {
				transfertServer.close();
				transfertClient.close();
				fail("Unable to read content");
			}

			transfertServer.close();
			transfertClient.close();
		} catch (final SocketException e3) {
			fail("Request error");
		}
	}

}
