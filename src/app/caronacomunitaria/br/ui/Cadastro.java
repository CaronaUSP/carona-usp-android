package app.caronacomunitaria.br.ui;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
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

public class Cadastro extends Activity {

	public TCPClient tcp_client;
	public TCPListener tcp_listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro);

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
		switch (v.getId()) {
		case R.id.voltar:
			finish();
			break;
		case R.id.cadastrar:

			// TODO Validar compos login e senha

			String login = ((EditText) findViewById(R.id.numeroUSP)).getText()
					.toString();
			String senha = ((EditText) findViewById(R.id.Senha)).getText()
					.toString();

			JSONObject json_mensagem = new JSONObject();
			JSONArray json_usuario = new JSONArray();

			try {
				json_usuario.put(login);
				json_usuario.put(Hash.gerarHash(senha.getBytes(), "SHA-256"));
				json_mensagem.put("usuario", json_usuario);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			new Cadastrar().execute(json_mensagem.toString());

			finish();
			break;
		}

	}

	public void cadastrar(JSONObject json_usuario) throws IOException {
		TCPClient tcp_client = new TCPClient(Consts.PORTA, Consts.HOST);

		tcp_client.conectar();
		tcp_client.enviarMensagem(json_usuario.toString());
		tcp_client.desconectar();
	}

	public class Cadastrar extends AsyncTask<String, String, String> {
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
