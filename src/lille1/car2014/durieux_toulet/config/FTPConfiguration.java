package lille1.car2014.durieux_toulet.config;

public interface FTPConfiguration extends PropertiesUtility{
	PropertiesUtility INSTANCE = new PropertiesUtilityImpl(
			FTPConfiguration.class.getResourceAsStream("ftp_config.ini"));
}
