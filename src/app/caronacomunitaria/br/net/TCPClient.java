package app.caronacomunitaria.br.net;

import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

/**
 * <p>
 * Carona Comunit�ria USP est� licenciado com uma Licen�a Creative Commons
 * Atribui��o-N�oComercial-CompartilhaIgual 4.0 Internacional (CC BY-NC-SA 4.0).
 * <p>
 * Carona Comunit�ria USP is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International License (CC BY-NC-SA
 * 4.0).
 * <p>
 * Classe que prov� fun��es para conex�o e comunica��o entre cliente e servidor
 * atrav�s do protocolo TCP
 * 
 ****************************************************************************** 
 **/

public class TCPClient {
	/**
	 * 
	 */
	/**
	 * Porta do servidor que ser� usada para a conex�o
	 * */

	private int porta;
	/**
	 * Endere�o ip do servidor
	 * */
	private String host;
	/**
	 * Socket usado para a transmiss�o de dados
	 * */
	private Socket socket;

	private OnMessageReceivedListener mMessageListener;

	private boolean run = false;

	private InputStreamReader in;

	private PrintWriter out;

	/**
	 * Construtor com porta, ip
	 * 
	 * @param porta
	 *            porta para conex�o
	 * @param host
	 *            host para conex�o
	 */
	public TCPClient(int porta, String host) {
		this.porta = porta;
		this.host = host;
	}

	/**
	 * Cria um novo Socket e um novo OutputStreamWriter
	 * 
	 * @throws UnknownHostException
	 * 
	 * @throws IOException
	 * 
	 */

	public void conectar() throws UnknownHostException, IOException {
		InetAddress serverAddr = InetAddress.getByName(this.host);
		socket = new Socket(serverAddr, porta);
		out = new PrintWriter(socket.getOutputStream(), true);
		this.in = new InputStreamReader(socket.getInputStream(), "UTF-8");
	}

	/**
	 * Envia a String <i>mensagem</i> para o servidor
	 * 
	 * @throws IOException
	 */

	public void enviarMensagem(String mensagem) throws IOException {

		if (out != null) {
			out.write(mensagem);
			out.write("\0");
			out.flush();
		}
	}

	public boolean isConnected(){		
		return socket==null ? false : socket.isConnected();
	}

	public void run() {
		StringBuilder mensagem_servidor = new StringBuilder();
		run = true;
		int m;
		try {
			while (run) {
				m = 0;
				while ((m = in.read()) != 0) {
					mensagem_servidor.append((char) m);
				}
				mMessageListener.messageReceived(mensagem_servidor.toString());
				mensagem_servidor = new StringBuilder();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}			
	}			


	/**
	 * Fecha o socket.
	 * 
	 * @throws IOException
	 */

	public void desconectar() {
		run = false;
		Log.i("Desconectado", String.valueOf(run));
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {

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