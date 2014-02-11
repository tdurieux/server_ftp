package lille1.car2014.durieux_toulet.config;

/**
 * FTP user base
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPUserDatabase extends PropertiesUtility {
    /* Set instance */
	PropertiesUtility INSTANCE = new PropertiesUtilityImpl (
		FTPUserDatabase.class.getResourceAsStream ("db_user.ini")
    );
}
