package app.caronacomunitaria.br.ui;


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

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voltar:
			finish();
			break;
		case R.id.cadastrar:

			String usuario = ((EditText) findViewById(R.id.numeroUSP))
					.getText().toString();
			String senha = ((EditText) findViewById(R.id.Senha)).getText()
					.toString();

			String hash = Hash.gerarHash(Consts.MENSAGEM_HASH + senha,
					Consts.ALGORITMO);
			
			JSONObject json_mensagem = new JSONObject();

			try {
				json_mensagem.put("cadastro", JSONObject.NULL);
				json_mensagem.put("usuario", usuario);
				json_mensagem.put("hash", hash);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			new Cadastrar().execute(json_mensagem);

			break;
		}

	}

	public class Cadastrar extends AsyncTask<JSONObject, String, String> {

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
			// TODO
			super.onProgressUpdate(mensagem);
			Log.e("Mensagem recebida", mensagem[0]);

			try {
				JSONObject jsonRecebido = new JSONObject(mensagem[0]);
				if (jsonRecebido.has("login")) {
					tcpClient.enviarMensagem(jsonMensagem.toString());
				} else if (jsonRecebido.has("msg")) {
					tcpListener.stop();
				}

			} catch (Exception e) {
				tcpListener.stop();
				e.printStackTrace();
			}
		}
	}
}
