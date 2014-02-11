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
	public UserDatabaseImpl() {}

    /**
     * @see UserDatabase
     */
	@Override
	public boolean userExist(String username) {
		return FTPUserDatabase.INSTANCE.getProperty(username) != null;
	}

    /**
     * @see UserDatabase
     */
	@Override
	public boolean loginUser(String username, String password) {
		String passwd = FTPUserDatabase.INSTANCE.getProperty(username);

		return (passwd != null && password.compareTo(passwd) == 0);
	}
}
