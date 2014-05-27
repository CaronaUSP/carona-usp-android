package app.caronacomunitaria.br.ui;

import java.util.ArrayList;

import app.caronacomunitaria.br.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Mapa extends Activity {
	GoogleMap map;

	//CIRCULAR 2
	/*FIXME 
	COLOCAR NUM VETOR
	*/
	private final LatLng METRO = new LatLng (-23.571611, -46.708702);
	private final LatLng AFRANIO = new LatLng (-23.566723, -46.709076);
	private final LatLng ED_FISICA = new LatLng (-23.563593, -46.712789);
	private final LatLng CPTM_IDA = new LatLng (-23.560494, -46.713150);
	private final LatLng RAIA = new LatLng (-23.556244, -46.721699);
	private final LatLng PSICO = new LatLng (-23.554483, -46.724746);
	private final LatLng P2 = new LatLng (-23.552742, -46.728168);
	private final LatLng TERMINAL = new LatLng (-23.552369, -46.731827);
	private final LatLng IPT = new LatLng (-23.555781, -46.733640);
	private final LatLng ELETROTECNICA = new LatLng (-23.557984, -46.732685);
	private final LatLng FAU = new LatLng (-23.559351, -46.729890);
	private final LatLng GEOCIENCIAS = new LatLng (-23.560652, -46.727188);
	private final LatLng LETRAS = new LatLng (-23.562060, -46.724405);
	private final LatLng HIST = new LatLng (-23.564056, -46.722538);
	private final LatLng FARMA = new LatLng (-23.565305, -46.724916);
	private final LatLng BIBLIO_FARMA  = new LatLng (-23.566495, -46.727041);
	private final LatLng LAGO = new LatLng (-23.567493, -46.728938);
	private final LatLng P3 = new LatLng (-23.568266, -46.740530);
	private final LatLng INDIANA = new LatLng (-23.568478, -46.731642);
	private final LatLng BIOMED = new LatLng (-23.568267, -46.731921);
	private final LatLng IPEN = new LatLng (-23.566071, -46.738497);
	private final LatLng COPESP = new LatLng (-23.563839, -46.740760);
	private final LatLng RIO_PEQUENO = new LatLng (-23.559836, -46.741823);
	private final LatLng PREFEITURA = new LatLng (-23.559807, -46.739419);
	private final LatLng FISICA = new LatLng (-23.559679, -46.734838);
	private final LatLng OCEANOGRAFIA = new LatLng (-23.560909, -46.730647);
	private final LatLng BIOCIENCIAS = new LatLng (-23.566158, -46.730417);
	private final LatLng CEPAN = new LatLng (-23.565932, -46.725621);
	private final LatLng BUTANTA = new LatLng (-23.564135, -46.722215);
	private final LatLng CULTURA_JAP = new LatLng (-23.562925, -46.719747);
	private final LatLng PACO = new LatLng (-23.563613, -46.716282);
	private final LatLng ACAD_POLICIA = new LatLng (-23.565447, -46.713335);
	private final LatLng VITAL = new LatLng (-23.570971, -46.709612);

	
	//CIRCULAR 1
	/*FIXME 
	COLOCAR NUM VETOR
	*/
	private final LatLng METRO2 = new LatLng (-23.571611, -46.708702);
	private final LatLng AFRANIO2 = new LatLng (-23.566723, -46.709076);
	private final LatLng EDUCACAO = new LatLng (-23.563284, -46.716054);
	private final LatLng REITORIA = new LatLng (-23.560501, -46.720185);
	private final LatLng BIBLIOTECA = new LatLng (-23.562222, -46.723296);
	private final LatLng BANCOS = new LatLng (-23.560125, -46.727266);
	private final LatLng FEA = new LatLng (-23.558945, -46.729841);
	private final LatLng BIENIO = new LatLng (-23.557834, -46.732287);
	private final LatLng PREFEITURA2 = new LatLng (-23.558965, -46.737780);
	private final LatLng MAE = new LatLng (-23.559719, -46.742198);
	private final LatLng HU = new LatLng (-23.563770, -46.741414);
	private final LatLng BIOMEDICAS_III = new LatLng (-23.564724, -46.739687);
	private final LatLng ODONTO = new LatLng (-23.566681, -46.738400);
	private final LatLng P3_2 = new LatLng (-23.568266, -46.740530);
	private final LatLng INDIANA2 = new LatLng (-23.568478, -46.731642);
	private final LatLng BIOCIENCIAS2 = new LatLng (-23.566158, -46.730417);
	private final LatLng FILOSOFIA = new LatLng (-23.561403, -46.730078);
	private final LatLng FAU2 = new LatLng (-23.560677, -46.730986);
	private final LatLng IAG = new LatLng (-23.559635, -46.734516);
	private final LatLng CLUBE_FUNCIONARIO = new LatLng (-23.559487, -46.737681);
	private final LatLng CIVIL = new LatLng (-23.555761, -46.733609);
	private final LatLng METALURGIA = new LatLng (-23.553037, -46.732014);
	private final LatLng MECANICA = new LatLng (-23.552846, -46.728735);
	private final LatLng HIDRAULICA = new LatLng (-23.556157, -46.726295);
	private final LatLng BARRACOES = new LatLng (-23.557837, -46.727252);
	private final LatLng ECA = new LatLng (-23.557830, -46.726905);
	private final LatLng PSICO_I = new LatLng (-23.556093, -46.725955);
	private final LatLng RELOGIO = new LatLng (-23.555307, -46.724033);
	private final LatLng CRUSP = new LatLng (-23.557279, -46.719972);
	private final LatLng CPTM = new LatLng (-23.560870, -46.713285);
	private final LatLng ED_FISICA2 = new LatLng (-23.563706, -46.713066);
	private final LatLng ACAD_POLICIA2 = new LatLng (-23.565447, -46.713335);
	private final LatLng VITAL2 = new LatLng (-23.570971, -46.709612);


	
	
	
	

	private ArrayList<LatLng> coordenadas = new ArrayList<LatLng>();

	private OnMarkerClickListener markerClickListener = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker m) {
			m.setIcon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

			LatLng position = m.getPosition();

			if (coordenadas.contains(position)) {
				coordenadas.remove(position);
			} else {
				coordenadas.add(position);
			}

			return false;
		}
	};

	private OnMapClickListener mapListener = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		adicionarcoordenadas();
		
		
		
		

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(HIST, 25));

		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	}

	private void adicionarcoordenadas() {
		adicionarAoMapa(map, METRO, "METRO", "Metro Butanta",
				BitmapDescriptorFactory.HUE_BLUE);
				adicionarAoMapa(map, AFRANIO, "AFRANIO", "Av. Afranio Peixoto",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, ED_FISICA, "ED_FISICA", "Educacao F?sica",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, CPTM_IDA, "CPTM_IDA", "CPTM",
				BitmapDescriptorFactory.HUE_BLUE);
				adicionarAoMapa(map, RAIA, "RAIA", "Raia",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, PSICO, "PSICO", "Psicologia",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, P2, "P2", "P2",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, TERMINAL, "TERMINAL", "Terminal USP",
				BitmapDescriptorFactory.HUE_BLUE);
				adicionarAoMapa(map, IPT, "IPT", "Instituto de Pesquisas Tecnol?gicas",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, ELETROTECNICA, "ELETROTECNICA", "eletrot?cnica",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, FAU, "FAU", "Faculdade de Arquitetura e Urbanismo",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, GEOCIENCIAS, "GEOCIENCIAS", "Geociencias",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, LETRAS, "LETRAS", "Letras",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, HIST, "HIST", "Historia e Geografia",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, FARMA, "FARMA", "Farmacia e Qu?mica",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, BIBLIO_FARMA, "BIBLIO_FARMA", "Biblioteca Farmacia e Qu?mica",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, LAGO, "LAGO", "Rua do Lago",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, P3, "P3", "P3",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, INDIANA2, "INDIANA", "Vila Indiana",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, BIOMED, "BIOMED", "Biom?dicas",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, IPEN, "IPEN", "Instituto de Pesquisas Energ?ticas e Nucleares",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, COPESP, "COPESP", "COPESP",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, RIO_PEQUENO, "RIO_PEQUENO", "Rio Pequeno",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, PREFEITURA, "PREFEITURA", "Prefeitura",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, FISICA, "FISICA", "Instituto de Física",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, OCEANOGRAFIA, "OCEANOGRAFIA", "Oceanografia",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, BIOCIENCIAS, "BIOCIENCIAS", "Biociencias",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, CEPAN, "CEPAN", "Centro de Estudos e Pesquisas de Administra??o Municipal",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, BUTANTA, "BUTANTA", "Instituto Butantã",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, CULTURA_JAP, "CULTURA_JAP", "Cultura Japonesa",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, PACO, "PACO", "Paço das Artes",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, ACAD_POLICIA, "ACAD_POLICIA", "Academia de Policia",
				BitmapDescriptorFactory.HUE_RED);
				adicionarAoMapa(map, VITAL, "VITAL", "Av. Vital Brasil",
				BitmapDescriptorFactory.HUE_RED);

				
				
				adicionarAoMapa(map, METRO2, "METRO", "Metro Butant?",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, AFRANIO2 , "AFRANIO", "Av. Afranio Peixoto",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, EDUCACAO , "EDUCACAO", "Faculdade de Educa??o",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, REITORIA, "REITORIA", "Reitoria",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, BIBLIOTECA, "BIBLIOTECA", "Biblioteca",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, BANCOS  , "BANCOS", "Bancos",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, FEA , "FEA", "Faculdade de Economia e Administra??o",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, BIENIO, "BIENIO", "Poli - Bienio",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, PREFEITURA2, "PREFEITURA", "Prefeitura",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, MAE, "MAE", "Museu de Arqueologia e Etnologia",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, HU , "HU", "Hospital Universit?rio",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, BIOMEDICAS_III, "BIOMEDICAS_III", "Biomedicas",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, ODONTO , "ODONTO", "Faculdade de Odontologia",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, P3_2 , "P3", "P3",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, INDIANA , "INDIANA", "Vila Indiana",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, BIOCIENCIAS2 , "BIOCIENCIAS", "Biociencias",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, FILOSOFIA, "FILOSOFIA", "Filosofia",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, FAU2 , "FAU", "Faculdade de Arquitetura e Urbanismo",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, IAG , "IAG", "Instituto de Astronomia e Geofisica",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, CLUBE_FUNCIONARIO, "CLUBE_FUNCIONARIO", "Clube dos Funcionarios",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, CIVIL, "CIVIL", "Poli - Civil",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, METALURGIA, "METALURGIA", "Poli - Metalurgia",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, MECANICA, "MECANICA", "Poli - Mecanica",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, HIDRAULICA, "HIDRAULICA", "Poli - Hidraulica",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, BARRACOES, "BARRACOES", "Barrac?es",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, ECA, "ECA", "Escola de Comunica??es e Artes",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, PSICO_I, "PSICO_I", "Psicologia",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, RELOGIO, "RELOGIO", "Pra?a do Relogio",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, CRUSP, "CRUSP", "CRUSP",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, CPTM, "CPTMVOLTA", "CPTM",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, ED_FISICA2, "ED_FISICA", "Educa??o Fisica",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, ACAD_POLICIA2, "ACAD_POLICIA", "Academia de Pol?cia",
						BitmapDescriptorFactory.HUE_GREEN);
						adicionarAoMapa(map, VITAL2, "VITAL", "Av. Vital Brasil",
						BitmapDescriptorFactory.HUE_GREEN);
		
	}

	public Marker adicionarAoMapa(GoogleMap g, LatLng lugar, String titulo,
			String descricao, float color) {
		Marker m = g.addMarker(new MarkerOptions().position(lugar)
				.title(titulo).snippet(descricao));
		m.setIcon(BitmapDescriptorFactory.defaultMarker(color));
		g.setOnMarkerClickListener(markerClickListener);
		g.setOnMapClickListener(mapListener);
		return m;
	}
	
	public void onClick(View v)
	{
		switch (v.getId()) {
		case R.id.confirmar:
			//TODO
			/*
			 * ENVIAR COORDENADAS AO SERVER
			 * */
			break;

		default:
			break;
		}
		
		
		
	}
}
