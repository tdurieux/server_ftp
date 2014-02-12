package lille1.car2014.durieux_toulet.exception;

/**
 * Execption thrown by FTP Client
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2863983684540775401L;

	/**
	 * Constructor
	 */
	public FTPClientException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            Message of exception
	 */
	public FTPClientException(final String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param t
	 *            Throwable exception
	 */
	public FTPClientException(final Throwable t) {
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
	public FTPClientException(final String message, final Throwable t) {
		super(message, t);
	}
}
