package app.caronacomunitaria.br.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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

	private int porta;
	private String host;
	private Socket socket;
	private OutputStreamWriter outw;

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
		socket = new Socket(host, porta);
		outw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
	}

	/**
	 * Envia o argumento <i>mensagem</i> para o servidor
	 * 
	 * @throws IOException
	 */

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

	/**
	 * Fecha o socket.
	 * 
	 * @throws IOException
	 */

	public void desconectar() throws IOException {
		socket.close();
	}

	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
}