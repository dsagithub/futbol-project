package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.URL;

import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Noticia;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
		if (titulo.length() > 0 && titulo.length() < 100 && 
				content.length() > 0 && content.length() < 500){
			(new PostNoticiaTask(url)).execute(titulo, content);
		}
		else if (content.length() >= 500){
			AlertDialog.Builder dialog = new AlertDialog.Builder(WriteNoticia.this);
        	dialog.setMessage("Content demasiado largo");
        	dialog.setCancelable(false);
        	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	 
        	  @Override
        	  public void onClick(DialogInterface dialog, int which) {
        	     //Login.this.finish();
        	  }
        	});
        	dialog.show();
		}else if (content.length() <= 0){
			AlertDialog.Builder dialog = new AlertDialog.Builder(WriteNoticia.this);
        	dialog.setMessage("Content no puede estar vacio");
        	dialog.setCancelable(false);
        	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	 
        	  @Override
        	  public void onClick(DialogInterface dialog, int which) {
        	     //Login.this.finish();
        	  }
        	});
        	dialog.show();
		}else if (titulo.length() >= 100){
			AlertDialog.Builder dialog = new AlertDialog.Builder(WriteNoticia.this);
        	dialog.setMessage("Titulo demasiado largo");
        	dialog.setCancelable(false);
        	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	 
        	  @Override
        	  public void onClick(DialogInterface dialog, int which) {
        	     //Login.this.finish();
        	  }
        	});
        	dialog.show();
		}else{
			AlertDialog.Builder dialog = new AlertDialog.Builder(WriteNoticia.this);
        	dialog.setMessage("Titulo no puede estar vacio");
        	dialog.setCancelable(false);
        	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	 
        	  @Override
        	  public void onClick(DialogInterface dialog, int which) {
        	     //Login.this.finish();
        	  }
        	});
        	dialog.show();
		}
		//(new PostNoticiaTask(url)).execute(titulo, content);
	}
	
	private void showNoticias(){
		Intent intent = new Intent(this, FutbolMainActivity.class);
		startActivity(intent);
	}
}
