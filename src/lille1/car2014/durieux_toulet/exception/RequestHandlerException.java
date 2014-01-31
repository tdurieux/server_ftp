package lille1.car2014.durieux_toulet.exception;

public class RequestHandlerException extends Exception {
	public RequestHandlerException() {
		super();
	}

	public RequestHandlerException(String message) {
		super(message);
	}

	public RequestHandlerException(Throwable t) {
		super(t);
	}

	public RequestHandlerException(String message, Throwable t) {
		super(message, t);
	}
}
