package app.caronacomunitaria.br;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import app.caronacomunitaria.br.R;

public class Main extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.dar_carona:
			Intent it = new Intent(this, Mapa.class);			
			startActivity(it);
			
		case R.id.receber_carona:
		
		
		}
		
		
	}
}
