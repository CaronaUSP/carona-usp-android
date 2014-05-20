package app.caronacomunitaria.br.ui;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.crypto.Hash;
import app.caronacomunitaria.br.net.Consts;
import app.caronacomunitaria.br.net.OnMessageReceivedListener;
import app.caronacomunitaria.br.net.TCPClient;
import app.caronacomunitaria.br.net.TCPListener;

public class Main extends Activity {

	private TCPClient tcp_client;
	private TCPListener tcp_listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			tcp_client.desconectar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onClick(View v) {

		Intent it;
		EditText edSenha = (EditText) findViewById(R.id.password);
		EditText edLogin = (EditText) findViewById(R.id.login);

		JSONObject json_mensagem = new JSONObject();
		try {
			json_mensagem.put("usuario", edLogin.getText().toString());
			json_mensagem.put("hash", Hash.gerarHash(edSenha.getText()
					.toString().getBytes(), "SHA-256"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switch (v.getId()) {

		case R.id.dar_carona:

			new Autenticar().execute(json_mensagem.toString());

			// TODO Validar os os EditText login e senha

			it = new Intent(this, Mapa.class);
			startActivity(it);

			break;

		case R.id.receber_carona:

			new Autenticar().execute(json_mensagem.toString());
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
		private String mensagem_servidor;

		@Override
		protected String doInBackground(String... mensagem) {
			this.mensagem_servidor = mensagem[0];

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

			return null;
		}

		@Override
		protected void onProgressUpdate(String... mensagem) {
			// TODO
			super.onProgressUpdate(mensagem);
			Log.e("Mensagem recebida", mensagem[0]);
			try {
				tcp_client.enviarMensagem(mensagem_servidor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
