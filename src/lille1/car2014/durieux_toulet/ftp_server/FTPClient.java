package lille1.car2014.durieux_toulet.ftp_server;

import java.util.Map;

import lille1.car2014.durieux_toulet.exception.SocketException;

/**
 * FTP client interface
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPClient {

	void setUsername(String username);

	boolean connect(String password);

	boolean isConnected();

	void setTypeCharactor(String type);

	Map<String, String> getOptions();

	String getCurrentDir();

	void setCurrentDir(String currentDir);

	int createNewTransfert(String ip, int port) throws SocketException;

	int createNewTransfert() throws SocketException;

	FTPTransfertServerImpl getTransfertServer();

	void setFileToRename(String path);

	String getFileToRename();
	
	String getUsername();
}
