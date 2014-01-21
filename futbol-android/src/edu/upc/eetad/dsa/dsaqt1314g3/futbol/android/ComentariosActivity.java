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
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Comentario;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.ComentariosCollection;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.User;

public class ComentariosActivity extends ListActivity {
	private final static String TAG = ComentariosActivity.class.toString();
	private String serverAddress = null;
	private String serverPort = null;
	private FutbolAPI api;
	private ArrayList<Comentario> comentarioList;
	private ComentarioAdapter adapter;
	private String iduser = null;
	private String uname;
	

 
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
		iduser = user.getIdusuario();
		getMenuInflater().inflate(R.menu.futbol_menu, menu);
		return true;
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
				url = new URL(url2 + "/comentarios");
			} catch (MalformedURLException e) {
				Log.d(TAG, e.getMessage(), e);
			}
			Intent intent = new Intent(this, WriteComentario.class);
			intent.putExtra("url", url);
			intent.putExtra("iduser", iduser);
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
		
		comentarioList = new ArrayList<Comentario>();
		adapter = new ComentarioAdapter(this, comentarioList);
		
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
					+ "/futbol-api/users/" + username);
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		User user = api.getUser(url);
		iduser = user.getIdusuario();
		if (user.getRole().compareTo("administrator") == 0){
			uname = "administrator";
		}
		else {
			uname = "registered";
		}
		
		
		api = new FutbolAPI();
		url = null;
		try {
			url = new URL(url2 + "/comentarios?offset=0&length=20");
		} catch (MalformedURLException e) {
			Log.d(TAG, e.getMessage(), e);
			finish();
		}
		(new FetchComentariosTask()).execute(url);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Comentario comentario = comentarioList.get(position);
		// HATEOAS version
		
		URL url = null;
		try {
			url = new URL(comentario.getLinks().get(0).getUri());
		} catch (MalformedURLException e) {
			return;
		}

		
		Log.d(TAG, url.toString());
		Intent intent = new Intent(this, ComentarioDetail.class);
		intent.putExtra("url", url.toString());
		intent.putExtra("uname", uname);
		intent.putExtra("iduser", iduser);
		startActivity(intent);
		
		
	}
	
	private void addComentarios(ComentariosCollection comentarios){
		comentarioList.addAll(comentarios.getComentarios());
		adapter.notifyDataSetChanged();
	}
	
	
	private class FetchComentariosTask extends AsyncTask<URL, Void, ComentariosCollection> {
		private ProgressDialog pd;
	 
		@Override
		protected ComentariosCollection doInBackground(URL... params) {
			ComentariosCollection comentarios = api.getComentarios(params[0]);
			return comentarios;
		}
	
		@Override
		protected void onPostExecute(ComentariosCollection result) {
			addComentarios(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ComentariosActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		
	 
	}
	
		
	}

