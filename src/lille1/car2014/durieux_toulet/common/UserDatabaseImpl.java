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
	 * 
	 * @throws UserDatabaseException
	 *             when unable to load user DB
	 */
	public UserDatabaseImpl() {}

	@Override
	public boolean userExist(String username) {
		return FTPUserDatabase.INSTANCE.getProperty(username) != null;
	}

	/**
	 * Signin
	 * 
	 * @param username
	 *            Username
	 * @param password
	 *            Password
	 */
	@Override
	public boolean loginUser(String username, String password) {
		String passwd = FTPUserDatabase.INSTANCE.getProperty(username);

		return (passwd != null && password.compareTo(passwd) == 0);
	}
}
