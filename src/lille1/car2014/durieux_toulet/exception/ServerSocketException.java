package lille1.car2014.durieux_toulet.exception;

/**
 * Exception launched by a server socket
 * 
 * @author Thomas Durieux
 * @author Toulet Cyrille
 */
public class ServerSocketException extends SocketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7674458099465801345L;

	/**
	 * Constructor
	 */
	public ServerSocketException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            Message of exception
	 */
	public ServerSocketException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param t
	 *            Throwable exception
	 */
	public ServerSocketException(final Throwable t) {
		super(t);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            Message of exception
	 * @param t
	 *            Throwable exception
	 */
	public ServerSocketException(final String message, final Throwable t) {
		super(message, t);
	}
}
