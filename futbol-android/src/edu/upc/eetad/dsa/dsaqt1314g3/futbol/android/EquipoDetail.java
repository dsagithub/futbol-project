package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Equipo;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;

public class EquipoDetail extends Activity{
	public final static String Tag = EquipoDetail.class.toString();

	private FutbolAPI api;
	private String url2;
	Button button;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equipo_detail_layout);
		api = new FutbolAPI();
		URL url = null;
		try {
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		url2 = (String) getIntent().getExtras().get("url");
		(new FetchEquipoTask()).execute(url);
	}
	
	public void clickjugadores(View v) {
 
		startJugadoresActivity();
	}
 
	private void startJugadoresActivity() {
		Intent intent = new Intent(this, JugadoresActivity.class);
		intent.putExtra("url2", url2);
		startActivity(intent);
		finish();
	}
	
	private void loadEquipo(Equipo equipo) {
		TextView tvDetailNombre = (TextView) findViewById(R.id.tvDetailNombre);
	 
		tvDetailNombre.setText(equipo.getNombre());
	}
	
	private class FetchEquipoTask extends AsyncTask<URL, Void, Equipo> {
		private ProgressDialog pd;
	 
		@Override
		protected Equipo doInBackground(URL... params) {
			Equipo equipo = api.getEquipo(params[0]);
			return equipo;
		}
	 
		@Override
		protected void onPostExecute(Equipo result) {
			loadEquipo(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(EquipoDetail.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
	
}


