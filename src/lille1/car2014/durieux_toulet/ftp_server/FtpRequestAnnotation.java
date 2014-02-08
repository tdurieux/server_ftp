package lille1.car2014.durieux_toulet.ftp_server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to match FTP command to a method
 * @author Thomas Durieux
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FtpRequestAnnotation {
	/**
	 * The FTP command name
	 * @return the command name
	 */
	String name();
	/**
	 * If the used must be connected to execute this method
	 * @return
	 */
	boolean connected() default true;
	
	/**
	 * If this method can be called by a anonymous
	 * @return
	 */
	boolean anonymous() default false;

}
