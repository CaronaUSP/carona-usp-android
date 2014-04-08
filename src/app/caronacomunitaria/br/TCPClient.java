/*******************************************************************************
 * Carona Comunit�ria USP est� licenciado com uma Licen�a Creative Commons
 * Atribui��o-N�oComercial-CompartilhaIgual 4.0 Internacional (CC BY-NC-SA 4.0).
 *
 * Carona Comunit�ria USP is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International License (CC BY-NC-SA 4.0).
 *
 * Fun��es para conex�o e comunica��o entre cliente e servidor atrav�s do protocolo TCP
 *******************************************************************************/

package app.caronacomunitaria.br;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.EventListener;

public class TCPClient implements Runnable {
	
	private int porta;
	private String ip_servidor = "";
	private int porta_local;
	private Socket socket;
	private OutputStream out;
	private InputStream in;
	private OnMessageReceivedListener mMessageListener = null;
	private boolean run;
	private int mensagem_servidor;

	public TCPClient(int porta, String ip_servidor, int porta_local) {
		this.porta = porta;
		this.ip_servidor = ip_servidor;
		this.porta_local = porta_local;
	}

	public void conectar() throws UnknownHostException, IOException {
		socket = new Socket(InetAddress.getByName(ip_servidor), porta, null,
				porta_local);
		out = socket.getOutputStream();
	}

	public void enviarMensagem(byte[] mensagem) throws IOException {
		if (out != null) {
			out.write(mensagem);
			out.flush();
			out.close();
		}
	}
	
	public void enviarMensagem(int mensagem) throws IOException {
		if (out != null) {
			out.write(mensagem);
			out.flush();
			out.close();
		}
	}

	public void desconectar() throws IOException {
		socket.close();
		run = false;
	}

	static byte[] intToByteArray(int i) {
		return new byte[] { (byte) i, (byte) (i >> 8), (byte) (i >> 16),
				(byte) (i >> 24) };
	}

	

	public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
		mMessageListener = listener;
	}

	public interface OnMessageReceivedListener extends EventListener {
		public void messageReceived(byte... message);
	}

	@Override
	public void run() {
		run = true;
		while (run) {
			try {
				mensagem_servidor = in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (mensagem_servidor != -1 && mMessageListener != null)
				mMessageListener
						.messageReceived(intToByteArray(mensagem_servidor));
			else
				break;
			mensagem_servidor = -1;
		}
	}
}