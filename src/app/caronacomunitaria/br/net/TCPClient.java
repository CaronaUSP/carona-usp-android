package app.caronacomunitaria.br.net;

import java.io.IOException;
import java.io.InputStream;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

/**
 * <p>
 * Carona Comunitária USP está licenciado com uma Licença Creative Commons
 * Atribuição-NãoComercial-CompartilhaIgual 4.0 Internacional (CC BY-NC-SA 4.0).
 * <p>
 * Carona Comunitária USP is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International License (CC BY-NC-SA
 * 4.0).
 * <p>
 * Classe que provê funções para conexão e comunicação entre cliente e servidor
 * através do protocolo TCP
 * 
 ****************************************************************************** 
 **/

public class TCPClient {
	/**
	 * 
	 */
	/**
	 * Porta do servidor que será usada para a conexão
	 * */

	private int porta;
	/**
	 * Endereço ip do servidor
	 * */
	private String host;
	/**
	 * Socket usado para a transmissão de dados
	 * */
	private Socket socket;

	private OnMessageReceivedListener mMessageListener;

	private boolean run = false;

	private InputStream is;

	private PrintWriter out;

	/**
	 * Construtor com porta, ip
	 * 
	 * @param porta
	 *            porta para conexão
	 * @param host
	 *            host para conexão
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
		this.is = socket.getInputStream();
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

	public void run() {
		StringBuilder mensagem_servidor = new StringBuilder();
		run = true;
		int m;		
		while (run) {

			m = 0;
			try {
				while ((m = is.read()) != 0) {
					mensagem_servidor.append((char) m);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mMessageListener.messageReceived(mensagem_servidor.toString());
			mensagem_servidor = new StringBuilder();
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

	/**
	 * Retorna um input stream para ler dados do socket.
	 * 
	 * @throws IOException
	 */

	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	public void stop() {
		run = false;
	}

	public void setOnMessageReceivedListener(
			OnMessageReceivedListener onMessageReceivedListener) {
		this.mMessageListener = onMessageReceivedListener;
	}
}