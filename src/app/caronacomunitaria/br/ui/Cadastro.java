package app.caronacomunitaria.br.ui;

import java.io.IOException;

import org.json.JSONObject;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.net.Consts;
import app.caronacomunitaria.br.net.OnMessageReceivedListener;
import app.caronacomunitaria.br.net.TCPClient;
import app.caronacomunitaria.br.net.TCPListener;

public class Cadastro extends Activity {

	private JSONObject json_usuario = new JSONObject();
	public TCPClient tcp_client;

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
			String numeroUSP = ((EditText) findViewById(R.id.numeroUSP))
					.getText().toString();
			String senha = ((EditText) findViewById(R.id.Senha)).getText()
					.toString();

			new Cadastrar().execute(""); // TODO

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

		private TCPListener tcp_listener;

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

			return null;
		}

		@Override
		protected void onProgressUpdate(String... mensagem) {
			// TODO
			super.onProgressUpdate(mensagem);
		}
	}
}
