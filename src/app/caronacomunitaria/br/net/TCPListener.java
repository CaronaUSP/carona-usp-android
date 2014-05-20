package app.caronacomunitaria.br.net;

import java.io.IOException;
import java.io.InputStream;
import android.util.Log;

public class TCPListener {

	private InputStream is;
	private boolean run = false;
	private StringBuilder mensagem_servidor = new StringBuilder();
	private OnMessageReceivedListener mMessageListener;

	public TCPListener(TCPClient tcp) throws IOException {
		this.is = tcp.getInputStream();
	}

	public void run() {
		run = true;

		try {
			while (run) {
				int m = 0;
				while ((m = is.read()) != 0) {
					mensagem_servidor.append((char) m);
				}
				mMessageListener.messageReceived(mensagem_servidor.toString());
			}

		} catch (Exception e) {
			// TODO
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