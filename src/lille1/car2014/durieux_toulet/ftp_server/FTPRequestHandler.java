package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;

public interface FTPRequestHandler {

	void execute() throws RequestHandlerException;
}
