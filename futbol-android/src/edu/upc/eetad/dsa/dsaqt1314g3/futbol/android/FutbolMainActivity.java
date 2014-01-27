package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Club;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.ClubCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.User;
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

public class FutbolMainActivity extends ListActivity {
	private final static String TAG = FutbolMainActivity.class.toString();
	private String serverAddress = null;
	private String serverPort = null;
	private FutbolAPI api;
	private ArrayList<Club> clubList;
	private ClubAdapter adapter;
 
	/** Called when the activity is first created. */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.futbol_menu3, menu);
		return true;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miExit:
			FutbolMainActivity.this.finish();
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
			return true;
	 
		default:
			return super.onOptionsItemSelected(item);
		}
	 
	}
	
	
	
	
	
	
	
	
	
	
	
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
	 
		setContentView(R.layout.futbol_layout2);

		clubList = new ArrayList<Club>();
		adapter = new ClubAdapter(this, clubList);
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
					+ "/futbol-api/club?&offset=0&length=20");
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		(new FetchClubsTask()).execute(url);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Club club = clubList.get(position);
		// HATEOAS version
		/*
		URL url = null;
		try {
			url = new URL(club.getLinks().get(0).getUri());
		} catch (MalformedURLException e) {
			return;
		}*/
		
		// No HATEOAS
		URL url = null;
		 try {
		 url = new URL("http://" + serverAddress + ":" + serverPort
		 + "/futbol-api/club/" + id);
		 } catch (MalformedURLException e) {
		 return;
		 }
		
		Log.d(TAG, url.toString());
		Intent intent = new Intent(this, ClubDetail.class);
		intent.putExtra("url", url.toString());
		startActivity(intent);
		
		
	}
	
	private void addClubs(ClubCollection clubs){
		clubList.addAll(clubs.getClubs());
		adapter.notifyDataSetChanged();
	}
	
	public void clickcampeonatos(View v) {
		 
		startCampeonatosActivity();
	}
 
	private void startCampeonatosActivity() {
		Intent intent = new Intent(this, CampeonatosActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	private class FetchClubsTask extends AsyncTask<URL, Void, ClubCollection> {
		private ProgressDialog pd;
	 
		@Override
		protected ClubCollection doInBackground(URL... params) {
			ClubCollection clubs = api.getClubs(params[0]);
			return clubs;
		}
	
		@Override
		protected void onPostExecute(ClubCollection result) {
			addClubs(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(FutbolMainActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		
	 
	}
	
		
	}
