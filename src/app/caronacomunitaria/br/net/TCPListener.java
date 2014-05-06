package app.caronacomunitaria.br.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public class TCPListener {

	private BufferedReader in;
	private boolean run = false;
	private String mensagem_servidor;
	private OnMessageReceivedListener mMessageListener;

	public TCPListener(TCPClient tcp) throws IOException {
		this.in = new BufferedReader(
				new InputStreamReader(tcp.getInputStream()));
	}

	public void run() {
		run = true;

		try {
			while (run) {
				mensagem_servidor = in.readLine();
				if (mensagem_servidor != null && mMessageListener != null) {
					mMessageListener.messageReceived(mensagem_servidor);
				}
				mensagem_servidor = null;

			}

		} catch (Exception e) {
			//TODO
			Log.e("ERRO:", e.getMessage());
		}
	}

	public void stop() {
		run = false;

	}

	public void setOnMessageReceivedListener(
			OnMessageReceivedListener onMessageReceivedListener) {
		this.mMessageListener = onMessageReceivedListener;
	}

}