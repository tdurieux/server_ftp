package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.assertEquals;
import lille1.car2014.durieux_toulet.common.UserDatabase;

import org.junit.Test;

public class UserDatabaseTest {

	@Test
	public void testLoginWithBadUsername() {
		final boolean isConnected = UserDatabase.INSTANCE.loginUser("NoUser",
				"test");
		assertEquals(isConnected, false);
	}

	@Test
	public void testLoginWithBadPassword() {
		final boolean isConnected = UserDatabase.INSTANCE.loginUser("user",
				"test");
		assertEquals(isConnected, false);
	}

	@Test
	public void testLogin() {
		final boolean isConnected = UserDatabase.INSTANCE.loginUser("user",
				"pass");
		assertEquals(isConnected, true);
	}

	@Test
	public void testUserExistWithBadUsername() {
		final boolean exist = UserDatabase.INSTANCE.userExist("NoUser");
		assertEquals(exist, false);
	}

	@Test
	public void testUserExist() {
		final boolean exist = UserDatabase.INSTANCE.userExist("user");
		assertEquals(exist, true);
	}

}
