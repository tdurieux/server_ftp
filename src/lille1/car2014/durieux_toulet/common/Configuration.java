package lille1.car2014.durieux_toulet.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Configuration class is a utility class used to load ini file
 * 
 * @author Thomas Durieux
 * 
 */
public class Configuration {

	private final Properties configurationProperties;
	private final File f;

	public Configuration(String fileName) {
		configurationProperties = new Properties();
		f = new File(fileName);
		if (!f.exists()) {
			try {
				configurationProperties.store(new FileOutputStream(f), null);
			} catch (IOException ex) {
				Logger.getLogger(Configuration.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			try {
				// load a properties file
				configurationProperties.load(new FileInputStream(f));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getProperty(String key) {
		return configurationProperties.getProperty(key);
	}

	public void setProperty(String key, String value) {
		configurationProperties.setProperty(key, value);
		try {
			configurationProperties.store(new FileOutputStream(f), null);
		} catch (IOException ex) {
			Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

}
