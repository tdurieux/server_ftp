package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.*;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.ftp_server.FTPServer;
import lille1.car2014.durieux_toulet.ftp_server.FTPServerImpl;

import org.junit.Test;

public class FTPServerTest {

	@Test
	public void testConstructorWithoutParameter() {
		FTPServer ftpServer = new FTPServerImpl();
		assertEquals(ftpServer.getPort(), 21);
	}

	@Test
	public void testConstructorWithParameter() {
		int port = 2121;
		FTPServer ftpServer = new FTPServerImpl(port);
		assertEquals(ftpServer.getPort(), port);
	}

	@Test
	public void testStartFTPServer() {
		int port = 2121;
		final FTPServer ftpServer = new FTPServerImpl(port);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ftpServer.startServer();
				} catch (ServerSocketException e) {
					fail("Unable to start server: " + e.getMessage());
				}
			}
		}).start();
		try {
			Thread.currentThread();
			Thread.sleep(500);
			assertEquals(ftpServer.isStarted(), true);
			ftpServer.closeServer();
		} catch (ServerSocketException e) {
			fail("Unable to stop server: " + e.getMessage());
		} catch (InterruptedException e) {
			fail("Unable to start server: " + e.getMessage());
		}
	}
	
	@Test
	public void testStartFTPServerWithServerAlreadyStarted() {
		int port = 2121;
		final FTPServer ftpServer = new FTPServerImpl(port);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ftpServer.startServer();
				} catch (ServerSocketException e) {
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
			} catch (ServerSocketException e) {
				assertEquals(e.getMessage(), "The FTP server is already started");
			}
			
			ftpServer.closeServer();
		} catch (ServerSocketException e) {
			fail("Unable to stop server: " + e.getMessage());
		} catch (InterruptedException e) {
			fail("Unable to start server: " + e.getMessage());
		}
	}
	
	@Test
	public void testStopFTPServerServerNotStarted() {
		int port = 2121;
		final FTPServer ftpServer = new FTPServerImpl(port);
		try {
			ftpServer.closeServer();
			fail("Server is not stared");
		} catch (ServerSocketException e) {
			assertEquals(e.getMessage(), "The FTP server is not started");
		} 
	}
}
