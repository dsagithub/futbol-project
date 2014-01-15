package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Club;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;

public class ClubDetail extends Activity{
	public final static String Tag = ClubDetail.class.toString();

	private FutbolAPI api;
	private String id;
	Button button;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.club_detail_layout);
		api = new FutbolAPI();
		URL url = null;
		try {
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		Club club = api.getClub(url);
		id = club.getIdClub();
		(new FetchClubTask()).execute(url);
	}
	
	public void clicknoticias(View v) {
 
		startNoticiasActivity();
	}
 
	private void startNoticiasActivity() {
		Intent intent = new Intent(this, NoticiasActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
		finish();
	}
	
	public void clickequipos(View v) {
		 
		startEquiposActivity();
	}
 
	private void startEquiposActivity() {
		Intent intent = new Intent(this, EquiposActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
		finish();
	}
	
	private void loadClub(Club club) {
		TextView tvDetailNombre = (TextView) findViewById(R.id.tvDetailNombre);
	 
		tvDetailNombre.setText(club.getNombre());
	}
	
	private class FetchClubTask extends AsyncTask<URL, Void, Club> {
		private ProgressDialog pd;
	 
		@Override
		protected Club doInBackground(URL... params) {
			Club club = api.getClub(params[0]);
			return club;
		}
	 
		@Override
		protected void onPostExecute(Club result) {
			loadClub(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ClubDetail.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
	
}


