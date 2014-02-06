package lille1.car2014.durieux_toulet.ftp_server;

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
	 */
	private UserDatabase () {
	
	}


	public static UserDatabase getInstance () {
		if (UserDatabase.instance == null)
			UserDatabase.instance = new UserDatabase ();

		return UserDatabase.instance;
	}

	/**
	 * Signin
	 *
	 * @param user Username
	 * @param password Password
	 */
	public boolean signin (String user, String password) {
		boolean sign = true;

		if (user.compareTo ("user") != 0)
			sign = false;

		if (password.compareTo ("pass") != 0)
			sign = false;

		return sign;
	}
}
