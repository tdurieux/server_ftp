package ftp_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {

	private final Properties prop;
	private final File f;

	public Settings(String fileName) {
		prop = new Properties();
		f = new File(fileName);
		if (!f.exists()) {
			try {

				prop.store(new FileOutputStream(f), null);
			} catch (IOException ex) {
				Logger.getLogger(Settings.class.getName()).log(Level.SEVERE,
						null, ex);
			}
			try {
				// load a properties file
				prop.load(new FileInputStream(f));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	public void setProperty(String key, String value) {
		prop.setProperty(key, value);
		try {
			prop.store(new FileOutputStream(f), null);
		} catch (IOException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

}
