package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.ftp_server.FTPServer;
import lille1.car2014.durieux_toulet.ftp_server.FTPServerImpl;

import org.junit.Test;

public class FTPServerTest {

	@Test
	public void testConstructorWithoutParameter() {
		final FTPServer ftpServer = new FTPServerImpl();
		assertEquals(21, ftpServer.getPort());
	}

	@Test
	public void testConstructorWithParameter() {
		final int port = 2121;
		final FTPServer ftpServer = new FTPServerImpl(port);
		assertEquals(port, ftpServer.getPort());
	}

	@Test
	public void testStartFTPServer() {
		final int port = 2121;
		final FTPServer ftpServer = new FTPServerImpl(port);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ftpServer.startServer();
				} catch (final ServerSocketException e) {
					fail("Unable to start server: " + e.getMessage());
				}
			}
		}).start();
		try {
			Thread.currentThread();
			Thread.sleep(500);
			assertEquals(true, ftpServer.isStarted());
			ftpServer.closeServer();
		} catch (final ServerSocketException e) {
			fail("Unable to stop server: " + e.getMessage());
		} catch (final InterruptedException e) {
			fail("Unable to start server: " + e.getMessage());
		}
	}

	@Test
	public void testStartFTPServerWithServerAlreadyStarted() {
		final int port = 2121;
		final FTPServer ftpServer = new FTPServerImpl(port);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ftpServer.startServer();
				} catch (final ServerSocketException e) {
					fail("Unable to start server: " + e.getMessage());
				}
			}
		}).start();
		try {
			Thread.currentThread();
			Thread.sleep(500);
			try {
				ftpServer.startServer();
				fail("Server already started");
			} catch (final ServerSocketException e) {
				assertEquals(e.getMessage(),
						"The FTP server is already started");
			}

			ftpServer.closeServer();
		} catch (final ServerSocketException e) {
			fail("Unable to stop server: " + e.getMessage());
		} catch (final InterruptedException e) {
			fail("Unable to start server: " + e.getMessage());
		}
	}

	@Test
	public void testStopFTPServerServerNotStarted() {
		final int port = 2121;
		final FTPServer ftpServer = new FTPServerImpl(port);
		try {
			ftpServer.closeServer();
			fail("Server is not stared");
		} catch (final ServerSocketException e) {
			assertEquals(e.getMessage(), "The FTP server is not started");
		}
	}
}
