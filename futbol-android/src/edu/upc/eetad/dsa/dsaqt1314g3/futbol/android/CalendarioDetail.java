package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Calendario;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;

public class CalendarioDetail extends Activity{
	public final static String Tag = CalendarioDetail.class.toString();

	private FutbolAPI api;
	private String url2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendario_detail_layout);
		api = new FutbolAPI();
		URL url = null;
		try {
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		url2 = (String) getIntent().getExtras().get("url");
		(new FetchCalendarioTask()).execute(url);
	}
	
	private void loadCalendario(Calendario calendario) {
		TextView tvDetailJornada = (TextView) findViewById(R.id.tvDetailJornada);
		TextView tvDetailEquipoA = (TextView) findViewById(R.id.tvDetailEquipoA);
		TextView tvDetailEquipoB = (TextView) findViewById(R.id.tvDetailEquipoB);
		TextView tvDetailFecha = (TextView) findViewById(R.id.tvDetailFecha);
		TextView tvDetailHora = (TextView) findViewById(R.id.tvDetailHora);
	 
		tvDetailJornada.setText("Jornada: " + calendario.getJornada());
		tvDetailEquipoA.setText(calendario.getIdEquipoA());
		tvDetailEquipoB.setText(calendario.getIdEquipoB());
		tvDetailFecha.setText(calendario.getFecha());
		tvDetailHora.setText(calendario.getHora());
	}
	
	public void clickcomentarios(View v) {
		 
		startComentariosActivity();
	}
 
	private void startComentariosActivity() {
		Intent intent = new Intent(this, ComentariosActivity.class);
		intent.putExtra("url2", url2);
		startActivity(intent);
		finish();
	}
	
	/*
	public void clickretrans(View v) {
		 
		startRetransActivity();
	}
 
	private void startRetransActivity() {
		Intent intent = new Intent(this, RetransmisionesActivity.class);
		intent.putExtra("url2", url2);
		startActivity(intent);
		finish();
	}
	
	*/
	private class FetchCalendarioTask extends AsyncTask<URL, Void, Calendario> {
		private ProgressDialog pd;
	 
		@Override
		protected Calendario doInBackground(URL... params) {
			Calendario calendario = api.getCalendario(params[0]);
			return calendario;
		}
	 
		@Override
		protected void onPostExecute(Calendario result) {
			loadCalendario(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(CalendarioDetail.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
	
}

