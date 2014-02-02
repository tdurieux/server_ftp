package lille1.car2014.durieux_toulet.config;

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

	public Configuration(final String fileName) {
		this.configurationProperties = new Properties();
		this.f = new File(fileName);
		if (!this.f.exists()) {
			try {
				this.configurationProperties.store(
						new FileOutputStream(this.f), null);
			} catch (final IOException ex) {
				Logger.getLogger(Configuration.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			try {
				// load a properties file
				this.configurationProperties.load(new FileInputStream(this.f));
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getProperty(final String key) {
		return this.configurationProperties.getProperty(key);
	}

	public void setProperty(final String key, final String value) {
		this.configurationProperties.setProperty(key, value);
		try {
			this.configurationProperties.store(new FileOutputStream(this.f),
					null);
		} catch (final IOException ex) {
			Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

}
