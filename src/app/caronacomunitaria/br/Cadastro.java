package app.caronacomunitaria.br;

import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import app.caronacomunitaria.br.R;

public class Cadastro extends Activity {

	JSONObject json_usuario = new JSONObject();
	public final String IP_SERVIDOR = "200.144.254.99";
	public final int PORTA = 3280;
	public final int PORTA_LOCAL = 3280;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro);

	}

	public static byte[] gerarHash(byte[] s, String algoritmo) {
		try {
			MessageDigest md = MessageDigest.getInstance(algoritmo);
			md.update(s);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
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
				json_usuario.put("senha", gerarHash(senha.getBytes(), "SHA-1"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				cadastrar(json_usuario);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			finish();
			break;
		}

	}

	public void cadastrar(JSONObject json_usuario) throws IOException {
		InetAddress ip = InetAddress.getByName(IP_SERVIDOR);
		TCPClient tcp = new TCPClient(PORTA, ip.getAddress(), PORTA_LOCAL);
		tcp.conectar();
		tcp.enviarMensagem(json_usuario.toString());
		tcp.desconectar();
	}
}
