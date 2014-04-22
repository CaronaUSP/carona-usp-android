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

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.EventListener;

public class TCPClient implements Runnable {

	private int porta;
	private byte[] ip_servidor;
	private int porta_local;
	private Socket socket;
	private OutputStreamWriter outw;
	private BufferedReader in;
	private OnMessageReceivedListener mMessageListener = null;
	private boolean run;
	private String mensagem_servidor;

	public TCPClient(int porta, byte[] ip_servidor, int porta_local) {
		this.porta = porta;
		this.ip_servidor = ip_servidor;
		this.porta_local = porta_local;
	}

	public void conectar() throws UnknownHostException, IOException {
		socket = new Socket(InetAddress.getByAddress(ip_servidor), porta, null,
				porta_local);
		outw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void enviarMensagem(String mensagem) throws IOException {
		if (outw != null) {
			outw.write(mensagem);
			outw.flush();
			outw.close();
		}
	}

	public void enviarMensagem(int mensagem) throws IOException {
		if (outw != null) {
			outw.write(mensagem);
			outw.flush();
			outw.close();
		}
	}

	public void desconectar() throws IOException {
		socket.close();
		run = false;
	}

	public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
		mMessageListener = listener;
	}

	public interface OnMessageReceivedListener extends EventListener {
		public void messageReceived(String message);
	}

	@Override
	public void run() {
		run = true;
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
}