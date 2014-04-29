package app.caronacomunitaria.br.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TCPListener implements Runnable {

	private BufferedReader in;
	private boolean run = true;
	private String mensagem_servidor;
	private OnMessageReceivedListener mMessageListener;

	public TCPListener(TCPClient tcp) throws IOException {
		this.in = new BufferedReader(new InputStreamReader(tcp.getInputStream()));
	}

	@Override
	public void run() {
		while (run) {
			try {
				mensagem_servidor = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mensagem_servidor != "" && mMessageListener != null)
				mMessageListener.messageReceived(mensagem_servidor);
			else
				break;
			mensagem_servidor = "";
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
