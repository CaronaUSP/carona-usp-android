package app.caronacomunitaria.br.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.caronacomunitaria.br.R;
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
import android.util.Log;
import android.view.View;

public class Mapa extends Activity {

	GoogleMap map;
	ArrayList<PontoMapa> p = new ArrayList<PontoMapa>();
	private final LatLng HIST = new LatLng(-23.564056, -46.722538);
	JSONArray jsonCoordenadas = new JSONArray();
	ArrayList<String> titulos = new ArrayList<String>();
	private ArrayList<Integer> coordenadas = new ArrayList<Integer>();

	final Messenger mMessenger = new Messenger(new IncomingHandler());
	Messenger mService = null;
	private boolean mIsBound = false;

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SocketService.ERRO:

				Log.e("erro", "erro");
				break;

			case SocketService.OK:
				Log.e("erro", "OK");

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
				Message msg = Message.obtain(null,
						SocketService.REGISTRAR_CLIENTE);
				msg.replyTo = mMessenger;
				mService.send(msg);

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected - process crashed.
			mService = null;
		}
	};

	private OnMarkerClickListener markerClickListener = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker m) {
			m.setIcon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

			int indice = titulos.indexOf(m.getTitle());

			if (coordenadas.contains(Integer.valueOf(indice))) {
				coordenadas.remove(Integer.valueOf(indice));
				m.setIcon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				// FIXME arrumar a cor

			} else {
				coordenadas.add(Integer.valueOf(indice));
				m.setIcon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
			}

			return false;
		}

	};

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.confirmar:
			JSONArray jsonCoordenadas = new JSONArray();
			for (int i : coordenadas) {
				jsonCoordenadas.put(i);
			}
			JSONObject jsonMensagem = new JSONObject();
			try {
				jsonMensagem.put("pontos", jsonCoordenadas);
			} 
			catch (JSONException e) {
			}

			Bundle b = new Bundle();
			b.putString("coordenadas", jsonMensagem.toString());
			sendMessageToService(SocketService.DAR_CARONA, b);

			break;

		case R.id.voltar:
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
		setContentView(R.layout.mapa);
		doBindService();

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		adicionarcoordenadas();

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HIST, 25));

		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	}
	
	protected void onDestroy() {
		super.onDestroy();
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

	private void adicionarcoordenadas() {

		LatLng METRO = new LatLng(-23.571611, -46.708702);
		LatLng AFRANIO = new LatLng(-23.566723, -46.709076);
		LatLng ED_FISICA = new LatLng(-23.563593, -46.712789);
		LatLng CPTM_IDA = new LatLng(-23.560494, -46.713150);
		LatLng RAIA = new LatLng(-23.556244, -46.721699);
		LatLng PSICO = new LatLng(-23.554483, -46.724746);
		LatLng P2 = new LatLng(-23.552742, -46.728168);
		LatLng TERMINAL = new LatLng(-23.552369, -46.731827);
		LatLng IPT = new LatLng(-23.555781, -46.733640);
		LatLng ELETROTECNICA = new LatLng(-23.557984, -46.732685);
		LatLng FAU = new LatLng(-23.559351, -46.729890);
		LatLng GEOCIENCIAS = new LatLng(-23.560652, -46.727188);
		LatLng LETRAS = new LatLng(-23.562060, -46.724405);
		LatLng HIST = new LatLng(-23.564056, -46.722538);
		LatLng FARMA = new LatLng(-23.565305, -46.724916);
		LatLng BIBLIO_FARMA = new LatLng(-23.566495, -46.727041);
		LatLng LAGO = new LatLng(-23.567493, -46.728938);
		LatLng P3 = new LatLng(-23.568266, -46.740530);
		LatLng INDIANA = new LatLng(-23.568478, -46.731642);
		LatLng BIOMED = new LatLng(-23.568267, -46.731921);
		LatLng IPEN = new LatLng(-23.566071, -46.738497);
		LatLng COPESP = new LatLng(-23.563839, -46.740760);
		LatLng RIO_PEQUENO = new LatLng(-23.559836, -46.741823);
		LatLng PREFEITURA = new LatLng(-23.559807, -46.739419);
		LatLng FISICA = new LatLng(-23.559679, -46.734838);
		LatLng OCEANOGRAFIA = new LatLng(-23.560909, -46.730647);
		LatLng BIOCIENCIAS = new LatLng(-23.566158, -46.730417);
		LatLng CEPAN = new LatLng(-23.565932, -46.725621);
		LatLng BUTANTA = new LatLng(-23.564135, -46.722215);
		LatLng CULTURA_JAP = new LatLng(-23.562925, -46.719747);
		LatLng PACO = new LatLng(-23.563613, -46.716282);
		LatLng ACAD_POLICIA = new LatLng(-23.565447, -46.713335);
		LatLng VITAL = new LatLng(-23.570971, -46.709612);

		
		LatLng AFRANIO2 = new LatLng(-23.566723, -46.709076);
		LatLng EDUCACAO = new LatLng(-23.563284, -46.716054);
		LatLng REITORIA = new LatLng(-23.560501, -46.720185);
		LatLng BIBLIOTECA = new LatLng(-23.562222, -46.723296);
		LatLng BANCOS = new LatLng(-23.560125, -46.727266);
		LatLng FEA = new LatLng(-23.558945, -46.729841);
		LatLng BIENIO = new LatLng(-23.557834, -46.732287);
		LatLng PREFEITURA2 = new LatLng(-23.558965, -46.737780);
		LatLng MAE = new LatLng(-23.559719, -46.742198);
		LatLng HU = new LatLng(-23.563770, -46.741414);
		LatLng BIOMEDICAS_III = new LatLng(-23.564724, -46.739687);
		LatLng ODONTO = new LatLng(-23.566681, -46.738400);
		LatLng P3_2 = new LatLng(-23.568266, -46.740530);
		LatLng INDIANA2 = new LatLng(-23.568478, -46.731642);
		LatLng BIOCIENCIAS2 = new LatLng(-23.566158, -46.730417);
		LatLng FILOSOFIA = new LatLng(-23.561403, -46.730078);
		LatLng FAU2 = new LatLng(-23.560677, -46.730986);
		LatLng IAG = new LatLng(-23.559635, -46.734516);
		LatLng CLUBE_FUNCIONARIO = new LatLng(-23.559487, -46.737681);
		LatLng CIVIL = new LatLng(-23.555761, -46.733609);
		LatLng METALURGIA = new LatLng(-23.553037, -46.732014);
		LatLng MECANICA = new LatLng(-23.552846, -46.728735);
		LatLng HIDRAULICA = new LatLng(-23.556157, -46.726295);
		LatLng BARRACOES = new LatLng(-23.557837, -46.727252);
		LatLng ECA = new LatLng(-23.557830, -46.726905);
		LatLng PSICO_I = new LatLng(-23.556093, -46.725955);
		LatLng RELOGIO = new LatLng(-23.555307, -46.724033);
		LatLng CRUSP = new LatLng(-23.557279, -46.719972);
		LatLng CPTM = new LatLng(-23.560870, -46.713285);
		LatLng ED_FISICA2 = new LatLng(-23.563706, -46.713066);
		LatLng ACAD_POLICIA2 = new LatLng(-23.565447, -46.713335);
		LatLng VITAL2 = new LatLng(-23.570971, -46.709612);

		p.add(new PontoMapa(METRO, "METRO", "Metro Butanta",
				BitmapDescriptorFactory.HUE_BLUE));
		p.add(new PontoMapa(AFRANIO, "AFRANIO", "Av. Afranio Peixoto",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(ED_FISICA, "ED_FISICA", "Educacao F?sica",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(CPTM_IDA, "CPTM_IDA", "CPTM",
				BitmapDescriptorFactory.HUE_BLUE));
		p.add(new PontoMapa(RAIA, "RAIA", "Raia",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(PSICO, "PSICO", "Psicologia",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(P2, "P2", "P2", BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(TERMINAL, "TERMINAL", "Terminal USP",
				BitmapDescriptorFactory.HUE_BLUE));
		p.add(new PontoMapa(IPT, "IPT", "Instituto de Pesquisas Tecnol?gicas",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(ELETROTECNICA, "ELETROTECNICA", "eletrot?cnica",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(FAU, "FAU", "Faculdade de Arquitetura e Urbanismo",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(GEOCIENCIAS, "GEOCIENCIAS", "Geociencias",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(LETRAS, "LETRAS", "Letras",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(HIST, "HIST", "Historia e Geografia",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(FARMA, "FARMA", "Farmacia e Qu?mica",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(BIBLIO_FARMA, "BIBLIO_FARMA",
				"Biblioteca Farmacia e Qu?mica",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(LAGO, "LAGO", "Rua do Lago",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(P3, "P3", "P3", BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(INDIANA2, "INDIANA", "Vila Indiana",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(BIOMED, "BIOMED", "Biom?dicas",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(IPEN, "IPEN",
				"Instituto de Pesquisas Energ?ticas e Nucleares",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(COPESP, "COPESP", "COPESP",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(RIO_PEQUENO, "RIO_PEQUENO", "Rio Pequeno",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(PREFEITURA, "PREFEITURA", "Prefeitura",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(FISICA, "FISICA", "Instituto de Física",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(OCEANOGRAFIA, "OCEANOGRAFIA", "Oceanografia",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(BIOCIENCIAS, "BIOCIENCIAS", "Biociencias",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(CEPAN, "CEPAN",
				"Centro de Estudos e Pesquisas de Administra??o Municipal",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(BUTANTA, "BUTANTA", "Instituto Butantã",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(CULTURA_JAP, "CULTURA_JAP", "Cultura Japonesa",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(PACO, "PACO", "Paço das Artes",
				BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(ACAD_POLICIA, "ACAD_POLICIA",
				"Academia de Policia", BitmapDescriptorFactory.HUE_RED));
		p.add(new PontoMapa(VITAL, "VITAL", "Av. Vital Brasil",
				BitmapDescriptorFactory.HUE_RED));

		p.add(new PontoMapa(AFRANIO2, "AFRANIO", "Av. Afranio Peixoto",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(EDUCACAO, "EDUCACAO", "Faculdade de Educa??o",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(REITORIA, "REITORIA", "Reitoria",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(BIBLIOTECA, "BIBLIOTECA", "Biblioteca",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(BANCOS, "BANCOS", "Bancos",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(FEA, "FEA",
				"Faculdade de Economia e Administra??o",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(BIENIO, "BIENIO", "Poli - Bienio",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(PREFEITURA2, "PREFEITURA", "Prefeitura",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(MAE, "MAE", "Museu de Arqueologia e Etnologia",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(HU, "HU", "Hospital Universit?rio",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(BIOMEDICAS_III, "BIOMEDICAS_III", "Biomedicas",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(ODONTO, "ODONTO", "Faculdade de Odontologia",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(P3_2, "P3", "P3", BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(INDIANA, "INDIANA", "Vila Indiana",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(BIOCIENCIAS2, "BIOCIENCIAS", "Biociencias",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(FILOSOFIA, "FILOSOFIA", "Filosofia",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(FAU2, "FAU",
				"Faculdade de Arquitetura e Urbanismo",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(IAG, "IAG", "Instituto de Astronomia e Geofisica",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(CLUBE_FUNCIONARIO, "CLUBE_FUNCIONARIO",
				"Clube dos Funcionarios", BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(CIVIL, "CIVIL", "Poli - Civil",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(METALURGIA, "METALURGIA", "Poli - Metalurgia",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(MECANICA, "MECANICA", "Poli - Mecanica",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(HIDRAULICA, "HIDRAULICA", "Poli - Hidraulica",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(BARRACOES, "BARRACOES", "Barrac?es",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(ECA, "ECA", "Escola de Comunica??es e Artes",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(PSICO_I, "PSICO_I", "Psicologia",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(RELOGIO, "RELOGIO", "Pra?a do Relogio",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(CRUSP, "CRUSP", "CRUSP",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(CPTM, "CPTMVOLTA", "CPTM",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(ED_FISICA2, "ED_FISICA", "Educa??o Fisica",
				BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(ACAD_POLICIA2, "ACAD_POLICIA",
				"Academia de Pol?cia", BitmapDescriptorFactory.HUE_GREEN));
		p.add(new PontoMapa(VITAL2, "VITAL", "Av. Vital Brasil",
				BitmapDescriptorFactory.HUE_GREEN));

		for (PontoMapa ponto : p) {
			adicionarAoMapa(map, ponto.getCoordenada(), ponto.getTitulo(),
					ponto.getDescricao(), ponto.getCor());
			titulos.add(ponto.getTitulo());
		}

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
