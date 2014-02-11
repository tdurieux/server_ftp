package lille1.car2014.durieux_toulet.exception;

/**
 * Exception launched by a socket
 * 
 * @author Durieux
 * @author Toulet Cyrille
 */
public class SocketException extends Exception {

	/**
	 * Constructor
	 */
	public SocketException() {
		super();
	}


	/**
	 * Constructor
	 * @param message Message of exception
	 */
	public SocketException(final String message) {
		super(message);
	}


	/**
	 * Constructor
	 * @param t Throwable exception
	 */
	public SocketException(final Throwable t) {
		super(t);
	}


	/**
	 * Constructor
	 * @param message Message of exception
	 * @param t Throwable exception
	 */
	public SocketException(final String message, final Throwable t) {
		super(message, t);
	}
}
