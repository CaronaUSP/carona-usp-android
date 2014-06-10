package app.caronacomunitaria.br.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.net.SocketService;


public class Main extends Activity {

	private boolean mIsBound;
	private Messenger mService = null;

	private static int nextActivity;
	private EditText editTextLogin, editTextSenha;






	private Handler IncomingHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			AlertDialog.Builder builder;
			AlertDialog dialogSucesso;
			Intent it = null;

			switch (msg.what) {

			case SocketService.ERRO:

				builder = new AlertDialog.Builder(Main.this);
				builder.setTitle("ERRO")
				.setMessage(
						Html.fromHtml("<p>Verifique sua conexão com a Internet.</p>"))
						.setNeutralButton("Ok", null);
				dialogSucesso = builder.create();
				dialogSucesso.show();
				Main.this.sendMessageToService(SocketService.CONECTAR, null);
				break;

			case SocketService.OK:

				if(nextActivity == SocketService.RECEBER_CARONA)
					it = new Intent(Main.this, ReceberCarona.class);
				else if(nextActivity == SocketService.DAR_CARONA)
					it = new Intent(Main.this, DarCarona.class);
				else if(nextActivity == SocketService.CADASTRAR)
					it = new Intent(Main.this, Cadastro.class);
				startActivityForResult(it,0); 
				break;

			case SocketService.ERRO_AUTENTICACAO:
				builder = new AlertDialog.Builder(Main.this);
				builder.setTitle("ERRO")
				.setMessage(
						Html.fromHtml("<p>Usuário ou senha errados.</p>"))
						.setNeutralButton("Ok", null);
				dialogSucesso = builder.create();
				dialogSucesso.show();
				sendMessageToService(SocketService.REINICIAR_CONEXAO, null);

				break;
			case SocketService.MSG:
				Bundle b = msg.getData();
				String mensagem = b.getString("msg");
				Toast.makeText(Main.this, mensagem, Toast.LENGTH_LONG).show();
			default:
				super.handleMessage(msg);
			}
		}
	};

	private final Messenger mMessenger = new Messenger(IncomingHandler);

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		doUnbindService();
		startService(new Intent(this, SocketService.class));
		doBindService();
		sendMessageToService(SocketService.CONECTAR, null);

		if(resultCode == SocketService.CADASTRAR)
		{
			String login = data.getStringExtra("usuario");
			String  senha = data.getStringExtra("senha");

			editTextLogin.setText(login);
			editTextSenha.setText(senha);
		}

	}


	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			try {
				Message msg = Message.obtain(null, SocketService.REGISTRAR_CLIENTE);
				msg.replyTo = mMessenger;
				mService.send(msg);

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startService(new Intent(this, SocketService.class));
		doBindService();
		editTextLogin = (EditText) findViewById(R.id.login);
		editTextSenha = (EditText) findViewById(R.id.senha);

	}



	@Override
	protected void onDestroy() {
		super.onDestroy();

		sendMessageToService(SocketService.FIM, null);

		try {
			doUnbindService();
		} catch (Exception e) {
		}
	}

	private void doBindService() {
		bindService(new Intent(this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true; 		
	}

	private void doUnbindService() {
		if (mIsBound) {            
			unbindService(mConnection);
			stopService(new Intent(this, SocketService.class));
			mIsBound = false;
		}
	}

	private void sendMessageToService(int arg, Bundle b) {
		if (mIsBound) {
			if (mService != null) {
				try {
					Message msg = Message.obtain(null, arg, 0, 0);
					msg.setData(b);
					msg.replyTo = mMessenger;
					mService.send(msg);				

				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public void onClick(View v) {

		Intent it = null;
		JSONObject json_mensagem = new JSONObject();

		String senha = editTextSenha.getText().toString();
		String login = editTextLogin.getText().toString();

		try {
			json_mensagem.put("usuario", login);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Bundle b;

		switch (v.getId()) {

		case R.id.dar_carona:
			if(validar(login,senha)){
				nextActivity = SocketService.DAR_CARONA;
				b = new Bundle();
				b.putString("usuario", login);
				b.putString("senha", senha);
				sendMessageToService(SocketService.AUTENTICAR, b);
			}

			break;


		case R.id.receber_carona:
			if(validar(login,senha)){
				nextActivity = SocketService.RECEBER_CARONA;
				b = new Bundle();
				b.putString("usuario", login);
				b.putString("senha", senha);
				sendMessageToService(SocketService.AUTENTICAR, b);
			}

			break;

		case R.id.cadastro:
			nextActivity = SocketService.CADASTRAR;
			sendMessageToService(SocketService.ESTA_CONECTADO,null);
			break;



		}

	}

	private boolean validar(String login, String senha) {
		return login.trim().length()>0 && senha.trim().length()>0;
	}

}



