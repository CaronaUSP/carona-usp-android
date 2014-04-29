package app.caronacomunitaria.br.ui;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.crypto.Hash;
import app.caronacomunitaria.br.net.Consts;
import app.caronacomunitaria.br.net.OnMessageReceivedListener;
import app.caronacomunitaria.br.net.TCPClient;
import app.caronacomunitaria.br.net.TCPListener;

public class Main extends Activity {

	private JSONArray json_mensagem_servidor;
	private JSONObject json_hash;

	private Semaphore semaforo = new Semaphore(0);
	private boolean autenticar = false;
	private String hash_recebido;
	private TCPClient tcp_client;
	private TCPListener tcp_listener;
	private OnMessageReceivedListener messageReceived = new OnMessageReceivedListener() {
		public void messageReceived(String mensagem) {
			if (mensagem == "mensagem_de_erro") {
			}
			hash_recebido = mensagem;
			semaforo.release();
		}
	};

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
		case R.id.esqueci:
			it = new Intent(this, Esqueci.class);
			startActivity(it);
			break;

		}

	}

	public boolean autenticar(String numero_usp, String senha)
			throws IOException, InterruptedException, JSONException {

		tcp_client = new TCPClient(Consts.PORTA, Consts.HOST);
		tcp_listener = new TCPListener(tcp_client);
		tcp_listener.setOnMessageReceivedListener(messageReceived);
		tcp_client.conectar();

		json_mensagem_servidor.put(0x494c4f50);
		json_mensagem_servidor.put(0);
		json_mensagem_servidor.put(numero_usp);

		tcp_client.enviarMensagem(json_mensagem_servidor.toString());

		new Thread(tcp_listener).start();
		semaforo.acquire();
		
		json_hash.put("hash", hash_recebido
				+ Hash.gerarHash(senha.getBytes(), Consts.ALGORITMO));
		tcp_client.enviarMensagem(json_hash.toString());

		semaforo.acquire();
		// mensagem de autenticação servidor

		tcp_listener.stop();
		tcp_client.desconectar();

		return autenticar;
	}
}
