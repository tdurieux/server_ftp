package lille1.car2014.durieux_toulet.exception;

public class UserDatabaseException extends Exception {
	public UserDatabaseException() {
		super();
	}

	public UserDatabaseException(final String message) {
		super(message);
	}

	public UserDatabaseException(final Throwable t) {
		super(t);
	}

	public UserDatabaseException(final String message, final Throwable t) {
		super(message, t);
	}
}
