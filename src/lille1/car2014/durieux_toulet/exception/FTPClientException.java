package lille1.car2014.durieux_toulet.exception;

public class FTPClientException extends Exception {
	public FTPClientException() {
		super();
	}

	public FTPClientException(final String message) {
		super(message);
	}

	public FTPClientException(final Throwable t) {
		super(t);
	}

	public FTPClientException(final String message, final Throwable t) {
		super(message, t);
	}
}
