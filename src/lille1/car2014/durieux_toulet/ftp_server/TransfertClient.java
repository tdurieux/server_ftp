package lille1.car2014.durieux_toulet.ftp_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import lille1.car2014.durieux_toulet.logs.LoggerUtilities;

public class TransfertClient {
	private final Socket tranfsertSocket;

	public TransfertClient(final Socket tranfsertSocket) {
		this.tranfsertSocket = tranfsertSocket;
	}

	public void writeMessage(final byte[] bytes) {
		try {
			final BufferedOutputStream bo = new BufferedOutputStream(
					this.tranfsertSocket.getOutputStream());
			bo.write(bytes);
			bo.flush();
			this.close();
		} catch (final IOException e) {
			LoggerUtilities.error(e);
		}
	}

	public byte[] readMessage() {
		final ArrayList<Integer> bytes = new ArrayList<>();
		try {
			final BufferedInputStream bi = new BufferedInputStream(
					this.tranfsertSocket.getInputStream());
			int b;
			while ((b = bi.read()) != -1) {
				bytes.add(b);
			}
			final byte[] bytesB = new byte[bytes.size()];
			for (int i = 0; i < bytes.size(); i++) {
				final int a = bytes.get(i);
				bytesB[i] = (byte) a;
			}
			System.out.println(new String(bytesB));
			return bytesB;
		} catch (final IOException e) {
			LoggerUtilities.error(e);
		}
		return null;
	}

	public void close() {
		try {
			this.tranfsertSocket.close();
		} catch (final IOException e) {
			LoggerUtilities.error(e);
		}
	}

}
