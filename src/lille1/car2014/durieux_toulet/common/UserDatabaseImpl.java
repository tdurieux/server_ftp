package lille1.car2014.durieux_toulet.common;

import lille1.car2014.durieux_toulet.config.FTPUserDatabase;

/**
 * User database
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class UserDatabaseImpl implements UserDatabase {

	/**
	 * Constructor
	 */
	public UserDatabaseImpl() {
	}

	/**
	 * @see UserDatabase
	 */
	@Override
	public boolean userExist(final String username) {
		return FTPUserDatabase.INSTANCE.getProperty(username) != null;
	}

	/**
	 * @see UserDatabase
	 */
	@Override
	public boolean loginUser(final String username, final String password) {
		final String passwd = FTPUserDatabase.INSTANCE.getProperty(username);

		return (passwd != null && password.compareTo(passwd) == 0);
	}
}
