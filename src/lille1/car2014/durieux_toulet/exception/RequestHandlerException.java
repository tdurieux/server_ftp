package lille1.car2014.durieux_toulet.exception;

/**
 * Execption thrown by Request Handler
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class RequestHandlerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7542887332656932456L;

	/**
	 * Constructor
	 */
	public RequestHandlerException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            Message of exception
	 */
	public RequestHandlerException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param t
	 *            Throwable exception
	 */
	public RequestHandlerException(final Throwable t) {
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
	public RequestHandlerException(final String message, final Throwable t) {
		super(message, t);
	}
}
