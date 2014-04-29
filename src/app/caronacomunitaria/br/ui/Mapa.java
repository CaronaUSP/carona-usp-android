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

public class Mapa extends Activity {
	GoogleMap map;

	private final LatLng EP = new LatLng(-23.554911, -46.730180);
	private final LatLng METRO_BUTANTA = new LatLng(-23.571595, -46.708722);
	private final LatLng CPTM = new LatLng(-23.558535, -46.710481);
	private final LatLng ECA = new LatLng(-23.557886, -46.726789);
	private final LatLng IME = new LatLng(-23.559321, -46.731498);
	private final LatLng FAU = new LatLng(-23.560058, -46.729945);
	private final LatLng IF = new LatLng(-23.560887, -46.734824);
	private final LatLng FFLCH = new LatLng(-23.56264, -46.724748);

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

	private OnMapClickListener listener2 = new OnMapClickListener() {

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

		adicionarAoMapa(map, EP, "EP", "Escola Politécnica",
				BitmapDescriptorFactory.HUE_RED);
		adicionarAoMapa(map, METRO_BUTANTA, "Metrô", "Metrô Butantã",
				BitmapDescriptorFactory.HUE_BLUE);
		adicionarAoMapa(map, CPTM, "CPTM",
				"CPTM - Estação Cidade Universitária",
				BitmapDescriptorFactory.HUE_BLUE);
		adicionarAoMapa(map, ECA, "ECA", "Escola de Comunicação e Arte",
				BitmapDescriptorFactory.HUE_RED);
		adicionarAoMapa(map, IME, "IME",
				"Instituto de Matemática e Estatística",
				BitmapDescriptorFactory.HUE_RED);
		adicionarAoMapa(map, FAU, "FAU",
				"Faculdade de Arquitetura e Urbanismo",
				BitmapDescriptorFactory.HUE_RED);
		adicionarAoMapa(map, IF, "IF", "Instituto de Física",
				BitmapDescriptorFactory.HUE_RED);
		adicionarAoMapa(map, FFLCH, "FFLCH",
				"Faculdade de Filosofia, Letras e Ciências Humanas",
				BitmapDescriptorFactory.HUE_RED);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(EP, 25));

		map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
	}

	public Marker adicionarAoMapa(GoogleMap g, LatLng lugar, String titulo,
			String descricao, float color) {
		Marker m = g.addMarker(new MarkerOptions().position(lugar)
				.title(titulo).snippet(descricao));
		m.setIcon(BitmapDescriptorFactory.defaultMarker(color));
		g.setOnMarkerClickListener(markerClickListener);
		g.setOnMapClickListener(listener2);
		return m;
	}
}
