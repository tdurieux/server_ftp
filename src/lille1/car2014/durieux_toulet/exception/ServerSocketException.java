package lille1.car2014.durieux_toulet.exception;

/**
 * Exception launched by a server socket
 * 
 * @author Thomas Durieux
 * 
 */
public class ServerSocketException extends SocketException {
	public ServerSocketException() {
		super();
	}

	public ServerSocketException(final String message) {
		super(message);
	}

	public ServerSocketException(final Throwable t) {
		super(t);
	}

	public ServerSocketException(final String message, final Throwable t) {
		super(message, t);
	}
}
