package lille1.car2014.durieux_toulet.ftp_server;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.Arrays;
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
public class FTPRequestHandlerImpl implements FTPRequestHandler {
	private final FTPClient ftpClient;

	public FTPRequestHandlerImpl(final FTPClientImpl ftpClient) {
		this.ftpClient = ftpClient;
	}

	public void parseStringRequest(final String request)
			throws RequestHandlerException {
		final String[] requestSplitted = request.split(" ");
		final String command = requestSplitted[0];
		if (requestSplitted.length > 1)
			this.executeRquest(command, Arrays.copyOfRange(requestSplitted, 1,
					requestSplitted.length));
		else
			this.executeRquest(command, new String[0]);
	}

	/**
	 * Execute the function associate to a command
	 * 
	 * @param command
	 *            the command send by the client
	 * @param parameters
	 *            parameters of the command
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
				if (types.length > parameters.length) {
					continue;
				}
				final Object[] args = new Object[types.length];
				for (int j = 0; j < types.length; j++) {
					final String type = types[j].getName();
					if (type.compareTo(parameters[j].getClass().getName()) == 0) {
						args[j] = parameters[j];
					} else if (type.compareTo("[Ljava.lang.String;") == 0) {
						String[] stringArgs = new String[parameters.length - j];
						for (int k = j, l = 0; k < parameters.length; k++, l++) {
							stringArgs[l] = parameters[k];
						}
						args[j] = stringArgs;
						break;
					} else {
						throw new RequestHandlerException(
								"Arguement not valid " + parameters[j]);
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
		if (username.compareTo("anonymous") == 0) {
			ftpClient.writeMessage("230 Anonyme");
		} else {
			ftpClient.writeMessage("331 User accepted");
		}

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
	@FtpRequestAnnotation(name = "SYST", connected = true, anonymous = true)
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
	@FtpRequestAnnotation(name = "OPTS", connected = true, anonymous = true)
	private void requestOptions(final String key, final String value) {
		ftpClient.getOptions().put(key, value);
		ftpClient.writeMessage("200 Accept option");
	}

	/**
	 * Get the current directory
	 */
	@FtpRequestAnnotation(name = "PWD", connected = true, anonymous = true)
	private void requestCurrentDirectory() {
		ftpClient.writeMessage("257 " + '"' + ftpClient.getCurrentDir() + '"');
	}

	private String arrayToPath(String[] dirs) {
		String path = "";
		for (int i = 0; i < dirs.length; i++) {
			String dir = dirs[i];
			path += dir + (i != dirs.length - 1 ? " " : "");
		}
		return path;
	}

	/**
	 * Change the current directory to dir
	 * 
	 * @param dir
	 *            the new current direcotry
	 */
	@FtpRequestAnnotation(name = "CWD", connected = true, anonymous = true)
	private void requestSetCurrentDirecory(String... dirs) {
		String path = arrayToPath(dirs);
		File f = new File(path);
		if (!f.exists() || !f.isDirectory()) {
			f = new File(ftpClient.getCurrentDir() + "/" + path);
			if (!f.exists() || !f.isDirectory()) {
				ftpClient
						.writeMessage("550 Can't change directory to test: No such file or directory");
			} else {
				ftpClient.setCurrentDir(f.getAbsolutePath());
				ftpClient.writeMessage("250 OK. Current directory is "
						+ f.getAbsolutePath());
			}
		} else {
			ftpClient.setCurrentDir(f.getAbsolutePath());
			ftpClient.writeMessage("250 OK. Current directory is "
					+ f.getAbsolutePath());
		}
	}

	/**
	 * Change the current directory to parent directory
	 * 
	 * @param dir
	 *            the new current direcotry
	 */
	@FtpRequestAnnotation(name = "CDUP", connected = true, anonymous = true)
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
	 * Send the port of the data connection (active mode)
	 */
	@FtpRequestAnnotation(name = "PORT", connected = true, anonymous = true)
	private void requestPort(String addressStirng) {
		try {
			String[] addressArray = addressStirng.split(",");
			String address = addressArray[0] + "." + addressArray[1] + "."
					+ addressArray[2] + "." + addressArray[3];
			final int port = Integer.parseInt(addressArray[4]) * 256
					+ Integer.parseInt(addressArray[5]);
			ftpClient.createNewTransfert(address, port);
			ftpClient.writeMessage("227 Entering Active Mode");
		} catch (final SocketException e) {
			LoggerUtilities.error(e);
			ftpClient.writeMessage("425 Can't open data connection.");
		}
	}

	/**
	 * Send the port of the data connection (passive mode)
	 */
	@FtpRequestAnnotation(name = "PASV", connected = true, anonymous = true)
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
	@FtpRequestAnnotation(name = "EPSV", connected = true, anonymous = true)
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
	@FtpRequestAnnotation(name = "List", connected = true, anonymous = true)
	private void requestListFiles(String... dirs) {
		String path = arrayToPath(dirs);
		if (path.compareTo("-a") == 0) {
			path = "";
		}
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				if (path.length() > 0 && path.charAt(0) == '/') {
					ftpClient.getTransfertServer().writeContent(
							this.createList(path));
				} else {
					ftpClient.getTransfertServer().writeContent(
							this.createList(ftpClient.getCurrentDir() + "/"
									+ path));
				}
				ftpClient.writeMessage("226 List transefered");
			} catch (final RequestHandlerException e) {
				LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
				ftpClient.writeMessage("426 Unable to send file list");
			} catch (SocketException e) {
				LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
				ftpClient.writeMessage("426 Unable to send file list");
			}
		}
	}

	/**
	 * List all file of the current directory
	 */
	@FtpRequestAnnotation(name = "List", connected = true, anonymous = true)
	private void requestListFiles() {
		this.requestListFiles("");
	}

	/**
	 * List all file name of the directory dir
	 */
	@FtpRequestAnnotation(name = "NLST", connected = true, anonymous = true)
	private void requestListFileName(final String... dirs) {
		String path = arrayToPath(dirs);
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			try {
				final File folder = new File((path.length() > 0
						&& path.charAt(0) == '/' ? ftpClient.getCurrentDir()
						+ "/" : "")
						+ path);
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
				ftpClient.writeMessage("426 Unable to send file list");
			} catch (SocketException e) {
				LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
				ftpClient.writeMessage("426 Unable to send file list");
			}
		}
	}

	/**
	 * List all file name of the current directory
	 */
	@FtpRequestAnnotation(name = "NLST", connected = true, anonymous = true)
	private void requestListFileName() {
		this.requestListFileName("");
	}

	@FtpRequestAnnotation(name = "SIZE", connected = true, anonymous = true)
	private void requestGetFileSize(final String... dirs) {
		String path = arrayToPath(dirs);
		final File f = new File(
				(path.length() > 0 && path.charAt(0) == '/' ? ftpClient
						.getCurrentDir() + "/" : "")
						+ path);
		ftpClient.writeMessage("226 " + f.length());
	}

	/**
	 * Send the content of a file to the client
	 * 
	 * @param file
	 *            the file to send
	 */
	@FtpRequestAnnotation(name = "RETR", connected = true, anonymous = true)
	private void requestDownloadFile(final String... dirs) {
		String path = arrayToPath(dirs);
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				String filePath = (path.length() > 0 && path.charAt(0) == '/' ? ""
						: ftpClient.getCurrentDir() + "/")
						+ path;
				FileInputStream fis = new FileInputStream(filePath);
				ftpClient.getTransfertServer().writeContent(fis);
				ftpClient.writeMessage("226 File successfully transferred");
			} catch (final IOException e) {
				LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
				ftpClient.writeMessage("426 Unable to send file");
			} catch (SocketException e) {
				LoggerUtilities.error(e);
				ftpClient.getTransfertServer().close();
				ftpClient.writeMessage("426 Unable to send file");
			}

		}
	}

	/**
	 * Send the content of a file to the server
	 * 
	 * @param file
	 *            The name of file to store
	 */
	@FtpRequestAnnotation(name = "STOR", connected = true, anonymous = false)
	private void requestUploadFile(final String[] dirs) {
		String path = arrayToPath(dirs);
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				byte[] content = ftpClient.getTransfertServer().readContent();

				FileOutputStream out = new FileOutputStream((path.length() > 0
						&& path.charAt(0) == '/' ? ftpClient.getCurrentDir()
						+ "/" : "")
						+ path);

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
	@FtpRequestAnnotation(name = "MDTM", connected = true, anonymous = true)
	private void requestGetLastModificationDate(final String... dirs) {
		String path = arrayToPath(dirs);
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm",
					Locale.ENGLISH);
			Date lastModificationDate = new Date(Files
					.getLastModifiedTime(
							Paths.get((path.length() > 0
									&& path.charAt(0) == '/' ? ftpClient
									.getCurrentDir() + "/" : "")
									+ path)).toMillis());
			ftpClient.writeMessage("226 " + sdf.format(lastModificationDate));
		} catch (IOException e) {
			ftpClient
					.writeMessage("451 Unable to access to last modification date.");
		}

	}

	/**
	 * Create a folder
	 * 
	 * @param folderName
	 */
	@FtpRequestAnnotation(name = "MKD", connected = true, anonymous = false)
	private void requestCreateFolder(final String... dirs) {
		String path = arrayToPath(dirs);
		String folderPath = "";
		if (path.length() > 0 && path.charAt(0) == '/') {
			folderPath = path;
		} else {
			folderPath = ftpClient.getCurrentDir() + "/" + path;
		}
		boolean success = (new File(folderPath)).mkdirs();
		if (success) {
			ftpClient.writeMessage("226 Folder created: " + path);
		} else {
			ftpClient.writeMessage("451 Folder not created: " + path);
		}
	}

	/**
	 * Remove a folder
	 * 
	 * @param folderName
	 */
	@FtpRequestAnnotation(name = "RMD", connected = true, anonymous = false)
	private void requestRemoveFolder(final String... dirs) {
		String path = arrayToPath(dirs);
		String folderPath = "";
		if (path.length() > 0 && path.charAt(0) == '/') {
			folderPath = path;
		} else {
			folderPath = ftpClient.getCurrentDir() + "/" + path;
		}
		boolean success = (new File(folderPath)).delete();
		if (success) {
			ftpClient.writeMessage("226 Folder removed: " + path);
		} else {
			ftpClient.writeMessage("451 Folder not removed: " + path);
		}
	}

	/**
	 * Remove a file
	 * 
	 * @param folderName
	 */
	@FtpRequestAnnotation(name = "DELE", connected = true, anonymous = false)
	private void requestRemoveFile(final String... dirs) {
		String path = arrayToPath(dirs);
		String folderPath = "";
		if (path.length() > 0 && path.charAt(0) == '/') {
			folderPath = path;
		} else {
			folderPath = ftpClient.getCurrentDir() + "/" + path;
		}
		boolean success = (new File(folderPath)).delete();
		if (success) {
			ftpClient.writeMessage("226 File removed: " + path);
		} else {
			ftpClient.writeMessage("451 File not removed: " + path);
		}
	}

	/**
	 * Rename a folder
	 * 
	 * @param folderName
	 */
	@FtpRequestAnnotation(name = "RNFR", connected = true, anonymous = false)
	private void requestFileToRename(final String... dirs) {
		String path = arrayToPath(dirs);
		String folderPath = "";
		if (path.length() > 0 && path.charAt(0) == '/') {
			folderPath = path;
		} else {
			folderPath = ftpClient.getCurrentDir() + "/" + path;
		}
		ftpClient.setFileToRename(folderPath);
		ftpClient.writeMessage("350 File to rename: " + path);
	}

	/**
	 * Rename a folder
	 * 
	 * @param folderName
	 */
	@FtpRequestAnnotation(name = "RNTO", connected = true)
	private void requestRename(final String... dirs) {
		String path = arrayToPath(dirs);
		String folderPath = "";
		if (path.length() > 0 && path.charAt(0) == '/') {
			folderPath = path;
		} else {
			folderPath = ftpClient.getCurrentDir() + "/" + path;
		}
		boolean success = (new File(ftpClient.getFileToRename()))
				.renameTo((new File(folderPath)));
		if (success) {
			ftpClient.writeMessage("250 Folder renamed: " + path);
		} else {
			ftpClient.writeMessage("451 Folder not renamed: " + path);
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
