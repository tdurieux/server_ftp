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
     * Main
     */
    public static void main (String [] args) {
        /* Test FTPClientSocketListener */
        Result result = JUnitCore.runClasses (FTPClientSocketListenerTest.class);

        for (Failure failure : result.getFailures ())
            System.out.println (failure.toString ());

        /* Test FTPClientSocketMockup */
        result = JUnitCore.runClasses (FTPClientSocketMockupTest.class);

        for (Failure failure : result.getFailures ())
            System.out.println (failure.toString ());

        /* Test FTPRequestHandler */
        result = JUnitCore.runClasses (FTPRequestHandlerTest.class);

        for (Failure failure : result.getFailures ())
            System.out.println (failure.toString ());

        /* Test FTPServer */
        result = JUnitCore.runClasses (FTPServerTest.class);

        for (Failure failure : result.getFailures ())
            System.out.println (failure.toString ());

        /* Test TransfertServer */
        result = JUnitCore.runClasses (TransfertServerTest.class);

        for (Failure failure : result.getFailures ())
            System.out.println (failure.toString ());

        /* Test UserDatabase */
        result = JUnitCore.runClasses (UserDatabaseTest.class);

        for (Failure failure : result.getFailures ())
            System.out.println (failure.toString ());
    }    

}

