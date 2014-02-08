package lille1.car2014.durieux_toulet.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * The Configuration class is a utility class used to load ini file
 * 
 * @author Thomas Durieux
 * 
 */
public class PropertiesUtilityImpl implements PropertiesUtility{

	private final Properties properties;

	public PropertiesUtilityImpl(final String fileName) {
		properties = new Properties();
		File f = new File(fileName);
		if (!f.exists()) {
			try {
				properties.store(new FileOutputStream(f), null);
			} catch (final IOException ex) {
				Logger.getLogger(PropertiesUtilityImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			try {
				// load a properties file
				properties.load(new FileInputStream(f));
			} catch (final IOException ex) {
				LoggerUtilities.error(ex);
			}
		}
	}

	public PropertiesUtilityImpl(InputStream openStream) {
		properties = new Properties();
		try {
			properties.load(openStream);
		} catch (IOException e) {
			LoggerUtilities.error(e);
		}
	}

	public String getProperty(final String key) {
		return properties.getProperty(key);
	}

	public boolean getBooleanProperty(final String key) {
		return Boolean.parseBoolean(properties.getProperty(key));
	}
	public int getIntProperty(final String key) {
		return Integer.parseInt(properties.getProperty(key));
	}
	/*
	 * public void setProperty(final String key, final String value) {
	 * configurationProperties.setProperty(key, value); try {
	 * configurationProperties.store(new FileOutputStream(f), null); } catch
	 * (final IOException ex) {
	 * Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null,
	 * ex); } }
	 */

}
