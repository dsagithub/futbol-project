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
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Noticia;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.NoticiasCollection;

public class NoticiasActivity extends ListActivity {
	private final static String TAG = NoticiasActivity.class.toString();
	private String serverAddress = null;
	private String serverPort = null;
	private FutbolAPI api;
	private ArrayList<Noticia> noticiaList;
	private NoticiaAdapter adapter;
	

 
	/** Called when the activity is first created. */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.futbol_menu, menu);
		return true;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miWrite:
			URL url = null;
			Bundle extra = this.getIntent().getExtras();
			String id = "1";
			id = extra.getString("id");
			try {
				url = new URL("http://" + serverAddress + ":" + serverPort
						+ "/futbol-api/club/" + id + "/noticias");
			} catch (MalformedURLException e) {
				Log.d(TAG, e.getMessage(), e);
			}
			Intent intent = new Intent(this, WriteNoticia.class);
			intent.putExtra("url", url);
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
		Bundle extra = this.getIntent().getExtras();
		String id = "1";
		id = extra.getString("id");
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
		
		noticiaList = new ArrayList<Noticia>();
		adapter = new NoticiaAdapter(this, noticiaList);
		
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
					+ "/futbol-api/club/" + id + "/noticias?&offset=0&length=20");
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		(new FetchNoticiasTask()).execute(url);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Noticia noticia = noticiaList.get(position);
		// HATEOAS version
		
		URL url = null;
		try {
			url = new URL(noticia.getLinks().get(0).getUri());
		} catch (MalformedURLException e) {
			return;
		}

		
		Log.d(TAG, url.toString());
		Intent intent = new Intent(this, NoticiaDetail.class);
		intent.putExtra("url", url.toString());
		startActivity(intent);
		
		
	}
	
	private void addNoticias(NoticiasCollection clubs){
		noticiaList.addAll(clubs.getNoticias());
		adapter.notifyDataSetChanged();
	}
	
	
	private class FetchNoticiasTask extends AsyncTask<URL, Void, NoticiasCollection> {
		private ProgressDialog pd;
	 
		@Override
		protected NoticiasCollection doInBackground(URL... params) {
			NoticiasCollection noticias = api.getNoticias(params[0]);
			return noticias;
		}
	
		@Override
		protected void onPostExecute(NoticiasCollection result) {
			addNoticias(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(NoticiasActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		
	 
	}
	
		
	}

