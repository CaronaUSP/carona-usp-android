package app.caronacomunitaria.br.net;

import java.io.IOException;
import java.net.UnknownHostException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import app.caronacomunitaria.br.crypto.Hash;

public class SocketService extends Service {
	private static boolean isRunning = false;
	private boolean isConnected = false;


	public static final int REGISTRAR_CLIENTE = 1;
	public static final int AUTENTICAR = 2;
	public static final int CADASTRAR = 3;
	public static final int DAR_CARONA = 4;


	public static final int ERRO_AUTENTICACAO = -2;
	public static final int ERRO = -1;
	public static final int OK = 0;

	private String senha;
	private String user;
	private String coordenadas;

	Messenger cliente; 

	final Messenger mMessenger = new Messenger(new IncomingHandler());

	TCPClient tcpClient = new TCPClient(Consts.PORTA, Consts.HOST);

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	public class IncomingHandler extends Handler { 
		@Override
		public void handleMessage(Message msg) {	
			Bundle b;
			switch (msg.what) {
			

			case REGISTRAR_CLIENTE:
				cliente = msg.replyTo;
				break;

			case AUTENTICAR:
				if(isConnected)
				{
				b = msg.getData();
				senha = b.getString("senha");
				user = b.getString("usuario");
				new Thread(threadAutenticar).start();
				}
				else{
				sendMessageToUI(ERRO);	
				}
				
				break;
			case CADASTRAR:
				b = msg.getData();
				senha = b.getString("senha");
				user = b.getString("usuario");
				new Thread(threadCadastrar).start();
				break;
				
			case DAR_CARONA:
				b = msg.getData();
				coordenadas = b.getString("coordenadas");
				Log.e("coordenadasdasda",coordenadas);
				new Thread(threadDarCarona).start();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	Thread threadCadastrar = new Thread(){


		public void run() {

			tcpClient.setOnMessageReceivedListener(new OnMessageReceivedListener() {
				private JSONObject jsonMensagem = new JSONObject();

				@Override
				public void messageReceived(String s) {
					try {
						Log.i("Mensagem recebida", s);
						jsonMensagem.put("cadastro", JSONObject.NULL);
						jsonMensagem.put("usuario", user);

						JSONObject jsonRecebido = new JSONObject(s);
						if (jsonRecebido.has(Consts.LOGIN)) {
							String hash = Hash.gerarHash(
									jsonRecebido.getString(Consts.LOGIN)
									+ Consts.MENSAGEM_HASH
									+ senha, Consts.ALGORITMO);
							jsonMensagem.put("hash", hash);
							Log.e("HASH", jsonMensagem.toString());
							tcpClient.enviarMensagem(jsonMensagem
									.toString());

						}
						else if (jsonRecebido.has("ok")) {
							tcpClient.stop();
							sendMessageToUI(OK);

						}
					} catch (Exception e) {
						tcpClient.desconectar();
						e.printStackTrace();
					}

				}
			});

			tcpClient.run();

		}
	};

	Thread threadConectar = new Thread(){

		@Override
		public void run() {
			try {
				tcpClient.conectar();
				isConnected = true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	Thread threadAutenticar = new Thread(){
		@Override
		public void run(){
			tcpClient.setOnMessageReceivedListener(new OnMessageReceivedListener() {
				private JSONObject jsonMensagem = new JSONObject();

				@Override
				public void messageReceived(String s) {
					try {
						JSONObject jsonRecebido = new JSONObject(s);
						if (jsonRecebido.has(Consts.LOGIN)) {
							jsonMensagem.put("usuario", user);
							String mensagemRecebida = jsonRecebido.getString(Consts.LOGIN);

							jsonMensagem.put("hash", Hash.gerarHash(mensagemRecebida
									+ Consts.MENSAGEM_HASH + senha, Consts.ALGORITMO));

							tcpClient.enviarMensagem(jsonMensagem.toString());
						}
						else if (jsonRecebido.has("ok")) {
							tcpClient.stop();
							sendMessageToUI(OK);
						}
						else if (jsonRecebido.has("fim")){
							tcpClient.stop();
							sendMessageToUI(ERRO_AUTENTICACAO);							
						}
					} catch (Exception e) {
						tcpClient.desconectar();
						isConnected = false;
						sendMessageToUI(ERRO);
						e.printStackTrace();
					}

				}
			});

			tcpClient.run();
		}

	};
	
	Thread threadDarCarona = new Thread(){
		@Override
		public void run()
			{
			JSONObject jsonMensagem;
			try {
				jsonMensagem = new JSONObject(coordenadas);
				tcpClient.enviarMensagem(jsonMensagem.toString());
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			}	
	};

	Thread Receber_carona = new Thread(){
		@Override
		public void run()
		{
			//TODO
		}

	};

	private void sendMessageToUI(int arg) {
		
				try{
				cliente.send(
						Message.obtain(null, arg, 0,
								0));
				Message msg = Message.obtain(null, arg);
				cliente.send(msg);
				}
				catch(RemoteException e){
					cliente = null;					
				}	
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("Serviço", "Serviço iniciado.");
		isRunning = true;	

		new Thread(threadConectar).start();
		if(isConnected)
			Log.i("Conexão", "Conectado com o servidor");

	}	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public static boolean isRunning() {
		return isRunning;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tcpClient.desconectar();
		
		Log.i("Serviço", "Serviço parado.");
		isRunning = false;
	}










}