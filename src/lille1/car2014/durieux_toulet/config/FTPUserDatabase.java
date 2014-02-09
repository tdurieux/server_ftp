package lille1.car2014.durieux_toulet.config;

public interface FTPUserDatabase extends PropertiesUtility {
	PropertiesUtility INSTANCE = new PropertiesUtilityImpl(
			FTPUserDatabase.class.getResourceAsStream("db_user.ini"));
}
