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
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Retransmision;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.RetransmisionCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.User;

public class RetransmisionesActivity extends ListActivity {
	private final static String TAG = RetransmisionesActivity.class.toString();
	private String serverAddress = null;
	private String serverPort = null;
	private FutbolAPI api;
	private ArrayList<Retransmision> retransmisionList;
	private RetransmisionAdapter adapter;
	

 
	/** Called when the activity is first created. */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
					+ "/futbol-api/users/" + username);
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		User user = api.getUser(url);
		if (user.getRole().compareTo("administrator") == 0){
			getMenuInflater().inflate(R.menu.futbol_menu, menu);
			return true;
		}
		else {
			getMenuInflater().inflate(R.menu.futbol_menu4, menu);
			return true;
		}
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miWrite:
			URL url = null;
			Bundle extra = this.getIntent().getExtras();
			String url2 = null;
			url2 = extra.getString("url2");
			try {
				url = new URL(url2 + "/retra");
			} catch (MalformedURLException e) {
				Log.d(TAG, e.getMessage(), e);
			}
			Intent intent = new Intent(this, WriteRetransmision.class);
			intent.putExtra("url", url);
			intent.putExtra("url2", url2);
			startActivity(intent);
			
			return true;

			
		case R.id.miHome:
			finish();
			Intent intent2 = new Intent(this, FutbolMainActivity.class);
			startActivity(intent2);
			return true;
	 
	 
		default:
			return super.onOptionsItemSelected(item);
		}
	 
	}
	
	
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
		
		retransmisionList = new ArrayList<Retransmision>();
		adapter = new RetransmisionAdapter(this, retransmisionList);
		
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
			url = new URL(url2 + "/retra?offset=0&length=20");
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		(new FetchRetransmisionesTask()).execute(url);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Retransmision retransmision = retransmisionList.get(position);
		// HATEOAS version
		
		URL url = null;
		try {
			url = new URL(retransmision.getLinks().get(0).getUri());
		} catch (MalformedURLException e) {
			return;
		}

		
		Log.d(TAG, url.toString());
		Intent intent = new Intent(this, RetransmisionDetail.class);
		intent.putExtra("url", url.toString());
		startActivity(intent);
		
		
	}
	
	private void addRetransmisiones(RetransmisionCollection comentarios){
		retransmisionList.addAll(comentarios.getRetrans());
		adapter.notifyDataSetChanged();
	}
	
	
	private class FetchRetransmisionesTask extends AsyncTask<URL, Void, RetransmisionCollection> {
		private ProgressDialog pd;
	 
		@Override
		protected RetransmisionCollection doInBackground(URL... params) {
			RetransmisionCollection retransmisiones = api.getRetransmisiones(params[0]);
			return retransmisiones;
		}
	
		@Override
		protected void onPostExecute(RetransmisionCollection result) {
			addRetransmisiones(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(RetransmisionesActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		
	 
	}
	
		
	}
