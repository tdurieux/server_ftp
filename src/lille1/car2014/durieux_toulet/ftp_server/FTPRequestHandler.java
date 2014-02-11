package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;

/**
 * Handle requests of FTP client
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPRequestHandler {

	/**
	 * Execute the function associate to a command
	 * @param command the command send by the client
	 * @param parameters parameters of the command
	 * @throws RequestHandlerException
	 */
	void execute() throws RequestHandlerException;
}
