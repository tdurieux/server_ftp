package test.lille1.car2014.durieux_toulet.ftp_server;

public interface FTPClientSocketListener {

	void newWriteMessage(String message);

	void sokcetClosed();

	void socketOpened();

}
