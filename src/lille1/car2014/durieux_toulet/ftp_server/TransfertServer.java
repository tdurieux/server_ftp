package lille1.car2014.durieux_toulet.ftp_server;

import java.io.FileInputStream;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;

public interface TransfertServer {
	int getPublicPort();
	
	TransfertClient getTransfertClient();
	
	void writeContent(final String content)
			throws RequestHandlerException, SocketException;
	
	void writeContent(final byte[] content)
			throws RequestHandlerException, SocketException;
	
	String readStringContent();
	
	byte[] readContent();
	
	void close();
	
	void writeContent(FileInputStream stream) throws SocketException;
	
}
