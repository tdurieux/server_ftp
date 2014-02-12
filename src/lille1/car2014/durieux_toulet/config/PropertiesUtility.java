package lille1.car2014.durieux_toulet.config;

/**
 * Properties utility used to manage properties
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public interface PropertiesUtility {
	/**
	 * Get property
	 * 
	 * @param key
	 *            The property token
	 * @return The property
	 */
	String getProperty(String key);

	/**
	 * Get property
	 * 
	 * @param key
	 *            The property token
	 * @return The int property
	 */
	int getIntProperty(String key);

	/**
	 * Get property
	 * 
	 * @param key
	 *            The property token
	 * @return The boolean property
	 */
	boolean getBooleanProperty(String key);
}
