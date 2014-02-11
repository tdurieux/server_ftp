package test.lille1.car2014.durieux_toulet.ftp_server;

import lille1.car2014.durieux_toulet.ftp_server.*;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * @author Toulet Cyrille
 */
public class JUnitRunner {

    /**
     * Run JUnit tests
     */
    public static void run () {
        Result result = JUnitCore.runClasses (FTPServer.class);

        for (Failure failure : result.getFailures ())
            System.out.println (failure.toString ());
    }

    /**
     * Main
     */
    public static void main (String [] args) {
        JUnitRunner.run ();
    }    

}

