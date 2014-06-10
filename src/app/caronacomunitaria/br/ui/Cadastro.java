package app.caronacomunitaria.br.ui;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.net.SocketService;

public class Cadastro extends Activity {

	private boolean mIsBound;
	private Messenger mService = null;
	private String usuario= null;
	private String senha = null;


	final Messenger mMessenger = new Messenger(new IncomingHandler());

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SocketService.CADASTRAR:
				mostrarAlertCodigo();
				break;

			case SocketService.ERRO:
				mostrarAlertErro();
				break;

			case SocketService.OK:
				Intent i = new Intent();
				i.putExtra("usuario", usuario);
				i.putExtra("senha", senha);
				
				Cadastro.this.setResult(SocketService.CADASTRAR, i);
				Cadastro.this.finish();
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

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};

	public void mostrarAlertCodigo()
	{

		AlertDialog.Builder alert = new AlertDialog.Builder(Cadastro.this);

		alert.setTitle(R.string.codigo_confirmacao_titulo);
		alert.setMessage(R.string.codigo_confirmacao_info);

		final EditText input = new EditText(Cadastro.this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(input);

		alert.setNeutralButton("Enviar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if(input.getText().toString().length()>0){
					dialog.dismiss();
					Bundle b = new Bundle();
					b.putInt("codigo", Integer.valueOf(input.getText().toString()));
					sendMessageToService(SocketService.CONFIRMAR_CADASTRO, b);
				}
				else
				{
					mostrarAlertErro();
				}
			}
		});



		alert.create()
		.show();


	}

	public void mostrarAlertErro()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(Cadastro.this);

		alert.setTitle("Erro");
		alert.setMessage(R.string.erro_enviar_codigo);


		alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				Cadastro.this.finish();
			}
		});


		alert.create()
		.show();
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastro);

		doBindService();
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

	void doBindService() {
		bindService(new Intent(this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;     
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.voltar:
			finish();
			break;

		case R.id.cadastrar:

			usuario = ((EditText) findViewById(R.id.login)).getText()
			.toString();
			senha = ((EditText) findViewById(R.id.senha)).getText()
					.toString();

			JSONObject jsonMensagem = new JSONObject();

			try {
				jsonMensagem.put("cadastro", JSONObject.NULL);
				jsonMensagem.put("usuario", usuario);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			if(validar())
			{
				Bundle b = new Bundle();
				b.putString("usuario", usuario);
				b.putString("senha", senha);
				sendMessageToService(SocketService.CADASTRAR, b);	
			}
			break;
		}

	}

	private boolean validar() {
		EditText etLogin = (EditText)findViewById(R.id.login);
		EditText etSenha = (EditText)findViewById(R.id.senha);
		EditText etSenha2 = (EditText)findViewById(R.id.repetir_senha);

		if(etLogin.getText().toString().trim().length()==0)
		{
			etLogin.requestFocus();
			return false;
		}
		else if(etSenha.getText().toString().trim().length()==0)
		{
			etSenha.requestFocus();
			return false;
		}
		else if (!etSenha.getText().toString().equals(etSenha2.getText().toString())) {
			etSenha2.setText("");
			etSenha2.requestFocus();

			return false;

		}
		return true;
	}	

	void doUnbindService() {
		if (mIsBound) {            
			unbindService(mConnection);
			mIsBound = false;
		}
	}
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		doUnbindService();
	}


}
