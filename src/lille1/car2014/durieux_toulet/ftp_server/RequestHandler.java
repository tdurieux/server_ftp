package lille1.car2014.durieux_toulet.ftp_server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import lille1.car2014.durieux_toulet.exception.FTPClientException;
import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * Handle requests of FTPClient
 * 
 * @author Durieux Thomas
 * 
 */
public class RequestHandler {
	private final FTPClientImpl ftpClient;

	public RequestHandler(final FTPClientImpl ftpClient) {
		this.ftpClient = ftpClient;
	}

	public void parseStringRequest(final String request)
			throws RequestHandlerException {
		final String[] requestSplitted = request.split(" ");
		final String command = requestSplitted[0];
		this.executeRquest(command, requestSplitted);
	}

	/**
	 * Execute the function associate to a command
	 * 
	 * @param command
	 *            the command send by the client
	 * @param parameters
	 *            parameters of the command, the first element is the command
	 * @throws RequestHandlerException
	 */
	private void executeRquest(final String command, final String[] parameters)
			throws RequestHandlerException {
		// list all methods of this class
		final Method[] allMethods = this.getClass().getDeclaredMethods();
		for (int i = 0; i < allMethods.length; i++) {
			final Method method = allMethods[i];
			final FtpRequestAnnotation annotation = method
					.getAnnotation(FtpRequestAnnotation.class);
			// verify that the annotation have on annotation
			if (annotation != null) {
				if (annotation.name().toLowerCase()
						.compareTo(command.toLowerCase()) != 0) {
					continue;
				}
				// Check the signature of the methods
				final Class<?>[] types = method.getParameterTypes();
				if (types.length != parameters.length - 1) {
					continue;
				}

				// parameters of the function
				final Object[] args = new Object[types.length];
				for (int j = 0; j < types.length; j++) {
					final String type = types[j].getName();
					if (type.compareTo(parameters[j + 1].getClass().getName()) == 0) {
						args[j] = parameters[j + 1];
					} else {
						throw new RequestHandlerException(
								"Arguement not valid " + parameters[j + 1]);
					}
				}
				// if the user must be connected before doing an command
				if (annotation.connected() && !ftpClient.isConnected()) {
					ftpClient.writeMessage("530 Not logged in.");
					return;
				}
				// invoke the methos
				try {
					method.invoke(this, args);
					return;
				} catch (final IllegalAccessException e) {
					throw new RequestHandlerException("Command not found "
							+ command, e);
				} catch (final IllegalArgumentException e) {
					throw new RequestHandlerException("Arguement not valid ", e);
				} catch (final InvocationTargetException e) {
					throw new RequestHandlerException("Error execution "
							+ command + " error: " + e.getMessage(), e);
				}
			}
		}
		throw new RequestHandlerException("Command not found " + command);
	}

	/**
	 * Define the primary encoding of the communication
	 * 
	 * @param typeCharacter
	 */
	private void setPrimaryTypeCaracter(final String typeCharacter) {
		switch (typeCharacter) {
		case "A":
			ftpClient.setTypeCharactor("ASCII");
			break;
		case "E":
			ftpClient.setTypeCharactor("EBCDIC");
			break;
		case "I":
			ftpClient.setTypeCharactor("image");
			break;
		case "L":
			ftpClient.setTypeCharactor("local");
			break;
		default:

			break;
		}
	}

	/**
	 * Define the primary encoding of the communication and send response to the
	 * client
	 * 
	 * @param typeCharacter
	 */
	@FtpRequestAnnotation(name = "TYPE", connected = true)
	private void requestType(final String typeCharacter) {
		this.setPrimaryTypeCaracter(typeCharacter);
		ftpClient.writeMessage("200 Type accepted");
	}

	@FtpRequestAnnotation(name = "TYPE", connected = true)
	private void requestType(final String typeCharacter,
			final String secondTypeCharacter) {
		this.setPrimaryTypeCaracter(typeCharacter);

		switch (secondTypeCharacter) {
		case "N":
			ftpClient.setTypeCharactor("Non-print");
			break;
		case "T":
			ftpClient.setTypeCharactor("Telnet");
			break;
		case "C":
			ftpClient.setTypeCharactor("ASA");
			break;
		default:

			break;
		}
		ftpClient.writeMessage("200 Type accepted");
	}

	/**
	 * Login the user
	 * 
	 * @param password
	 *            the password of the user
	 * @throws RequestHandlerException
	 */
	@FtpRequestAnnotation(name = "PASS", connected = false)
	private void requestConnect(final String password)
			throws RequestHandlerException {
		try {
			// connect the user
			if (ftpClient.connect(password)) {
				ftpClient.writeMessage("230 Connected");
			} else {
				ftpClient.writeMessage("430 Invalid username/password");
			}
		} catch (FTPClientException e) {
			LoggerUtilities.error("Unable to log the user", e);
			ftpClient.writeMessage("430 Invalid username/password");
		}

	}

	/**
	 * Define the username of the client
	 * 
	 * @param username
	 *            the username of the client
	 */
	@FtpRequestAnnotation(name = "USER", connected = false)
	private void requestUser(final String username) {
		ftpClient.setUsername(username);
		ftpClient.writeMessage("331 User accepted");
	}

	/**
	 * Close the connection
	 */
	@FtpRequestAnnotation(name = "QUIT", connected = false)
	private void closeConnection() {
		ftpClient.close();
	}

	/**
	 * Request the os type
	 */
	@FtpRequestAnnotation(name = "SYST", connected = true)
	private void requestSYST() {
		// final String OS = System.getProperty("os.name").toLowerCase();
		// this.ftpClient.writeMessage("215 " + OS);
		ftpClient.writeMessage("215 UNIX Type: L8");
	}

	/**
	 * Save an option
	 * 
	 * @param key
	 *            the name of the option
	 * @param value
	 *            the value of the option
	 */
	@FtpRequestAnnotation(name = "OPTS", connected = true)
	private void requestOptions(final String key, final String value) {
		ftpClient.getOptions().put(key, value);
		ftpClient.writeMessage("200 Accept option");
	}

	/**
	 * Get the current directory
	 */
	@FtpRequestAnnotation(name = "PWD", connected = true)
	private void requestCurrentDirectory() {
		ftpClient.writeMessage("257 " + '"' + ftpClient.getCurrentDir() + '"');
	}

	/**
	 * Change the current directory to dir
	 * 
	 * @param dir
	 *            the new current direcotry
	 */
	@FtpRequestAnnotation(name = "CWD", connected = true)
	private void requestSetCurrentDirecory(String dir) {
		File f = new File(dir);
		if (!f.exists() || !f.isDirectory()) {
			f = new File(ftpClient.getCurrentDir() + "/" + dir);
			if (!f.exists() || !f.isDirectory()) {
				ftpClient
						.writeMessage("550 Can't change directory to test: No such file or directory");
			}
		}
		ftpClient.setCurrentDir(f.getAbsolutePath());
		ftpClient.writeMessage("250 OK. Current directory is "
				+ f.getAbsolutePath());
	}

	/**
	 * Change the current directory to parent directory
	 * 
	 * @param dir
	 *            the new current direcotry
	 */
	@FtpRequestAnnotation(name = "CDUP", connected = true)
	private void requestUpCurrentDirecory() {
		File f = new File(ftpClient.getCurrentDir() + "/..");
		if (!f.exists() || !f.isDirectory()) {
			ftpClient
					.writeMessage("550 Can't change directory to test: No such file or directory");
		}
		ftpClient.setCurrentDir(f.getAbsolutePath());
		ftpClient.writeMessage("250 OK. Current directory is "
				+ f.getAbsolutePath());
	}

	/**
	 * Send the port of the data connection (passive mode)
	 */
	@FtpRequestAnnotation(name = "PASV", connected = true)
	private void requestPassiveMode() {
		try {
			final int port = ftpClient.createNewTransfert();
			final int p1 = (port / 256);
			final int p2 = port - p1 * 256;
			ftpClient.writeMessage("227 Entering Passive Mode (127,0,0,1," + p1
					+ "," + p2 + ")");
		} catch (final SocketException e) {
			LoggerUtilities.error(e);
			ftpClient.writeMessage("425 Can't open data connection.");
		}
	}

	/**
	 * Get the port of the data connection (extended passive mode)
	 */
	@FtpRequestAnnotation(name = "EPSV", connected = true)
	private void requestExtendedPassiveMode() {
		try {
			final int port = ftpClient.createNewTransfert();
			ftpClient.writeMessage("229 Entering Extended Passive Mode (|||"
					+ port + "|)");
		} catch (final SocketException e) {
			LoggerUtilities.error(e);
			ftpClient.writeMessage("425 Can't open data connection.");
		}
	}

	/**
	 * List all files of the directory dir
	 * 
	 * @param dir
	 *            the directory to list
	 */
	@FtpRequestAnnotation(name = "List", connected = true)
	private void requestListFiles(String dir) {
		if (dir.compareTo("-a") == 0) {
			dir = "";
		}
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				ftpClient.getTransfertServer().writeContent(
						this.createList(ftpClient.getCurrentDir() + "/" + dir));
				ftpClient.writeMessage("226");
			} catch (final RequestHandlerException e) {
				LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
			}
		}
	}

	/**
	 * List all file of the current directory
	 */
	@FtpRequestAnnotation(name = "List", connected = true)
	private void requestListFiles() {
		this.requestListFiles("");
	}

	/**
	 * List all file name of the directory dir
	 */
	@FtpRequestAnnotation(name = "NLST", connected = true)
	private void requestListFileName(final String dir) {
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			try {
				final File folder = new File(ftpClient.getCurrentDir() + "/"
						+ dir);
				if (!folder.exists() || !folder.isDirectory()) {
					ftpClient.writeMessage("504 Only accept folder");
				} else {
					ftpClient.writeMessage("150 Accepted data connection");
					final String[] files = folder.list();
					String listFilename = "";
					for (int i = 0; i < files.length; i++) {
						listFilename += files[i]
								+ ((i != files.length - 1) ? "\n" : "");

					}
					ftpClient.getTransfertServer().writeContent(listFilename);
					ftpClient.writeMessage("226 " + files.length
							+ " matches total");
				}
			} catch (final RequestHandlerException e) {
					LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
			}
		}
	}

	/**
	 * List all file name of the current directory
	 */
	@FtpRequestAnnotation(name = "NLST", connected = true)
	private void requestListFileName() {
		this.requestListFileName("");
	}

	@FtpRequestAnnotation(name = "SIZE", connected = true)
	private void requestGetFileSize(final String file) {
		final File f = new File(file);
		ftpClient.writeMessage("226 " + f.length());
	}

	/**
	 * Send the content of a file to the client
	 * 
	 * @param file
	 *            the file to send
	 */
	@FtpRequestAnnotation(name = "RETR", connected = true)
	private void requestDownloadFile(final String file) {
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				try {
					final byte[] encoded = Files.readAllBytes(Paths
							.get(ftpClient.getCurrentDir() + "/" + file));
					ftpClient.getTransfertServer().writeContent(encoded);
					ftpClient.writeMessage("226 File successfully transferred");
				} catch (final IOException e) {
					LoggerUtilities.error(e);
					ftpClient.getTransfertServer().close();
				}

			} catch (final RequestHandlerException e) {
				LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
			}
		}
	}

	/**
	 * Send the content of a file to the server
	 * 
	 * @param file The name of file to store
	 */
	@FtpRequestAnnotation(name = "STOR", connected = true)
	private void requestUploadFile(final String file) {
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				byte [] content = ftpClient.getTransfertServer().readContent();

				FileOutputStream out = new FileOutputStream(ftpClient.getCurrentDir() + "/" + file);

				out.write(content);
				out.close();

				ftpClient.writeMessage("226 File successfully transferred");
			} catch (final IOException e) {
				LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
			}
		}
	}

	/**
	 * Get last modification date
	 * 
	 * @param file
	 */
	@FtpRequestAnnotation(name = "MDTM", connected = true)
	private void requestGetLastModificationDate(final String file) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm",
					Locale.ENGLISH);
			Date lastModificationDate = new Date(Files.getLastModifiedTime(
					Paths.get(ftpClient.getCurrentDir() + "/" + file))
					.toMillis());
			ftpClient.writeMessage("226 " + sdf.format(lastModificationDate));
		} catch (IOException e) {
			ftpClient
					.writeMessage("451 Unable to access to last modification date.");
		}

	}

	/**
	 * Create the list of all file of the direcotry dir
	 * 
	 * @param dir
	 *            the directory to list
	 * @return a string containing all file of the dir
	 */
	private String createList(final String dir) {
		String result = "";
		final File folder = new File(dir);
		if (!folder.exists() || !folder.isDirectory()) {
			return "not good";
		}
		final File[] files = folder.listFiles();
		final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:ss",
				Locale.ENGLISH);
		for (int i = 0; i < files.length; i++) {
			final File file = files[i];
			result += (file.isDirectory() ? "d" : "-");

			String owner = "unknown";
			String group = "group";
			String permissionsString = (file.isDirectory() ? "d" : "-")
					+ (file.canRead() ? 'r' : '-')
					+ (file.canWrite() ? 'w' : '-')
					+ (file.canExecute() ? 'x' : '-') + "r--r--";
			try {
				final PosixFileAttributes attr = Files.readAttributes(
						Paths.get(file.getAbsolutePath()),
						PosixFileAttributes.class);
				group = attr.group().getName();
				owner = attr.owner().getName();
				final Set<PosixFilePermission> permissions = attr.permissions();
				permissionsString = (permissions
						.contains(PosixFilePermission.OWNER_READ) ? "r" : "-")
						+ (permissions
								.contains(PosixFilePermission.OWNER_WRITE) ? "w"
								: "-")
						+ (permissions
								.contains(PosixFilePermission.OWNER_EXECUTE) ? "x"
								: "-")
						+ (permissions.contains(PosixFilePermission.GROUP_READ) ? "r"
								: "-")
						+ (permissions
								.contains(PosixFilePermission.GROUP_WRITE) ? "w"
								: "-")
						+ (permissions
								.contains(PosixFilePermission.GROUP_EXECUTE) ? "x"
								: "-")
						+ (permissions
								.contains(PosixFilePermission.OTHERS_READ) ? "r"
								: "-")
						+ (permissions
								.contains(PosixFilePermission.OTHERS_WRITE) ? "w"
								: "-")
						+ (permissions
								.contains(PosixFilePermission.OTHERS_EXECUTE) ? "x"
								: "-");
			} catch (final InvalidPathException e) {
				// use the default value
			} catch (final IOException e2) {
				// use the default value
			}

			result += permissionsString + " 1 " + owner + " " + group + " "
					+ file.length() + " " + sdf.format(file.lastModified())
					+ " " + file.getName() + "\r\n";
		}
		return result;
	}

}
