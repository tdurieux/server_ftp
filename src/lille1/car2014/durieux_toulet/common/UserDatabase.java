package lille1.car2014.durieux_toulet.common;

/**
 * User database implementation
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface UserDatabase {
	/* Instance */
	UserDatabase INSTANCE = new UserDatabaseImpl();

	/**
	 * Tell if user exists or not
	 * @param username Username
	 * @return True if user exists, false else
	 */
	boolean userExist(String username);

	/**
	 * Signin
	 * 
	 * @param username Username
	 * @param password Password
	 * @return Succes
	 */
	boolean loginUser(String username, String password);
}
