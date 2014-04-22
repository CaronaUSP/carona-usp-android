package app.caronacomunitaria.br;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import app.caronacomunitaria.br.R;

public class Main extends Activity {
	JSONArray json_mensagem_servidor;
	public final String IP_SERVIDOR = "200.144.254.99";
	public final int PORTA = 3280;
	public final int PORTA_LOCAL = 3280;
	Semaphore semaforo = new Semaphore(0);
	boolean autenticar = false;
	Thread TCPListener;
	public String hash_recebido;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dar_carona:
			Intent it = new Intent(this, Mapa.class);
			startActivity(it);
			break;

		case R.id.receber_carona:
			it = new Intent(this, Mapa.class);
			startActivity(it);
			break;

		case R.id.cadastro:
			it = new Intent(this, Cadastro.class);
			startActivity(it);
			break;
		case R.id.button1:
			it = new Intent(this, Esqueci.class);
			startActivity(it);
			break;

		}

	}

	public boolean autenticar(String numero_usp, String senha)
			throws UnknownHostException, IOException, InterruptedException {

		InetAddress ip = InetAddress.getByName(IP_SERVIDOR);

		TCPClient tcp = new TCPClient(PORTA, ip.getAddress(), PORTA_LOCAL);

		tcp.setOnMessageReceivedListener(new TCPClient.OnMessageReceivedListener() {
			public void messageReceived(String mensagem) {
				if (mensagem.equals("ok")) // FIX ME
				{
					autenticar = true;
					semaforo.release();
					TCPListener.interrupt();
				} else {
					hash_recebido = mensagem;
					semaforo.release();
				}
			}
		});

		tcp.conectar();
		json_mensagem_servidor.put(0x494c4f50);
		json_mensagem_servidor.put(0);
		json_mensagem_servidor.put(numero_usp);
		tcp.enviarMensagem(json_mensagem_servidor.toString());

		TCPListener = new Thread(tcp);
		TCPListener.start();

		semaforo.acquire();

		// enviar hash + hash(senha);

		semaforo.acquire();

		tcp.desconectar();

		return autenticar;
	}
}
