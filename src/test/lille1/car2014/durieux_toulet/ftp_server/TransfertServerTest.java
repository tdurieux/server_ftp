package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.*;

import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.ftp_server.TransfertServer;
import lille1.car2014.durieux_toulet.ftp_server.TransfertServerImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransfertServerTest {

	private TransfertServer transfertServer;

	@Before
	public void setUp() {
		try {
			transfertServer = new TransfertServerImpl();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testTransfertServerActiveMode() {
		try {
			TransfertServer transfertServer = new TransfertServerImpl("127.0.0.1", 8743);
		} catch (ServerSocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testTransfertServerPassiveMode() {
		try {
			TransfertServer transfertServer = new TransfertServerImpl();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPublicPort() {
		transfertServer.getPublicPort();
	}

	@Test
	public void testGetTransfertClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteContentString() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteContentByteArray() {
		fail("Not yet implemented");
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
	public void testClose() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteContentFileInputStream() {
		fail("Not yet implemented");
	}

}
