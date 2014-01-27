package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Campeonatos;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.CampeonatosCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;

public class CampeonatosActivity extends ListActivity {
	private final static String TAG = CampeonatosActivity.class.toString();
	private String serverAddress = null;
	private String serverPort = null;
	private FutbolAPI api;
	private ArrayList<Campeonatos> campeonatoList;
	private CampeonatoAdapter adapter;
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.futbol_menu3, menu);
		return true;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miExit:
			finish();
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
			return true;
	 
		default:
			return super.onOptionsItemSelected(item);
		}
	 
	}
 
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		AssetManager assetManager = getAssets();
		Properties config = new Properties();
		try {
			config.load(assetManager.open("config.properties"));
			serverAddress = config.getProperty("server.address");
			serverPort = config.getProperty("server.port");
	 
			Log.d(TAG, "Configured server " + serverAddress + ":" + serverPort);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			finish();
		}
	 
		setContentView(R.layout.futbol_layout3);
		
		campeonatoList = new ArrayList<Campeonatos>();
		adapter = new CampeonatoAdapter(this, campeonatoList);
		
		setListAdapter(adapter);
	 
		SharedPreferences prefs = getSharedPreferences("futbol-profile", Context.MODE_PRIVATE);
		final String username = prefs.getString("username", null);
		final String password = prefs.getString("password", null);
	 
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password
						.toCharArray());
			}
		});
		Log.d(TAG, "authenticated with " + username + ":" + password);

		api = new FutbolAPI();
		URL url = null;
		try {
			url = new URL("http://" + serverAddress + ":" + serverPort
					+ "/futbol-api/campeonato?&offset=0&length=20");
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		(new FetchCampeonatosTask()).execute(url);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Campeonatos campeonato = campeonatoList.get(position);
		// HATEOAS version
		
		URL url = null;
		try {
			url = new URL(campeonato.getLinks().get(0).getUri());
		} catch (MalformedURLException e) {
			return;
		}

		
		Log.d(TAG, url.toString());
		Intent intent = new Intent(this, CalendariosActivity.class);
		intent.putExtra("url", url.toString());
		startActivity(intent);
		
		
	}
	
	private void addCampeonatos(CampeonatosCollection campeonatos){
		campeonatoList.addAll(campeonatos.getCampeonatos());
		adapter.notifyDataSetChanged();
	}
	
	public void clickclubs(View v) {
		 
		startClubsActivity();
	}
 
	private void startClubsActivity() {
		Intent intent = new Intent(this, FutbolMainActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	private class FetchCampeonatosTask extends AsyncTask<URL, Void, CampeonatosCollection> {
		private ProgressDialog pd;
	 
		@Override
		protected CampeonatosCollection doInBackground(URL... params) {
			CampeonatosCollection campeonatos = api.getCampeonatos(params[0]);
			return campeonatos;
		}
	
		@Override
		protected void onPostExecute(CampeonatosCollection result) {
			addCampeonatos(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(CampeonatosActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		
	 
	}
	
		
	}