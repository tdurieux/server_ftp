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

import lille1.car2014.durieux_toulet.config.FTPConfiguration;
import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * Handle requests of FTP client
 * 
 * @author Durieux Thomas
 * @author Toulet Cyrille
 */
public class FTPRequestHandlerImpl implements FTPRequestHandler {

	/* Parameters */
	private String command;
	private String[] args;
	private FTPClient ftpClient;
	private FTPClientSocket clientSocket;

	/**
	 * Constructor
	 * 
	 * @param command
	 *            FTP command
	 * @param args
	 *            Arguments
	 * @param ftpClient
	 *            The FTP client
	 * @param clientSocket
	 *            The client socket
	 */
	public FTPRequestHandlerImpl(String command, String[] args,
			FTPClient ftpClient, FTPClientSocket clientSocket) {
		this.command = command;
		this.args = args;
		this.ftpClient = ftpClient;
		this.clientSocket = clientSocket;
	}

	/**
	 * Parse request
	 * 
	 * @param request
	 *            The request string
	 * @param ftpClient
	 *            The FTP client
	 * @param clientSocket
	 *            The FTP client socket
	 * @return FTPRequestHandler The FTP request handler
	 * @throws RequestHandlerException
	 *             when unable to parse request
	 */
	public static FTPRequestHandler parseStringRequest(final String request,
			final FTPClient ftpClient, FTPClientSocket clientSocket)
			throws RequestHandlerException {
		final String[] requestSplitted = request.split(" ");
		final String command = requestSplitted[0];
		if (requestSplitted.length > 1)
			return new FTPRequestHandlerImpl(command, Arrays.copyOfRange(
					requestSplitted, 1, requestSplitted.length), ftpClient,
					clientSocket);

		return new FTPRequestHandlerImpl(command, new String[0], ftpClient,
				clientSocket);
	}

	/**
	 * @see FTPRequestHandler
	 */
	@Override
	public void execute() throws RequestHandlerException {
		// list all methods of this class
		final Method[] allMethods = this.getClass().getDeclaredMethods();
		for (int i = 0; i < allMethods.length; i++) {
			final Method method = allMethods[i];
			final FTPRequestAnnotation annotation = method
					.getAnnotation(FTPRequestAnnotation.class);
			// verify that the annotation have on annotation
			if (annotation == null) {
				continue;
			}
			if (annotation.name().toLowerCase()
					.compareTo(command.toLowerCase()) != 0) {
				continue;
			}
			// Check the signature of the methods
			final Class<?>[] types = method.getParameterTypes();
			if (types.length > args.length) {
				continue;
			}
			// create the call parameters
			final Object[] objectArray = new Object[types.length];
			for (int j = 0; j < types.length; j++) {
				final String type = types[j].getName();
				if (type.compareTo(args[j].getClass().getName()) == 0) {
					objectArray[j] = args[j];
				} else if (type.compareTo("[Ljava.lang.String;") == 0) {
					String[] stringArgs = new String[args.length - j];
					for (int k = j, l = 0; k < args.length; k++, l++) {
						stringArgs[l] = args[k];
					}
					objectArray[j] = stringArgs;
					break;
				} else {
					throw new RequestHandlerException("Arguement not valid "
							+ args[j]);
				}
			}

			// if the user must be connected before doing an command
			if ((annotation.connected() && !ftpClient.isConnected())) {
				if (!FTPConfiguration.INSTANCE
						.getBooleanProperty("allowAnonymous")
						|| !annotation.anonymous()
						|| !ftpClient.getUsername().equals("anonymous")) {
					clientSocket.writeMessage("530 Not logged in.");
					return;
				}
			}
			// invoke the methos
			try {
				method.invoke(this, objectArray);
				return;
			} catch (final IllegalAccessException e) {
				throw new RequestHandlerException("Command not found "
						+ command, e);
			} catch (final IllegalArgumentException e) {
				throw new RequestHandlerException("Arguement not valid ", e);
			} catch (final InvocationTargetException e) {
				throw new RequestHandlerException("Error execution " + command
						+ " error: " + e.getMessage(), e);
			}
		}
		throw new RequestHandlerException("Command not found " + command);
	}

	/**
	 * Define the primary encoding of the communication
	 * 
	 * @param typeCharacter
	 *            The type caracter (A|E|I|L)
	 * @return True if able to set the primary caracter, false else
	 */
	private boolean setPrimaryTypeCaracter(final String typeCharacter) {
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
			return false;
		}

		return true;
	}

	/**
	 * Request encoding
	 * 
	 * @param typeCharacter
	 *            The encoding char
	 * @param secondTypeCharacter
	 *            The network type char
	 */
	@FTPRequestAnnotation(name = "TYPE", connected = true, anonymous = true)
	private void requestType(final String typeCharacter,
			final String secondTypeCharacter) {
		if (!this.setPrimaryTypeCaracter(typeCharacter)) {
			clientSocket.writeMessage("400 Type not accepted");
			return;
		}

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
			clientSocket.writeMessage("400 Type not accepted");
			return;
		}
		clientSocket.writeMessage("200 Type accepted");
	}

	/**
	 * Define the primary encoding of the communication and send response to the
	 * client
	 * 
	 * @param typeCharacter
	 *            The encoding caracter
	 */
	@FTPRequestAnnotation(name = "TYPE", connected = true, anonymous = true)
	private void requestType(final String typeCharacter) {
		if (this.setPrimaryTypeCaracter(typeCharacter))
			clientSocket.writeMessage("200 Type accepted");
		else
			clientSocket.writeMessage("400 Type not accepted");
	}

	/**
	 * Login the user
	 * 
	 * @param password
	 *            The password of the user
	 * @throws RequestHandlerException
	 *             when unable to connect user
	 */
	@FTPRequestAnnotation(name = "PASS", connected = false)
	private void requestConnect(final String password)
			throws RequestHandlerException {
		// connect the user
		if (ftpClient.connect(password)) {
			clientSocket.writeMessage("230 Connected");
		} else {
			clientSocket.writeMessage("430 Invalid username/password");
		}
	}

	/**
	 * Define the username of the client
	 * 
	 * @param username
	 *            The username of the client
	 */
	@FTPRequestAnnotation(name = "USER", connected = false)
	private void requestUser(final String username) {
		ftpClient.setUsername(username);
		if (FTPConfiguration.INSTANCE.getBooleanProperty("allowAnonymous")
				&& username.compareTo("anonymous") == 0) {
			clientSocket.writeMessage("230 anonymous");
		} else {
			clientSocket.writeMessage("331 User accepted");
		}

	}

	/**
	 * Close the connection
	 */
	@FTPRequestAnnotation(name = "QUIT", connected = false)
	private void closeConnection() {
		// Print close message
		clientSocket.writeMessage("426 Close connection");
		try {
			clientSocket.close();
		} catch (SocketException e) {
			// unable to client client connection
			LoggerUtilities.error("Unable to close client connection", e);
		}
	}

	/**
	 * Request the os type
	 */
	@FTPRequestAnnotation(name = "SYST", connected = true, anonymous = true)
	private void requestSYST() {
		// final String OS = System.getProperty("os.name").toLowerCase();
		// this.ftpClient.writeMessage("215 " + OS);
		clientSocket.writeMessage("215 UNIX Type: L8");
	}

	/**
	 * Save an option
	 * 
	 * @param key
	 *            The name of the option
	 * @param value
	 *            The value of the option
	 */
	@FTPRequestAnnotation(name = "OPTS", connected = true, anonymous = true)
	private void requestOptions(final String key, final String value) {
		ftpClient.getOptions().put(key, value);
		clientSocket.writeMessage("200 Accept option");
	}

	/**
	 * Get the current directory
	 */
	@FTPRequestAnnotation(name = "PWD", connected = true, anonymous = true)
	private void requestCurrentDirectory() {
		clientSocket.writeMessage("257 " + '"' + ftpClient.getCurrentDir()
				+ '"');
	}

	/**
	 * Path builder
	 * 
	 * @param An
	 *            Array of a splited path
	 * @return An absolute path
	 */
	private String arrayToPath(String[] dirs) {
		String path = "";
		for (int i = 0; i < dirs.length; i++) {
			String dir = dirs[i];
			path += dir + (i != dirs.length - 1 ? " " : "");
		}
		if (path.length() > 0 && path.charAt(0) == '/') {
			return path;
		}
		return ftpClient.getCurrentDir() + "/" + path;
	}

	/**
	 * Change the current directory to dir
	 * 
	 * @param dir
	 *            The new current direcotry
	 */
	@FTPRequestAnnotation(name = "CWD", connected = true, anonymous = true)
	private void requestSetCurrentDirecory(String... dirs) {
		String path = arrayToPath(dirs);
		File f = new File(path);
		if (!f.exists() || !f.isDirectory()) {
			clientSocket
					.writeMessage("550 Can't change directory: No such file or directory");
		} else {
			ftpClient.setCurrentDir(f.getAbsolutePath());
			clientSocket.writeMessage("250 OK. Current directory is "
					+ f.getAbsolutePath());
		}
	}

	/**
	 * Change the current directory to parent directory
	 * 
	 * @param dir
	 *            The new current direcotry
	 */
	@FTPRequestAnnotation(name = "CDUP", connected = true, anonymous = true)
	private void requestUpCurrentDirecory() {
		File f = new File(ftpClient.getCurrentDir() + "/..");
		if (!f.exists() || !f.isDirectory()) {
			clientSocket
					.writeMessage("550 Can't change directory to test: No such file or directory");
		}
		ftpClient.setCurrentDir(f.getAbsolutePath());
		clientSocket.writeMessage("250 OK. Current directory is "
				+ f.getAbsolutePath());
	}

	/**
	 * Send the port of the data connection (active mode)
	 * 
	 * @param adressString
	 *            IP address
	 */
	@FTPRequestAnnotation(name = "PORT", connected = true, anonymous = true)
	private void requestPort(String addressStirng) {
		try {
			String[] addressArray = addressStirng.split(",");
			if (addressArray.length != 6) {
				clientSocket.writeMessage("425 Can't open data connection.");
				return;
			}
			String address = addressArray[0] + "." + addressArray[1] + "."
					+ addressArray[2] + "." + addressArray[3];
			final int port = Integer.parseInt(addressArray[4]) * 256
					+ Integer.parseInt(addressArray[5]);
			ftpClient.createNewTransfert(address, port);
			clientSocket.writeMessage("227 Entering Active Mode");
		} catch (final SocketException e) {
			LoggerUtilities.error(e);
			clientSocket.writeMessage("425 Can't open data connection.");
		}
	}

	/**
	 * Send the port of the data connection (passive mode)
	 */
	@FTPRequestAnnotation(name = "PASV", connected = true, anonymous = true)
	private void requestPassiveMode() {
		try {
			final int port = ftpClient.createNewTransfert();
			final int p1 = (port / 256);
			final int p2 = port - p1 * 256;
			clientSocket.writeMessage("227 Entering Passive Mode (127,0,0,1,"
					+ p1 + "," + p2 + ")");
		} catch (final SocketException e) {
			LoggerUtilities.error(e);
			clientSocket.writeMessage("425 Can't open data connection.");
		}
	}

	/**
	 * Send the port of the data connection (passive mode)
	 */
	@FTPRequestAnnotation(name = "ABOR", connected = true, anonymous = true)
	private void requestABOR() {
		try {
			ftpClient.getTransfertServer().close();
			clientSocket.writeMessage("226 ABORTED.");
		} catch (SocketException e) {
			clientSocket.writeMessage("451 Cannot ABOR.");
		}
	}

	/**
	 * Get the port of the data connection (extended passive mode)
	 */
	@FTPRequestAnnotation(name = "EPSV", connected = true, anonymous = true)
	private void requestExtendedPassiveMode() {
		try {
			final int port = ftpClient.createNewTransfert();
			clientSocket.writeMessage("229 Entering Extended Passive Mode (|||"
					+ port + "|)");
		} catch (final SocketException e) {
			LoggerUtilities.error(e);
			clientSocket.writeMessage("425 Can't open data connection.");
		}
	}

	/**
	 * List all files of the directory dir
	 * 
	 * @param dirs
	 *            The directory to list
	 */
	@FTPRequestAnnotation(name = "List", connected = true, anonymous = true)
	private void requestListFiles(String... dirs) {
		if (dirs[0].compareTo("-a") == 0) {
			dirs[0] = "";
		}
		String path = arrayToPath(dirs);
		
		if (ftpClient.getTransfertServer() == null) {
			clientSocket.writeMessage("443 No data connection");
		} else {
			clientSocket.writeMessage("150 Accepted data connection");
			try {
				ftpClient.getTransfertServer().writeContent(
						this.createList(path));
				clientSocket.writeMessage("226 List transefered");
			} catch (SocketException e) {
				LoggerUtilities.error(e);
				clientSocket.writeMessage("426 Unable to send file list");
			}
			try {
				ftpClient.getTransfertServer().close();
			} catch (SocketException e1) {
				LoggerUtilities.error(e1);
				clientSocket
						.writeMessage("451 Unable to close the transfert Server");
			}
		}
	}

	/**
	 * List all file of the current directory
	 */
	@FTPRequestAnnotation(name = "List", connected = true, anonymous = true)
	private void requestListFiles() {
		this.requestListFiles("");
	}

	/**
	 * List all file name of the directory dir
	 * 
	 * @param dirs
	 *            The directory to list
	 */
	@FTPRequestAnnotation(name = "NLST", connected = true, anonymous = true)
	private void requestListFileName(final String... dirs) {
		String path = arrayToPath(dirs);
		if (ftpClient.getTransfertServer() == null) {
			clientSocket.writeMessage("443 No data connection");
		} else {
			try {
				final File folder = new File(path);
				if (!folder.exists() || !folder.isDirectory()) {
					clientSocket.writeMessage("504 Only accept folder");
				} else {
					clientSocket.writeMessage("150 Accepted data connection");
					final String[] files = folder.list();
					String listFilename = "";
					for (int i = 0; i < files.length; i++) {
						listFilename += files[i]
								+ ((i != files.length - 1) ? "\n" : "");

					}
					ftpClient.getTransfertServer().writeContent(listFilename);
					clientSocket.writeMessage("226 " + files.length
							+ " matches total");
				}
			} catch (SocketException e) {
				LoggerUtilities.error(e);
				clientSocket.writeMessage("426 Unable to send file list");
			}
			try {
				ftpClient.getTransfertServer().close();
			} catch (SocketException e1) {
				LoggerUtilities.error(e1);
				clientSocket
						.writeMessage("451 Unable to close the transfert Server");
			}
		}
	}

	/**
	 * List all file name of the current directory
	 */
	@FTPRequestAnnotation(name = "NLST", connected = true, anonymous = true)
	private void requestListFileName() {
		this.requestListFileName("");
	}

	/**
	 * Get all file size of the directory dir
	 * 
	 * @param dirs
	 *            The directory to list
	 */
	@FTPRequestAnnotation(name = "SIZE", connected = true, anonymous = true)
	private void requestGetFileSize(final String... dirs) {
		String path = arrayToPath(dirs);
		final File f = new File(path);
		clientSocket.writeMessage("226 " + f.length());
	}

	/**
	 * Send the content of a file to the client
	 * 
	 * @param file
	 *            The file to send
	 */
	@FTPRequestAnnotation(name = "RETR", connected = true, anonymous = true)
	private void requestDownloadFile(final String... dirs) {
		String path = arrayToPath(dirs);
		if (ftpClient.getTransfertServer() == null) {
			clientSocket.writeMessage("443 No data connection");
		} else {
			clientSocket.writeMessage("150 Accepted data connection");
			try {
				FileInputStream fis = new FileInputStream(path);
				ftpClient.getTransfertServer().writeContent(fis);
				clientSocket.writeMessage("226 File successfully transferred");
			} catch (final IOException e) {
				LoggerUtilities.error(e);
				clientSocket.writeMessage("426 Unable to send file");
			} catch (SocketException e) {
				LoggerUtilities.error(e);
				clientSocket.writeMessage("426 Unable to send file");
			}
			try {
				ftpClient.getTransfertServer().close();
			} catch (SocketException e1) {
				LoggerUtilities.error(e1);
				clientSocket
						.writeMessage("451 Unable to close the transfert Server");
			}

		}
	}

	/**
	 * Send the content of a file to the server
	 * 
	 * @param file
	 *            The name of file to store
	 */
	@FTPRequestAnnotation(name = "STOR", connected = true, anonymous = false)
	private void requestUploadFile(final String[] dirs) {
		String path = arrayToPath(dirs);
		if (ftpClient.getTransfertServer() == null) {
			clientSocket.writeMessage("443 No data connection");
		} else {
			clientSocket.writeMessage("150 Accepted data connection");
			try {
				FileOutputStream out = new FileOutputStream(path);
				ftpClient.getTransfertServer().readContent(out);
				out.close();
				clientSocket.writeMessage("226 File successfully transferred");
			} catch (final IOException e) {
				LoggerUtilities.error(e);
			} catch (SocketException e) {
				clientSocket.writeMessage("426 Unable to transfer file");
			}
			try {
				ftpClient.getTransfertServer().close();
			} catch (SocketException e1) {
				LoggerUtilities.error(e1);
				clientSocket
						.writeMessage("451 Unable to close the transfert Server");
			}
		}
	}

	/**
	 * Get last modification date
	 * 
	 * @param file
	 *            The name of the file
	 */
	@FTPRequestAnnotation(name = "MDTM", connected = true, anonymous = true)
	private void requestGetLastModificationDate(final String... dirs) {
		String path = arrayToPath(dirs);
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm",
					Locale.ENGLISH);
			Date lastModificationDate = new Date(Files.getLastModifiedTime(
					Paths.get(path)).toMillis());
			clientSocket
					.writeMessage("226 " + sdf.format(lastModificationDate));
		} catch (IOException e) {
			clientSocket
					.writeMessage("451 Unable to access to last modification date.");
		}
	}

	/**
	 * Create a folder
	 * 
	 * @param folderName
	 *            The folder name
	 */
	@FTPRequestAnnotation(name = "MKD", connected = true, anonymous = false)
	private void requestCreateFolder(final String... dirs) {
		String path = arrayToPath(dirs);
		boolean success = (new File(path)).mkdirs();
		if (success) {
			clientSocket.writeMessage("226 Folder created: " + path);
		} else {
			clientSocket.writeMessage("451 Folder not created: " + path);
		}
	}

	/**
	 * Remove a folder
	 * 
	 * @param folderName
	 *            The folder name
	 */
	@FTPRequestAnnotation(name = "RMD", connected = true, anonymous = false)
	private void requestRemoveFolder(final String... dirs) {
		String path = arrayToPath(dirs);
		boolean success = (new File(path)).delete();
		if (success) {
			clientSocket.writeMessage("226 Folder removed: " + path);
		} else {
			clientSocket.writeMessage("451 Folder not removed: " + path);
		}
	}

	/**
	 * Remove a file
	 * 
	 * @param folderName
	 *            The folder name
	 */
	@FTPRequestAnnotation(name = "DELE", connected = true, anonymous = false)
	private void requestRemoveFile(final String... dirs) {
		String path = arrayToPath(dirs);
		boolean success = (new File(path)).delete();
		if (success) {
			clientSocket.writeMessage("226 File removed: " + path);
		} else {
			clientSocket.writeMessage("451 File not removed: " + path);
		}
	}

	/**
	 * Rename a file
	 * 
	 * @param folderName
	 *            The old file name
	 */
	@FTPRequestAnnotation(name = "RNFR", connected = true, anonymous = false)
	private void requestFileToRename(final String... dirs) {
		String path = arrayToPath(dirs);
		ftpClient.setFileToRename(path);
		clientSocket.writeMessage("350 File to rename: " + path);
	}

	/**
	 * Rename a file
	 * 
	 * @param folderName
	 *            The new folder name
	 */
	@FTPRequestAnnotation(name = "RNTO", connected = true)
	private void requestRename(final String... dirs) {
		String path = arrayToPath(dirs);
		boolean success = (new File(ftpClient.getFileToRename()))
				.renameTo((new File(path)));
		if (success) {
			clientSocket.writeMessage("250 Folder renamed: " + path);
		} else {
			clientSocket.writeMessage("451 Folder not renamed: " + path);
		}
	}

	/**
	 * Create the list of all file of the direcotry dir
	 * 
	 * @param dir
	 *            The directory to list
	 * @return A string containing all file of the dir
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
			} catch (final IOException e2) {
			}

			result += permissionsString + " 1 " + owner + " " + group + " "
					+ file.length() + " " + sdf.format(file.lastModified())
					+ " " + file.getName() + "\r\n";
		}
		return result;
	}

}
