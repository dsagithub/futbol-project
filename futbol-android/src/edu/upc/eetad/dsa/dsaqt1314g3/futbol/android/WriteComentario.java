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
	private String iduser = null;
 
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
			Comentario comentario = api.createComentario(url, params[0], iduser);
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
		Bundle extra = this.getIntent().getExtras();
		iduser = extra.getString("iduser");
		url = (URL) getIntent().getExtras().get("url");
	}
 
	public void cancel(View v) {
		finish();
	}
 
	public void postSting(View v) {
		//EditText etTitulo = (EditText) findViewById(R.id.etTitulo);
		EditText etContent = (EditText) findViewById(R.id.etContent);
 
		//String titulo = etTitulo.getText().toString();
		String content = etContent.getText().toString();
 
		(new PostComentarioTask(url)).execute(content);
	}
	
	private void showComentarios(){
		Intent intent = new Intent(this, CampeonatosActivity.class);
		startActivity(intent);
	}
}
