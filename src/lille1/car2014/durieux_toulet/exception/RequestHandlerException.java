package lille1.car2014.durieux_toulet.exception;

public class RequestHandlerException extends Exception {
	public RequestHandlerException() {
		super();
	}

	public RequestHandlerException(final String message) {
		super(message);
	}

	public RequestHandlerException(final Throwable t) {
		super(t);
	}

	public RequestHandlerException(final String message, final Throwable t) {
		super(message, t);
	}
}
