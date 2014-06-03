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
import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.net.SocketService;


public class Main extends Activity {

	boolean mIsBound;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	Messenger mService = null;


	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			AlertDialog.Builder builder;
			AlertDialog dialogSucesso;
			
			switch (msg.what) {

			case SocketService.ERRO:

				builder = new AlertDialog.Builder(Main.this);
				builder.setTitle("ERRO")
				.setMessage(
						Html.fromHtml("<p>Verifique sua conexão com a Internet.</p>"));	
				dialogSucesso = builder.create();
				dialogSucesso.show();				
				break;

			case SocketService.OK:
				Intent it = new Intent(Main.this, Mapa.class);
				startActivity(it);
				break;			

			case SocketService.ERRO_AUTENTICACAO:
				builder = new AlertDialog.Builder(Main.this);
				builder.setTitle("ERRO")
				.setMessage(
						Html.fromHtml("<p>Verifique sua conexão com a Internet.</p>"));	
				dialogSucesso = builder.create();
				dialogSucesso.show();				
				break;
			default:
				super.handleMessage(msg);
			}

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

	}



	@Override
	protected void onDestroy() {
		super.onDestroy();

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

		String senha = ((EditText) findViewById(R.id.senha)).getText().toString();
		String login = ((EditText) findViewById(R.id.login)).getText().toString();

		try {
			json_mensagem.put("usuario", login);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		switch (v.getId()) {

		case R.id.dar_carona:
			if(validar())
				it = new Intent(this, Mapa.class);
			startActivity(it);
			break;
			// @TODO Validar os os EditText login e senha

		case R.id.receber_carona:
			Bundle b = new Bundle();
			b.putString("usuario", login);
			b.putString("senha", senha);
			sendMessageToService(SocketService.AUTENTICAR, b);
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

	private boolean validar() {

		// TODO validar formulário
		return true;
	}

}



