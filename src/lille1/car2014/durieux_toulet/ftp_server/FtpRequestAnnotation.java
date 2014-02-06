package lille1.car2014.durieux_toulet.ftp_server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FtpRequestAnnotation {

	String name();

	boolean connected() default true;
	
	boolean annonymous() default false;

}
