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

	public SocketException(String message) {
		super(message);
	}

	public SocketException(Throwable t) {
		super(t);
	}

	public SocketException(String message, Throwable t) {
		super(message, t);
	}
}
