package app.caronacomunitaria.br.ui;

import java.io.IOException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.crypto.Hash;
import app.caronacomunitaria.br.net.Consts;
import app.caronacomunitaria.br.net.TCPClient;

public class Cadastro extends Activity {

	private JSONObject json_usuario = new JSONObject();

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
			
			try {
				json_usuario.put("numeroUSP", numeroUSP);
				json_usuario.put("senha", Hash.gerarHash(senha.getBytes(), "SHA-1"));
				cadastrar(json_usuario);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
}
