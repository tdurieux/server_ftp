package lille1.car2014.durieux_toulet.ftp_server;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;

public interface FTPTransfertServer {
	int getPublicPort();
	
	FTPTransfertClient getTransfertClient();
	
	void writeContent(final String content)
			throws RequestHandlerException, SocketException;
	
	void writeContent(final byte[] content)
			throws RequestHandlerException, SocketException;
	
	String readStringContent() throws SocketException;
	
	void readContent(FileOutputStream fileOutputStream) throws SocketException;
	
	byte[] readContent() throws SocketException;
	
	void close() throws SocketException;
	
	void writeContent(FileInputStream stream) throws SocketException;
	
}
