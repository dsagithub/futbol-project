package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.URL;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Noticia;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WriteNoticia extends Activity {
	private final static String TAG = WriteNoticia.class.toString();
 
	private class PostNoticiaTask extends AsyncTask<String, Void, Noticia> {
		private URL url;
		private ProgressDialog pd;
 
		public PostNoticiaTask(URL url) {
			super();
			this.url = url;
		}
 
		@Override
		protected Noticia doInBackground(String... params) {
			FutbolAPI api = new FutbolAPI();
			Noticia noticia = api.createNoticia(url, params[0], params[1]);
			return noticia;
		}
 
		@Override
		protected void onPostExecute(Noticia result) {
			showNoticias();
			if (pd != null) {
				pd.dismiss();
			}
		}
 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(WriteNoticia.this);
 
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
 
	}
 
	private URL url;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_noticia_layout);
 
		url = (URL) getIntent().getExtras().get("url");
	}
 
	public void cancel(View v) {
		finish();
	}
 
	public void postSting(View v) {
		EditText etTitulo = (EditText) findViewById(R.id.etTitulo);
		EditText etContent = (EditText) findViewById(R.id.etContent);
 
		String titulo = etTitulo.getText().toString();
		String content = etContent.getText().toString();
 
		(new PostNoticiaTask(url)).execute(titulo, content);
	}
	
	private void showNoticias(){
		Intent intent = new Intent(this, FutbolMainActivity.class);
		startActivity(intent);
	}
}
