package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lille1.car2014.durieux_toulet.config.FTPConfiguration;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * Create on new FTP server and start waiting client connection
 *
 * @author Thomas Durieux
 * @author Toulet Cyrille
 */
public class FTPServerImpl implements FTPServer {
  /* Attributes */

  private final int port;
  private ServerSocket serverSocket;
  private ExecutorService executor;
  private boolean isStarted;

  /**
   * Constructor with default port
   */
  public FTPServerImpl() {
    port = 21;
  }

  /**
   * Constructor with custom port
   *
   * @param port The server port
   */
  public FTPServerImpl(final int port) {
    this.port = port;
    isStarted = false;
  }

  /**
   * @see FTPServer
   */
  @Override
  public void startServer() throws ServerSocketException {
    if (isStarted) {
      throw new ServerSocketException("The FTP server is already started");
    }
    try {
      // Create socket
      serverSocket = new ServerSocket(port);
      isStarted = true;
    } catch (final IOException e) {
      throw new ServerSocketException("Port " + port
              + " already used or reserved by the system.", e);
    }

    // Log server starting
    LoggerUtilities.log("FTP server started on port: " + port);

    executor = Executors.newFixedThreadPool(FTPConfiguration.INSTANCE
            .getIntProperty("maxConcurrentUser"));
    // Listen for client connections
    while (true) {
      try {
        final Socket clientSocket = serverSocket.accept();

        // Create client thread
        executor.execute(new FTPClientSocketImpl(clientSocket));
      } catch (final IOException e) {
        // The connection with the client is already closed, nothing to
        // do. The ftp client must stay alive and wait new client
        // connection
        LoggerUtilities.error(e);
      }
    }
  }

  /**
   * @see FTPServer
   */
  @Override
  public void closeServer() throws ServerSocketException {
    if (!isStarted) {
      throw new ServerSocketException("The FTP server is not started");
    }
    executor.shutdownNow();
    try {
      serverSocket.close();
      isStarted = false;
    } catch (final IOException e) {
      throw new ServerSocketException("Unable to close the FTP server", e);
    }
  }

  /**
   * @see FTPServer
   */
  @Override
  public int getPort() {
    return port;
  }

  /**
   * @see FTPServer
   */
  @Override
  public boolean isStarted() {
    return isStarted;
  }
}
