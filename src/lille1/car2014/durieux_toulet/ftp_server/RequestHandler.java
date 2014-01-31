package lille1.car2014.durieux_toulet.ftp_server;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * Handle requests of FTPClient
 * 
 * @author Durieux Thomas
 * 
 */
public class RequestHandler {
	private FTPClient ftpClient;

	public RequestHandler(FTPClient ftpClient) {
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
				if(annotation.connected() != ftpClient.isConnected()) {
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
					throw new RequestHandlerException("Command not found "
							+ command, e);
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
			ftpClient.setTypeCharactor("ASCII");
			break;
		case "T":
			ftpClient.setTypeCharactor("EBCDIC");
			break;
		case "C":
			ftpClient.setTypeCharactor("image");
			break;
		default:

			break;
		}
	}

	@FtpRequestAnnotation(name = "PASS", connected = false)
	private void requestConnect(String password) {
		if(ftpClient.connect(password)){
			ftpClient.writeMessage("230");
		} else {
			ftpClient.writeMessage("430");
		}
	}

	@FtpRequestAnnotation(name = "USER", connected = false)
	private void requestUser(String username) {
		ftpClient.setUsername(username);
		ftpClient.writeMessage("331");
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
		ftpClient.writeMessage("200");
	}

	@FtpRequestAnnotation(name = "FEAT", connected = true)
	private void requestFeat() {
		ftpClient
				.writeMessage("211-Extensions supported\nSIZE\nMDTM\nMLST size*;type*;perm*;create*;modify*;\nLANG EN*\nTVFS\nUTF8\n211 end  ");
	}

	@FtpRequestAnnotation(name = "PWD", connected = true)
	private void requestPwd() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		ftpClient.writeMessage("257 " + '"'+s+'"');
	}

}
