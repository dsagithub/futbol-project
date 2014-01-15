package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Jugadores;

public class JugadorDetail extends Activity{
	public final static String Tag = JugadorDetail.class.toString();

	private FutbolAPI api;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jugador_detail_layout);
		api = new FutbolAPI();
		URL url = null;
		try {
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		(new FetchJugadorTask()).execute(url);
	}
	
	private void loadJugador(Jugadores jugador) {
		TextView tvDetailNombre = (TextView) findViewById(R.id.tvDetailNombre);
		TextView tvDetailApellidos = (TextView) findViewById(R.id.tvDetailApellidos);
		TextView tvDetailDni = (TextView) findViewById(R.id.tvDetailDni);
	 
		tvDetailNombre.setText(jugador.getNombre());
		tvDetailApellidos.setText(jugador.getApellidos());
		tvDetailDni.setText(jugador.getDni());
	}
	
	private class FetchJugadorTask extends AsyncTask<URL, Void, Jugadores> {
		private ProgressDialog pd;
	 
		@Override
		protected Jugadores doInBackground(URL... params) {
			Jugadores jugador = api.getJugador(params[0]);
			return jugador;
		}
	 
		@Override
		protected void onPostExecute(Jugadores result) {
			loadJugador(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(JugadorDetail.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
	
}

