package app.caronacomunitaria.br.ui;

import java.util.ArrayList;

import org.json.JSONArray;
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
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DarCarona extends Activity {

	private GoogleMap map;
	private final LatLng HIST = new LatLng(-23.564056, -46.722538);
	private static ArrayList<Integer> indiceCoordenadas = new ArrayList<Integer>();

	private final Messenger mMessenger = new Messenger(new IncomingHandler());
	private Messenger mService = null;
	private boolean mIsBound = false;
	private boolean mensagemEnviada = false; 

	private Coordenadas coordenadasMapa = new Coordenadas();

	public static final int PARAR = 1;

	private final long DISTANCIA_MINIMA = 100;
	private final long TEMPO_MINIMO = 2000;

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

			case PARAR:
				Bundle b = msg.getData();
				int parar = b.getInt("parar");
				String titulo = DarCarona.this.getResources().getString(
						R.string.parar_titulo);
				String pontoParar = coordenadasMapa.getCoordenadas()
						.get(indiceCoordenadas.get(parar)).getDescricao();
				String mensagem = String.format(DarCarona.this.getResources()
						.getString(R.string.parar_info), pontoParar);
				showAlertDialog(titulo, mensagem);

			default:
				super.handleMessage(msg);
			}

		}

	}

	private void showAlertDialog(String titulo, String mensagem) {
		AlertDialog.Builder alert = new AlertDialog.Builder(DarCarona.this);

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

			if (indiceCoordenadas.contains(Integer.valueOf(indice))) {

				PontoMapa pontoMapa = coordenadasMapa.getCoordenadas().get(
						indice);
				float cor = pontoMapa.getCor();
				indiceCoordenadas.remove(Integer.valueOf(indice));
				m.setIcon(BitmapDescriptorFactory.defaultMarker(cor));

			} else {
				indiceCoordenadas.add(Integer.valueOf(indice));
				m.setIcon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
			}

			return false;
		}

	};

	public void onClick(View v) {

		AlertDialog.Builder alert;
		switch (v.getId()) {

		case R.id.confirmar:

			if (!mensagemEnviada) {
				alert = new AlertDialog.Builder(DarCarona.this);
				alert.setTitle("Dados do veículo").setMessage(
						R.string.insira_caracteristicas); 

				LinearLayout layout = new LinearLayout(DarCarona.this);
				layout.setOrientation(LinearLayout.VERTICAL);

				final EditText inputPlaca = new EditText(DarCarona.this);
				inputPlaca.setHint(R.string.dados);
				inputPlaca
				.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				final EditText inputLugares = new EditText(DarCarona.this);
				inputLugares.setHint(R.string.hint_lugares);
				inputLugares.setInputType(InputType.TYPE_CLASS_NUMBER);


				layout.addView(inputPlaca);
				layout.addView(inputLugares);

				alert.setView(layout).setPositiveButton("Enviar",
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton) {
						JSONArray jsonCoordenadas = new JSONArray();
						JSONObject jsonMensagem = new JSONObject();
						for (int i : indiceCoordenadas) {
							jsonCoordenadas.put(i);
						}
						try {
							jsonMensagem.put("pontos", jsonCoordenadas);
							jsonMensagem.put("placa", inputPlaca
									.getText().toString());
							jsonMensagem.put("lugares", Integer
									.parseInt(inputLugares.getText()
											.toString()));
							Bundle b = new Bundle();
							b.putString("coordenadas",
									jsonMensagem.toString());
							sendMessageToService(
									SocketService.DAR_CARONA, 0, b);
							mensagemEnviada = true;

							LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
							for (int i = 0; i < indiceCoordenadas
									.size(); i++) {
								PontoMapa ponto = coordenadasMapa
										.getCoordenadas().get(
												indiceCoordenadas
												.get(i));
								LocationChangedListener lcl = new LocationChangedListener(
										ponto.getCoordenada().latitude,
										ponto.getCoordenada().longitude,
										i);
								locationManager.requestLocationUpdates(
										LocationManager.GPS_PROVIDER,
										DISTANCIA_MINIMA, TEMPO_MINIMO,
										lcl);

							}

						} catch (JSONException e) {
						}
					}
				});
				alert.create().show();
			}
			break;

		case R.id.voltar:
		default:
			this.finish();
			break;
		}
	}

	public void sendMessageToService(int what, int arg, Bundle b) {
		if (mIsBound) {
			if (mService != null) {
				try {
					Message msg = Message.obtain(null, what, arg, 0);
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
		setContentView(R.layout.dar_carona);
		doBindService();

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		for (PontoMapa ponto : coordenadasMapa.getCoordenadas()) {
			adicionarAoMapa(map, ponto.getCoordenada(), ponto.getTitulo(),
					ponto.getDescricao(), ponto.getCor());
		}

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HIST, 25));

		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		indiceCoordenadas = new ArrayList<Integer>();
		try {
			doUnbindService();
		} catch (Exception e) {
		}

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

	public class LocationChangedListener implements LocationListener {

		private Location localizacao;
		private int indice;
		private boolean enviado = false;

		public LocationChangedListener(double latitude, double longitude,
				int indice) {
			localizacao = new Location("");
			localizacao.setLatitude(latitude);
			localizacao.setLongitude(longitude);

			this.indice = indice;
		}

		@Override
		public void onLocationChanged(Location minhaLocalizacao) {
			float distance = minhaLocalizacao.distanceTo(localizacao);
			Log.e(indice + "", localizacao.toString());
			System.out.println(distance);

			if (distance < Consts.distanciaNotificacao && !enviado) {
				JSONObject jsonMensagem = new JSONObject();
				try {
					jsonMensagem.put("proximo", indice);
					enviado = true;
				} catch (JSONException e) {

				}
				System.out.println(jsonMensagem.toString());
				Bundle b = new Bundle();
				b.putString("proximo", jsonMensagem.toString());
				sendMessageToService(SocketService.DAR_CARONA, 1, b);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	}

}
