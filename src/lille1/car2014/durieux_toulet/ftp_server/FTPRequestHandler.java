package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;

/**
 * FTP Request Handler used to parse and execute ftp command
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPRequestHandler {

	/**
	 * Execute the function associate to a command
	 * 
	 * @throws RequestHandlerException
	 */
	void execute() throws RequestHandlerException;
}
