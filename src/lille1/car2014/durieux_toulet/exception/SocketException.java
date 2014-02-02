package lille1.car2014.durieux_toulet.exception;

/**
 * Exception launched by a socket
 * 
 * @author Durieux
 * 
 */
public class SocketException extends Exception {
	public SocketException() {
		super();
	}

	public SocketException(final String message) {
		super(message);
	}

	public SocketException(final Throwable t) {
		super(t);
	}

	public SocketException(final String message, final Throwable t) {
		super(message, t);
	}
}
