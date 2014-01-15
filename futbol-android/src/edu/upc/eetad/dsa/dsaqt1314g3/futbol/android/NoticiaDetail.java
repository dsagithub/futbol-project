package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Noticia;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class NoticiaDetail extends Activity{
	public final static String Tag = NoticiaDetail.class.toString();

	private FutbolAPI api;

	
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

