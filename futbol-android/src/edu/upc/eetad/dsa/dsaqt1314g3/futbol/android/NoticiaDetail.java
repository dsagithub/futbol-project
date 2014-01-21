package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Noticia;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NoticiaDetail extends Activity{
	public final static String TAG = NoticiaDetail.class.toString();

	private FutbolAPI api;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Bundle extra = this.getIntent().getExtras();
		String username = extra.getString("uname");
		
		if (username.compareTo("administrator") == 0){
			getMenuInflater().inflate(R.menu.futbol_menu2, menu);
			return true;
		}
		else return false;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miDelete:
			api = new FutbolAPI();
			URL url = null;
			try {
				url = new URL((String) getIntent().getExtras().get("url"));
			} catch (MalformedURLException e) {
			}
			api.deleteNoticia(url);
			Intent intent = new Intent(this, FutbolMainActivity.class);
			startActivity(intent);
			return true;
	 
		default:
			return super.onOptionsItemSelected(item);
		}
	 
	}
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noticia_detail_layout);
		api = new FutbolAPI();
		URL url = null;
		try {
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		(new FetchNoticiaTask()).execute(url);
	}
	
	private void loadNoticia(Noticia noticia) {
		TextView tvDetailSubject = (TextView) findViewById(R.id.tvDetailTitulo);
		TextView tvDetailContent = (TextView) findViewById(R.id.tvDetailContent);
		TextView tvDetailUsername = (TextView) findViewById(R.id.tvDetailMedia);
		TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
	 
		tvDetailSubject.setText(noticia.getTitulo());
		tvDetailContent.setText(noticia.getContent());
		tvDetailUsername.setText(noticia.getMedia());
		tvDetailDate.setText(SimpleDateFormat.getInstance().format(
				noticia.getLastModified()));
	}
	
	private class FetchNoticiaTask extends AsyncTask<URL, Void, Noticia> {
		private ProgressDialog pd;
	 
		@Override
		protected Noticia doInBackground(URL... params) {
			Noticia noticia = api.getNoticia(params[0]);
			return noticia;
		}
	 
		@Override
		protected void onPostExecute(Noticia result) {
			loadNoticia(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(NoticiaDetail.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
	
}

