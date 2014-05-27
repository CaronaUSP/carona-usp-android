package app.caronacomunitaria.br.ui;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// TODO
		// fechar conexão caso ela esteja aberta

	}

	public void onClick(View v) {

		Intent it;
		EditText edSenha = (EditText) findViewById(R.id.password);
		EditText edLogin = (EditText) findViewById(R.id.login);

		JSONObject json_mensagem = new JSONObject();
		try {
			json_mensagem.put("usuario", edLogin.getText().toString());
			json_mensagem.put("senha", edSenha.getText().toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		switch (v.getId()) {

		case R.id.dar_carona:

			new Autenticar().execute(json_mensagem);

			// TODO Validar os os EditText login e senha

			it = new Intent(this, Mapa.class);
			startActivity(it);

			break;

		case R.id.receber_carona:

			new Autenticar().execute(json_mensagem);
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

	public class Autenticar extends AsyncTask<JSONObject, String, String> {

		private JSONObject jsonMensagem;
		private TCPClient tcpClient;
		private TCPListener tcpListener;

		@Override
		protected String doInBackground(JSONObject... mensagem) {
			this.jsonMensagem = mensagem[0];

			tcpClient = new TCPClient(Consts.PORTA, Consts.HOST);
			try {
				tcpClient.conectar();
				tcpListener = new TCPListener(tcpClient);

			} catch (Exception e) {
				Log.e("ERRO DE CONEXÃO", "Verifique sua conexão com a Internet");
				return null;
			}

			tcpListener
					.setOnMessageReceivedListener(new OnMessageReceivedListener() {
						@Override
						public void messageReceived(String s) {
							publishProgress(s);

						}
					});

			tcpListener.run();

			return null;
		}

		@Override
		protected void onProgressUpdate(String... mensagem) {
			
			super.onProgressUpdate(mensagem);
			Log.e("Mensagem recebida", mensagem[0]);

			try {
				JSONObject jsonRecebido = new JSONObject(mensagem[0]);

				if (jsonRecebido.has("login")) {
					String senha = jsonMensagem.getString("senha");
					String mensagemRecebida = jsonRecebido.getString("login");

					jsonMensagem.put("hash", Hash.gerarHash(mensagemRecebida
							+ Consts.MENSAGEM_HASH + senha, Consts.ALGORITMO));
					jsonMensagem.remove("senha");

					tcpClient.enviarMensagem(jsonMensagem.toString());

				} else {
					if (jsonRecebido.has("fim")) {
						tcpListener.stop();
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("Mensagem recebida", mensagem[0]);

				tcpListener.stop();
			}

		}
	}

}
