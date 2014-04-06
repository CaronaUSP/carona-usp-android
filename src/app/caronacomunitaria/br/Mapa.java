package app.caronacomunitaria.br;

import app.caronacomunitaria.br.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
	private final LatLng MetroButanta = new LatLng(-23.571595, -46.708722);
	private final LatLng CPTM = new LatLng(-23.558535, -46.710481);
	private final LatLng ECA = new LatLng(-23.557886, -46.726789);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		map.addMarker(new MarkerOptions().position(EP).title("EP")
				.snippet("Escola Politécnica"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(EP, 20));
		Marker metroButanta = map.addMarker(new MarkerOptions().position(
				MetroButanta).title("Metro Butantã"));
		Marker cptm = map.addMarker(new MarkerOptions().position(CPTM).title(
				"CPTM"));
		map.addMarker(new MarkerOptions().position(ECA).title("ECA")
				.snippet("Escola de Comunicações e Artes"));

		cptm.setIcon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		metroButanta.setIcon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

		map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

	}
}
