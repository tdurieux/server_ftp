package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.SocketException;

public interface FTPClient {
	void writeMessage(String message) throws SocketException;
	
	void readMessage();
	
	void close();
}
