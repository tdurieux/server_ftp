package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.*;
import lille1.car2014.durieux_toulet.ftp_server.UserDatabase;

import org.junit.Test;

public class UserDatabaseTest {

	@Test
	public void testLoginWithBadUsername() {
		boolean isConnected = UserDatabase.INSTANCE.loginUser("NoUser", "test");
		assertEquals(isConnected, false);
	}
	
	@Test
	public void testLoginWithBadPassword() {
		boolean isConnected = UserDatabase.INSTANCE.loginUser("user", "test");
		assertEquals(isConnected, false);
	}
	
	@Test
	public void testLogin() {
		boolean isConnected = UserDatabase.INSTANCE.loginUser("user", "pass");
		assertEquals(isConnected, true);
	}

	@Test
	public void testUserExistWithBadUsername() {
		boolean exist = UserDatabase.INSTANCE.userExist("NoUser");
		assertEquals(exist, false);
	}
	
	@Test
	public void testUserExist() {
		boolean exist = UserDatabase.INSTANCE.userExist("user");
		assertEquals(exist, true);
	}

}
