package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Comentario;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;

public class WriteComentario extends Activity {
	private final static String TAG = WriteComentario.class.toString();
 
	private class PostComentarioTask extends AsyncTask<String, Void, Comentario> {
		private URL url;
		private ProgressDialog pd;
 
		public PostComentarioTask(URL url) {
			super();
			this.url = url;
		}
 
		@Override
		protected Comentario doInBackground(String... params) {
			FutbolAPI api = new FutbolAPI();
			Comentario comentario = api.createComentario(url, params[0], params[2]);
			return comentario;
		}
 
		@Override
		protected void onPostExecute(Comentario result) {
			showComentarios();
			if (pd != null) {
				pd.dismiss();
			}
		}
 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(WriteComentario.this);
 
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
 
	}
 
	private URL url;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_comentario_layout);
 
		url = (URL) getIntent().getExtras().get("url");
	}
 
	public void cancel(View v) {
		finish();
	}
 
	public void postSting(View v) {
		EditText etTiempo = (EditText) findViewById(R.id.etTiempo);
		EditText etTexto = (EditText) findViewById(R.id.etTexto);
 
		String tiempo = etTiempo.getText().toString();
		String texto = etTexto.getText().toString();
 
		(new PostComentarioTask(url)).execute(tiempo, texto);
	}
	
	private void showComentarios(){
		Intent intent = new Intent(this, ComentariosActivity.class);
		startActivity(intent);
	}
}

