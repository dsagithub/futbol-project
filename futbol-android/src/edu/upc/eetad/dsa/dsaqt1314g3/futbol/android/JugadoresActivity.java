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
import android.view.View;
import android.widget.ListView;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Jugadores;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.JugadoresCollection;

public class JugadoresActivity extends ListActivity {
	private final static String TAG = JugadoresActivity.class.toString();
	private String serverAddress = null;
	private String serverPort = null;
	private FutbolAPI api;
	private ArrayList<Jugadores> jugadorList;
	private JugadorAdapter adapter;
	

 
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		Bundle extra = this.getIntent().getExtras();
		String url2 = null;
		url2 = extra.getString("url2");
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
	 
		setContentView(R.layout.futbol_layout);
		
		jugadorList = new ArrayList<Jugadores>();
		adapter = new JugadorAdapter(this, jugadorList);
		
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
			url = new URL(url2 + "/jugadores?&offset=0&length=20");
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		(new FetchJugadoresTask()).execute(url);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Jugadores jugador = jugadorList.get(position);
		// HATEOAS version
		
		URL url = null;
		try {
			url = new URL(jugador.getLinks().get(0).getUri());
		} catch (MalformedURLException e) {
			return;
		}

		
		Log.d(TAG, url.toString());
		Intent intent = new Intent(this, JugadorDetail.class);
		intent.putExtra("url", url.toString());
		startActivity(intent);
		
		
	}
	
	private void addJugadores(JugadoresCollection jugadores){
		jugadorList.addAll(jugadores.getJugadores());
		adapter.notifyDataSetChanged();
	}
	
	
	private class FetchJugadoresTask extends AsyncTask<URL, Void, JugadoresCollection> {
		private ProgressDialog pd;
	 
		@Override
		protected JugadoresCollection doInBackground(URL... params) {
			JugadoresCollection jugadores = api.getJugadores(params[0]);
			return jugadores;
		}
	
		@Override
		protected void onPostExecute(JugadoresCollection result) {
			addJugadores(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(JugadoresActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		
	 
	}
	
		
	}

