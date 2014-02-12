package lille1.car2014.durieux_toulet.ftp_server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to match FTP command to a method
 * 
 * @author Thomas Durieux
 * @author Toulet Cyrille
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FTPRequestAnnotation {

	/**
	 * The FTP command name
	 * 
	 * @return the command name
	 */
	String name();

	/**
	 * Tell if the used must be connected to execute this method
	 * 
	 * @return True if the user hate to be connected, false else
	 */
	boolean connected() default true;

	/**
	 * Tell if this method can be called by a anonymous user
	 * 
	 * @return True if the user can be anonymous, false else
	 */
	boolean anonymous() default false;

}
