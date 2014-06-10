package app.caronacomunitaria.br.net;

import java.io.IOException;
import java.net.UnknownHostException;

import org.json.JSONException;
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
import app.caronacomunitaria.br.ui.DarCarona;
import app.caronacomunitaria.br.ui.ReceberCarona;

public class SocketService extends Service {
	private static boolean estaRodando = false;

	public static final int REGISTRAR_CLIENTE = 1;
	public static final int AUTENTICAR = 2;
	public static final int CADASTRAR = 3;
	public static final int DAR_CARONA = 4;
	public static final int CONFIRMAR_CADASTRO = 5;
	public static final int FIM = 6;
	public static final int CONECTAR = 7;
	public static final int RECEBER_CARONA = 8;

	public static final int ERRO_AUTENTICACAO = -2;
	public static final int ERRO = -1;
	public static final int OK = 0;

	public static final int ESTA_CONECTADO = 9;
	public static final int REINICIAR_CONEXAO = 10;

	private String senha;
	private String user;
	private String coordenadas;
	private String mensagemLogin = null;
	private int codigo;

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

			case CONECTAR:
				new Thread(threadConectar).start();
				break;

			case REINICIAR_CONEXAO:
				try {
					tcpClient.enviarMensagem(Consts.MSG_FIM);
					mensagemLogin = null;
				} catch (IOException e) {
					e.printStackTrace();
				}

				new Thread(threadConectar).start();
				break;

			case ESTA_CONECTADO:
				if (isConnected())
					sendMessageToUI(OK, null);
				else
					sendMessageToUI(ERRO, null);
				break;

			case REGISTRAR_CLIENTE:
				cliente = msg.replyTo;
				break;

			case AUTENTICAR:
				if (isConnected()) {
					b = msg.getData();
					senha = b.getString("senha");
					user = b.getString("usuario");
					new Thread(threadAutenticar).start();
				} else {
					sendMessageToUI(ERRO, null);
				}

				break;

			case CADASTRAR:
				if (isConnected()) {
					b = msg.getData();
					senha = b.getString("senha");
					user = b.getString("usuario");
					new Thread(threadCadastrar).start();
				} else
					sendMessageToUI(ERRO, null);
				break;

			case DAR_CARONA:
				if (msg.arg1 == 0) {
					b = msg.getData();
					coordenadas = b.getString("coordenadas");
					new Thread(threadDarCarona).start();
				} else {
					b = msg.getData();

					try {
						tcpClient.enviarMensagem(b.getString("proximo"));
					} catch (Exception e) {
						//
						e.printStackTrace();
					}
				}
				break;
			case CONFIRMAR_CADASTRO:
				b = msg.getData();
				codigo = b.getInt("codigo");
				new Thread(threadConfirmarCadastro).start();
				break;
			case FIM:
				try {
					tcpClient.enviarMensagem(Consts.MSG_FIM);
				} catch (IOException e) {
				}
			case RECEBER_CARONA:
				if (isConnected()) {
					b = msg.getData();
					coordenadas = b.getString("coordenadas");
					new Thread(threadReceberCarona).start();
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	private Thread threadCadastrar = new Thread() { 

		JSONObject jsonMensagem = new JSONObject();

		public void run() {

			tcpClient
			.setOnMessageReceivedListener(new OnMessageReceivedListener() {

				@Override
				public void messageReceived(String s) {

					try {
						jsonMensagem.put("cadastro", JSONObject.NULL);
						jsonMensagem.put("usuario", user);

						JSONObject jsonRecebido = new JSONObject(s);
						if (jsonRecebido.has(Consts.LOGIN)) {
							SocketService.this.mensagemLogin = jsonRecebido
									.getString(Consts.LOGIN);

						} else if (jsonRecebido.has("ok")) {
							if ((Boolean) jsonRecebido.get("ok")) {
								tcpClient.stop();
								sendMessageToUI(CADASTRAR, null);
							} else {
								sendMessageToUI(ERRO, null);
							}

						}
					} catch (Exception e) {
						tcpClient.desconectar();
						e.printStackTrace();
					}

				}
			});
			try {
				jsonMensagem.put("cadastro", JSONObject.NULL);
				jsonMensagem.put("usuario", user);
				String hash = Hash.gerarHash(Consts.MENSAGEM_HASH + senha,
						Consts.ALGORITMO);
				jsonMensagem.put("hash", hash);
				tcpClient.enviarMensagem(jsonMensagem.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			tcpClient.run();

		}
	};

	private Thread threadConfirmarCadastro = new Thread() {
		JSONObject jsonMensagem = new JSONObject();

		public void run() {
			try {
				jsonMensagem.put("codigo", codigo);

				tcpClient.enviarMensagem(jsonMensagem.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			tcpClient
			.setOnMessageReceivedListener(new OnMessageReceivedListener() {

				@Override
				public void messageReceived(String s) {
					try {
						JSONObject mensagemRecebida = new JSONObject(s);
						if (mensagemRecebida.has("ok")) {
							if ((Boolean) mensagemRecebida.get("ok") == Boolean.TRUE) {
								sendMessageToUI(OK, null);
							} else {
								sendMessageToUI(ERRO, null);
							}
						}

					} catch (Exception e) {

					}
				}
			});

			tcpClient.run();

		}

	};

	private Thread threadConectar = new Thread() {

		@Override
		public void run() {
			try {
				tcpClient.conectar();
				mensagemLogin = null;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private Thread threadAutenticar = new Thread() {
		JSONObject jsonMensagem = new JSONObject();

		@Override
		public void run() {

			tcpClient
			.setOnMessageReceivedListener(new OnMessageReceivedListener() {

				@Override
				public void messageReceived(String s) {
					try {
						JSONObject jsonRecebido = new JSONObject(s);
						if (jsonRecebido.has(Consts.LOGIN)) {
							jsonMensagem.put("usuario", user);
							mensagemLogin = jsonRecebido
									.getString(Consts.LOGIN);

							jsonMensagem.put("hash",Hash.gerarHash(mensagemLogin+ Hash.gerarHash(Consts.MENSAGEM_HASH									+ senha,
									Consts.ALGORITMO),
									Consts.ALGORITMO));

							tcpClient.enviarMensagem(jsonMensagem
									.toString());
						} else if (jsonRecebido.has("ok")) {
							tcpClient.stop();
							if ((Boolean) jsonRecebido.get("ok")) {
								sendMessageToUI(OK, null);
							} else {
								sendMessageToUI(ERRO_AUTENTICACAO, null);
							}

						} else if (jsonRecebido.has("fim")) {
							tcpClient.stop();
							sendMessageToUI(ERRO_AUTENTICACAO, null);
						}
					} catch (Exception e) {
						tcpClient.desconectar();
						sendMessageToUI(ERRO, null);
						e.printStackTrace();
					}

				}
			});
			if (mensagemLogin != null) {
				try {
					jsonMensagem.put("usuario", user);
					jsonMensagem.put(
							"hash",
							Hash.gerarHash(mensagemLogin + Consts.MENSAGEM_HASH
									+ senha, Consts.ALGORITMO));

					tcpClient.enviarMensagem(jsonMensagem.toString());
				} catch (Exception e) {
					// TODO tratar exceção
				}
			}
			tcpClient.run();
		}

	};

	private Thread threadDarCarona = new Thread() {
		@Override
		public void run() {
			JSONObject jsonMensagem;
			try {
				jsonMensagem = new JSONObject(coordenadas);
				tcpClient.enviarMensagem(jsonMensagem.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			tcpClient
			.setOnMessageReceivedListener(new OnMessageReceivedListener() {

				@Override
				public void messageReceived(String s) {
					try {
						JSONObject jsonMensagemRecebida = new JSONObject(
								s);
						if (jsonMensagemRecebida.has("parar")) {
							Bundle b = new Bundle();
							b.putInt("parar",
									(Integer) jsonMensagemRecebida
									.get("parar"));
							sendMessageToUI(DarCarona.PARAR, b);
						}
					} catch (JSONException e) {
						//
						e.printStackTrace();
					}
				}
			});
			tcpClient.run();
		}
	};

	private Thread threadReceberCarona = new Thread() {
		@Override
		public void run() {
			try {
				JSONObject jsonMensagem = new JSONObject(coordenadas);
				tcpClient.enviarMensagem(jsonMensagem.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			tcpClient
			.setOnMessageReceivedListener(new OnMessageReceivedListener() {

				@Override
				public void messageReceived(String s) {
					try {
						JSONObject jsonMensagemRecebida = new JSONObject(
								s);
						if (jsonMensagemRecebida.has("placa")) {
							Bundle b = new Bundle();
							String placa = jsonMensagemRecebida
									.getString("placa");
							b.putString("placa", placa);
							sendMessageToUI(ReceberCarona.PLACA, b);
						} else if (jsonMensagemRecebida.has("chegando")) {
							sendMessageToUI(ReceberCarona.CHEGANDO,
									null);
						}
					} catch (JSONException e) {
						//
						e.printStackTrace();
					}
				}
			});

			tcpClient.run();
		}
	};

	private void sendMessageToUI(int arg, Bundle b) {

		try {			
			Message msg = Message.obtain(null, arg);
			msg.setData(b);
			cliente.send(msg);
		} catch (RemoteException e) {
			cliente = null;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		estaRodando = true;
		new Thread(threadConectar).start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public static boolean isRunning() {
		return estaRodando;
	}

	public boolean isConnected() {
		return tcpClient.isConnected();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tcpClient.desconectar();
		mensagemLogin = null;
		estaRodando = false;
	}
}
