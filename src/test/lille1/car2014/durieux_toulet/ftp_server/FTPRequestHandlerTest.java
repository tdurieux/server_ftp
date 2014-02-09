package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.*;

import java.net.Socket;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.ftp_server.FTPClientImpl;
import lille1.car2014.durieux_toulet.ftp_server.FTPRequestHandler;
import lille1.car2014.durieux_toulet.ftp_server.FTPRequestHandlerImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FTPRequestHandlerTest {

	private FTPRequestHandler requestHandler;

	@Before
	public void setUp() {
		requestHandler = new FTPRequestHandlerImpl(new FTPClientImpl(new Socket()));
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testRequestList() {
		try {
			requestHandler.parseStringRequest("LIST -a");
		} catch (RequestHandlerException e) {
			fail("The command is valid");
		}
	}

}
