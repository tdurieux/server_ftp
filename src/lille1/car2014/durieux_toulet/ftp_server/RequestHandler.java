package lille1.car2014.durieux_toulet.ftp_server;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

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
	private FTPClientImpl ftpClient;

	public RequestHandler(FTPClientImpl ftpClient) {
		this.ftpClient = ftpClient;
	}

	public void parseStringRequest(String request)
			throws RequestHandlerException {
		String[] requestSplitted = request.split(" ");
		String command = requestSplitted[0];
		executeRquest(command, requestSplitted);
	}

	private void executeRquest(String command, String[] parameters)
			throws RequestHandlerException {
		Method[] methods1 = this.getClass().getDeclaredMethods();
		for (int i = 0; i < methods1.length; i++) {
			Method method = methods1[i];
			FtpRequestAnnotation annotation = method
					.getAnnotation(FtpRequestAnnotation.class);
			if (annotation != null) {
				// check if the method must be invoked
				if (annotation.name().toLowerCase()
						.compareTo(command.toLowerCase()) != 0) {
					continue;
				}
				Class<?>[] types = method.getParameterTypes();
				if (types.length > parameters.length - 1) {
					continue;
				}
				Object[] args = new Object[types.length];
				for (int j = 0; j < types.length; j++) {
					String type = types[j].getName();
					if (type.compareTo(parameters[j + 1].getClass().getName()) == 0)
						args[j] = parameters[j + 1];
					else
						throw new RequestHandlerException(
								"Arguement not valid " + parameters[j + 1]);
				}
				if (annotation.connected() != ftpClient.isConnected()) {
					ftpClient.writeMessage("530 Not logged in.");
					return;
				}
				try {
					method.invoke(this, args);
					return;
				} catch (IllegalAccessException e) {
					throw new RequestHandlerException("Command not found "
							+ command, e);
				} catch (IllegalArgumentException e) {
					throw new RequestHandlerException("Arguement not valid ", e);
				} catch (InvocationTargetException e) {
					throw new RequestHandlerException("Error execution "
							+ command + " error: " + e.getMessage(), e);
				}
			}
		}
		throw new RequestHandlerException("Command not found " + command);
	}

	@FtpRequestAnnotation(name = "TYPE", connected = true)
	private void requestType(String typeCharacter) {
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
		ftpClient.writeMessage("200");
	}

	@FtpRequestAnnotation(name = "TYPE", connected = true)
	private void requestType(String typeCharacter, String secondTypeCharacter) {
		requestType(typeCharacter);

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
	}

	@FtpRequestAnnotation(name = "PASS", connected = false)
	private void requestConnect(String password) {
		if (ftpClient.connect(password)) {
			ftpClient.writeMessage("230 Connected");
		} else {
			ftpClient.writeMessage("430 Invalid username/password");
		}
	}

	@FtpRequestAnnotation(name = "USER", connected = false)
	private void requestUser(String username) {
		ftpClient.setUsername(username);
		ftpClient.writeMessage("331 User accepted");
	}

	@FtpRequestAnnotation(name = "QUIT", connected = false)
	private void closeConnection() {
		ftpClient.close();
	}

	@FtpRequestAnnotation(name = "SYST", connected = true)
	private void requestSYST() {
		ftpClient.writeMessage("UNIX Type: L8");
	}

	@FtpRequestAnnotation(name = "OPTS", connected = true)
	private void requestOptions(String key, String value) {
		ftpClient.getOptions().put(key, value);
		ftpClient.writeMessage("200 Accept option");
	}

	@FtpRequestAnnotation(name = "FEAT", connected = true)
	private void requestFeat() {
		ftpClient.writeMessage("211");
	}

	@FtpRequestAnnotation(name = "PWD", connected = true)
	private void requestCurrentDirecory() {
		ftpClient.writeMessage("257 " + '"' + ftpClient.getCurrentDir() + '"');
	}

	@FtpRequestAnnotation(name = "CWD", connected = true)
	private void requestSetCurrentDirecory(String dir) {
		File f = new File(dir);
		if (!f.exists()) {
			ftpClient
					.writeMessage("550 Can't change directory to test: No such file or directory");
		} else {
			ftpClient.setCurrentDir(dir);
			ftpClient.writeMessage("250 OK. Current directory is " + dir);
		}
	}

	@FtpRequestAnnotation(name = "PASV", connected = true)
	private void requestPassiveMode() {
		try {
			int port = ftpClient.createNewTransfert();
			int p1 = (port / 256);
			int p2 = port - p1 * 256;
			ftpClient.writeMessage("227 Entering Passive Mode (127,0,0,1," + p1
					+ "," + p2 + ")");
		} catch (SocketException e) {
			LoggerUtilities.error(e);
			ftpClient.writeMessage("425 Can't open data connection.");
		}
	}

	@FtpRequestAnnotation(name = "EPSV", connected = true)
	private void requestExtendedPassiveMode() {
		try {
			int port = ftpClient.createNewTransfert();
			ftpClient.writeMessage("229 Entering Extended Passive Mode (|||"
					+ port + "|)");
		} catch (SocketException e) {
			LoggerUtilities.error(e);
			ftpClient.writeMessage("425 Can't open data connection.");
		}
	}

	@FtpRequestAnnotation(name = "List", connected = true)
	private void requestListFiles(String parameter) {
		requestListFiles();
	}
	
	@FtpRequestAnnotation(name = "NLST", connected = true)
	private void requestListFileName(String parameter) {
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				File folder = new File(parameter);
				if (!folder.exists() || !folder.isDirectory()) {
					
				} else {
					String[] files = folder.list();
					String listFilename = "";
					for (int i = 0; i < files.length; i++) {
						listFilename+=files[i]+((i!=files.length-1)?"\n":"");
						
					}
					ftpClient.getTransfertServer().writeContent(listFilename);
					ftpClient.writeMessage("226 "+files.length+" matches total");
				}
			} catch (RequestHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ftpClient.getTransfertServer().close();
			}
		}
	}
	
	@FtpRequestAnnotation(name = "NLST", connected = true)
	private void requestListFileName() {
		requestListFileName(ftpClient.getCurrentDir());
	}

	@FtpRequestAnnotation(name = "List", connected = true)
	private void requestListFiles() {
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				ftpClient.getTransfertServer().writeContent(
						createList(ftpClient.getCurrentDir()));
				ftpClient.writeMessage("226 8 matches total");
			} catch (RequestHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ftpClient.getTransfertServer().close();
			}
		}
	}
	
	@FtpRequestAnnotation(name = "RETR", connected = true)
	private void requestDownloadFile(String file) {
		if (ftpClient.getTransfertServer() == null) {
			ftpClient.writeMessage("443 No data connection");
		} else {
			ftpClient.writeMessage("150 Accepted data connection");
			try {
				try {
					byte[] encoded = Files.readAllBytes(Paths.get(ftpClient.getCurrentDir()+"/"+file));
					ftpClient.getTransfertServer().writeContent(encoded);
					ftpClient.writeMessage("226 File successfully transferred");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (RequestHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ftpClient.getTransfertServer().close();
			}
		}
	}

	private String createList(String dir) {
		String result = "";
		File folder = new File(dir);
		if (!folder.exists() || !folder.isDirectory()) {
			return "not good";
		}
		File[] files = folder.listFiles();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			
			result += (file.isDirectory() ? "d" : "-") + (file.canRead() ? 'r' : '-')
					+ (file.canWrite() ? 'w' : '-')
					+ (file.canExecute() ? 'x' : '-')
					+ "r--r--  1   owner   group "+file.length()+" "+sdf.format(file.lastModified())+" "
					+ file.getName() + "\n";
		}
		return result;
	}

}
