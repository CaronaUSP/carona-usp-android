package app.caronacomunitaria.br.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import app.caronacomunitaria.br.R;
import app.caronacomunitaria.br.map.Coordenadas;
import app.caronacomunitaria.br.net.Consts;
import app.caronacomunitaria.br.net.SocketService;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

public class ReceberCarona extends Activity {

	private GoogleMap map;
	private final LatLng HIST = new LatLng(-23.564056, -46.722538);
	private int inicio = -1;
	private int fim = -1;
	private ArrayList<Marker> markers = new ArrayList<Marker>();
	private Coordenadas coordenadasMapa = new Coordenadas();
	
	public static final int PLACA = 1;
	public static final int CHEGANDO = 2;
	
	private final Messenger mMessenger = new Messenger(new IncomingHandler());
	private Messenger mService = null;
	private boolean mIsBound = false;
	private String placa = "";
	private boolean mensagemEnviada = false;

	private class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SocketService.ERRO:

				Log.e("erro", "erro");
				break;

			case SocketService.OK:
				Log.e("erro", "OK");

				break;

			case PLACA:
				Bundle b = msg.getData();

				placa = b.getString("placa");
				String placaInfo = String.format(ReceberCarona.this.getResources()
						.getString(R.string.placa_info), placa);
				showAlertDialog(
						ReceberCarona.this.getResources().getString(
								R.string.placa_titulo), placaInfo);
				break;
			case CHEGANDO:
				Vibrator v = (Vibrator) ReceberCarona.this
				.getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(500);
				String texto = String.format((ReceberCarona.this.getResources().getString(R.string.notificacao_info)),placa,Consts.distanciaNotificacao);

				String titulo = ReceberCarona.this.getResources().getString(R.string.notificacao_titulo);
				createNotification(titulo, texto);
				break;

			default:
				super.handleMessage(msg);
			}

		}
	}

	private void createNotification(String titulo, String texto) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(titulo).setContentText(texto);

		Intent notificationIntent = new Intent(this, ReceberCarona.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, builder.build());
	}

	private void removeNotification() {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(0);
	}

	private void showAlertDialog(String titulo, String mensagem) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(titulo);
		alert.setMessage(mensagem);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();

			}
		});
		alert.create().show();
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			try {
				Message msg = Message.obtain(null,
						SocketService.REGISTRAR_CLIENTE);
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

	private OnMarkerClickListener markerClickListener = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker m) {

			int indice = coordenadasMapa.getTitulos().indexOf(m.getTitle());

			if (inicio == -1) {
				inicio = indice;
				m.setIcon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
			} else if (fim == -1 && indice!=inicio) {
				fim = indice;
				m.setIcon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
			}
			else{
				float corFim = coordenadasMapa.getCoordenadas().get(
						fim).getCor();
				float corInicio =  coordenadasMapa.getCoordenadas().get(
						inicio).getCor();
				markers.get(inicio).setIcon(BitmapDescriptorFactory
						.defaultMarker(corInicio));
				markers.get(fim).setIcon(BitmapDescriptorFactory
						.defaultMarker(corFim));
				fim = -1;
				inicio = indice; 
				m.setIcon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

			}
			return false;
		}

	};

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.confirmar:
			JSONObject jsonMensagem = new JSONObject();
			if (inicio != -1 && fim != -1) {
				try {
					if (!mensagemEnviada) {
						jsonMensagem.put("inicio", inicio);
						jsonMensagem.put("fim", fim);
						mensagemEnviada = true;
					}
				} catch (JSONException e) {
				}

				Bundle b = new Bundle();
				b.putString("coordenadas", jsonMensagem.toString());
				sendMessageToService(SocketService.RECEBER_CARONA, b);
			} else {
				showAlertDialog(this.getResources().getString(R.string.erro), this.getResources().getString(R.string.erro_inicio_fim_info) );
			}

			break;

		case R.id.voltar:
			removeNotification();
			this.finish();
			break;

		default:
			break;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receber_carona);
		doBindService();

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		for (PontoMapa ponto : coordenadasMapa.getCoordenadas()) {
			markers.add(adicionarAoMapa(map, ponto.getCoordenada(),
					ponto.getTitulo(), ponto.getDescricao(), ponto.getCor()));
		}

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HIST, 25));

		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();		
		inicio = -1;
		fim = -1;
		doUnbindService();
		
		removeNotification();
	}

	private void doUnbindService() {
		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}
	}

	private void doBindService() {
		bindService(new Intent(this, SocketService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	public Marker adicionarAoMapa(GoogleMap g, LatLng lugar, String titulo,
			String descricao, float color) {
		Marker m = g.addMarker(new MarkerOptions().position(lugar)
				.title(titulo).snippet(descricao));
		m.setIcon(BitmapDescriptorFactory.defaultMarker(color));
		g.setOnMarkerClickListener(markerClickListener);
		return m;
	}
}
