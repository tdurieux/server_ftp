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
public class Configuration {

	private final Properties configurationProperties;

	public Configuration(final String fileName) {
		configurationProperties = new Properties();
		File f = new File(fileName);
		if (!f.exists()) {
			try {
				configurationProperties.store(new FileOutputStream(f), null);
			} catch (final IOException ex) {
				Logger.getLogger(Configuration.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			try {
				// load a properties file
				configurationProperties.load(new FileInputStream(f));
			} catch (final IOException ex) {
				LoggerUtilities.error(ex);
			}
		}
	}

	public Configuration(InputStream openStream) {
		configurationProperties = new Properties();
		try {
			configurationProperties.load(openStream);
		} catch (IOException e) {
			LoggerUtilities.error(e);
		}
	}

	public String getProperty(final String key) {
		return configurationProperties.getProperty(key);
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
