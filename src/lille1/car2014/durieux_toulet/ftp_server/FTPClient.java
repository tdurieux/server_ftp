package lille1.car2014.durieux_toulet.ftp_server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

/**
 * All commands of the client is interpreted by the class
 * 
 * @author durieux
 */
public class FTPClient {
	private final Socket clientSocket;

	public FTPClient(Socket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			// InputStreamReader reader = new
			// InputStreamReader(clientSocket.getInputStream(),
			// Charset.forName("UTF-8"));
			OutputStreamWriter writer = new OutputStreamWriter(
					clientSocket.getOutputStream());
			writer.write("Hello\n");
			writer.flush();

			InputStreamReader reader = new InputStreamReader(
					clientSocket.getInputStream(), Charset.forName("UTF-8"));
			int c = reader.read();
			// en UTF-8 le premier octet indique le codage
			while (c != -1) {
				System.out.print((char) c);
				c = reader.read();
			}
		} catch (IOException e) {
			LoggerUtilities.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
