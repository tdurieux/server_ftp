package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import lille1.car2014.durieux_toulet.config.Configuration;
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

	// User database
	private Configuration udb;

	/**
	 * Constructor
	 *
	 * @throws UserDatabaseException when unable to load user DB
	 */
	private UserDatabase () throws UserDatabaseException {
		try {
			this.udb = new Configuration(Configuration.class
					.getResource("db_user.ini").openStream());
		} catch (IOException e) {
			throw new UserDatabaseException ("Unable to load user database", e);
		}
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

		String passwd = this.udb.getProperty(username);

		if (passwd == null || password.compareTo (passwd) != 0)
			sign = false;

		return sign;
	}
}
