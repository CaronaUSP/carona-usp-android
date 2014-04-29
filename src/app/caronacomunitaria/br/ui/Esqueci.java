package app.caronacomunitaria.br.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import app.caronacomunitaria.br.R;

public class Esqueci extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.esqueci);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voltar2:
			finish();
			break;

		}
	}
}
