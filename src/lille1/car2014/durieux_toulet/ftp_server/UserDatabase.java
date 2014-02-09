package lille1.car2014.durieux_toulet.ftp_server;

public interface UserDatabase {
	UserDatabase INSTANCE = new UserDatabaseImpl();

	boolean loginUser(String username, String password);
	
	boolean userExist(String username);
}
