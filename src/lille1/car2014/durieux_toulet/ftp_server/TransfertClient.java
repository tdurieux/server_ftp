package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

public class TransfertClient {
	private Socket tranfsertSocket;
	private BufferedReader in;

	public TransfertClient(Socket tranfsertSocket) {
		this.tranfsertSocket = tranfsertSocket;
	}

	public void writeMessage(byte[] bytes) {
		try {
			BufferedOutputStream bo = new BufferedOutputStream(
					tranfsertSocket.getOutputStream());
			bo.write(bytes);
			bo.flush();
			close();
		} catch (IOException e) {
			LoggerUtilities.error(e);
		}
	}

	public byte[] readMessage() {
		ArrayList<Integer> bytes = new ArrayList<>();
		try {
			BufferedInputStream bi = new BufferedInputStream(
					tranfsertSocket.getInputStream());
			int b;
			while ((b = bi.read())!=-1) {
				bytes.add(b);
			}
			byte[] bytesB = new byte[bytes.size()];
			for (int i = 0; i < bytes.size(); i++) {
				int a = bytes.get(i);
				bytesB[i] = (byte) a;
			}
			System.out.println(new String(bytesB));
			return bytesB;
		} catch (IOException e) {
			LoggerUtilities.error(e);
		}
		return null;
	}

	public void close() {
		try {
			this.tranfsertSocket.close();
		} catch (IOException e) {
			LoggerUtilities.error(e);
		}
	}

}
