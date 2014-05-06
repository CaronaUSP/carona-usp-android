package app.caronacomunitaria.br.ui;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.crypto.Hash;
import app.caronacomunitaria.br.net.Consts;
import app.caronacomunitaria.br.net.OnMessageReceivedListener;
import app.caronacomunitaria.br.net.TCPClient;
import app.caronacomunitaria.br.net.TCPListener;

public class Main extends Activity {

	private JSONArray json_mensagem_servidor;
	private JSONObject json_login;

	private Semaphore semaforo = new Semaphore(0);
	private String hash_recebido;
	private TCPClient tcp_client;
	private TCPListener tcp_listener;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick(View v) {

		Intent it;
		EditText edSenha = (EditText) findViewById(R.id.password);
		EditText edLogin = (EditText) findViewById(R.id.login);

		json_login = new JSONObject();
		try {
			json_login.put("login", edLogin.getText().toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}

		switch (v.getId()) {

		case R.id.dar_carona:
			new Autenticar().execute(edSenha.getText().toString());

			// TODO

			it = new Intent(this, Mapa.class);
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

	public class Autenticar extends AsyncTask<String, String, String> {

		private JSONObject mensagem_servidor;

		@Override
		protected String doInBackground(String... mensagem) {

			tcp_client = new TCPClient(Consts.PORTA, Consts.HOST);
			try {
				tcp_client.conectar();
				tcp_listener = new TCPListener(tcp_client);

			} catch (Exception e) {
				Log.e("ERRO DE CONEXÃO", "Verifique sua conexão com a Internet");
				return null;
			}

			tcp_listener
					.setOnMessageReceivedListener(new OnMessageReceivedListener() {
						@Override
						public void messageReceived(String s) {
							publishProgress(s);
						}
					});

			tcp_listener.run();

			try {
				tcp_client.enviarMensagem(json_login.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... mensagem) {
			// TODO
			super.onProgressUpdate(mensagem);
			try {
				mensagem_servidor = new JSONObject(mensagem[0]);
				//TODO

			} catch (JSONException e) {
				Log.w("Mensagem recebida", mensagem[0]);
			}

		}
	}

	public void enviarHash(String mensagem_recebida, String hash_calculado)
			throws IOException, JSONException {
		String hash = Hash.gerarHash(
				(mensagem_recebida + hash_calculado).getBytes(), "SHA-1");
		JSONObject json_hash = new JSONObject();
		json_hash.put("hash", hash);
		tcp_client.enviarMensagem(json_hash.toString());
	}
}
