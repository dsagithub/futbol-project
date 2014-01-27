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
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Calendario;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.CalendarioCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;

public class CalendariosActivity extends ListActivity {
	private final static String TAG = CalendariosActivity.class.toString();
	private String serverAddress = null;
	private String serverPort = null;
	private FutbolAPI api;
	private ArrayList<Calendario> calendarioList;
	private CalendarioAdapter adapter;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.futbol_menu4, menu);
		return true;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miHome:
			finish();
			Intent intent = new Intent(this, FutbolMainActivity.class);
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
	 
		setContentView(R.layout.futbol_layout);
		
		calendarioList = new ArrayList<Calendario>();
		adapter = new CalendarioAdapter(this, calendarioList);
		
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
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		(new FetchCalendariosTask()).execute(url);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Calendario calendario = calendarioList.get(position);
		// HATEOAS version
		/*
		URL url = null;
		try {
			url = new URL(calendario.getLinks().get(0).getUri());
		} catch (MalformedURLException e) {
			return;
		}
		*/
		// No HATEOAS
				URL url = null;
				 try {
				 url = new URL("http://" + serverAddress + ":" + serverPort
				 + "/futbol-api/campeonato/2/calendario/" + calendario.getIdPartido());
				 } catch (MalformedURLException e) {
				 return;
				 }


		
		Log.d(TAG, url.toString());
		Intent intent = new Intent(this, CalendarioDetail.class);
		intent.putExtra("url", url.toString());
		startActivity(intent);
		
		
	}
	
	private void addCalendarios(CalendarioCollection calendarios){
		calendarioList.addAll(calendarios.getCalendarios());
		adapter.notifyDataSetChanged();
	}
	
	
	private class FetchCalendariosTask extends AsyncTask<URL, Void, CalendarioCollection> {
		private ProgressDialog pd;
	 
		@Override
		protected CalendarioCollection doInBackground(URL... params) {
			CalendarioCollection calendarios = api.getCalendarios(params[0]);
			return calendarios;
		}
	
		@Override
		protected void onPostExecute(CalendarioCollection result) {
			addCalendarios(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(CalendariosActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		
	 
	}
	
		
	}


