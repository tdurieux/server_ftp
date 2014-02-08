package lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.config.FTPUserDatabase;
import lille1.car2014.durieux_toulet.exception.UserDatabaseException;

/**
 * User database
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class UserDatabase {
	// Instance
	private static UserDatabase instance;

	/**
	 * Constructor
	 *
	 * @throws UserDatabaseException when unable to load user DB
	 */
	private UserDatabase () throws UserDatabaseException {
	}


	/**
	 * Get singleton
	 *
	 * @throws UserDatabaseException when unable to load user DB
	 */
	public static UserDatabase getInstance () throws UserDatabaseException {
		if (UserDatabase.instance == null)
			UserDatabase.instance = new UserDatabase ();

		return UserDatabase.instance;
	}

	/**
	 * Signin
	 *
	 * @param username Username
	 * @param password Password
	 */
	public boolean signin (String username, String password) {
		boolean sign = true;

		String passwd = FTPUserDatabase.INSTANCE.getProperty(username);

		if (passwd == null || password.compareTo (passwd) != 0)
			sign = false;

		return sign;
	}
}
