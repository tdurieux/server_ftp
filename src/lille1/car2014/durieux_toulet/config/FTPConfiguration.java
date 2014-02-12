package lille1.car2014.durieux_toulet.config;

/**
 * FTP configuration
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface FTPConfiguration extends PropertiesUtility {
	/* Set instance */
	PropertiesUtility INSTANCE = new PropertiesUtilityImpl(
			FTPConfiguration.class.getResourceAsStream("ftp_config.ini"));
}
