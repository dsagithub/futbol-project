package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Comentario;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;

public class ComentarioDetail extends Activity{
	public final static String Tag = ComentarioDetail.class.toString();

	private FutbolAPI api;
	private boolean aux = false;
	private String iduser = null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Bundle extra = this.getIntent().getExtras();
		String username = extra.getString("uname");
		
		
		if (username.compareTo("administrator") == 0 || aux){
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
			api.deleteComentario(url);
			Intent intent = new Intent(this, CampeonatosActivity.class);
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
		Bundle extra = this.getIntent().getExtras();
		iduser = extra.getString("iduser");
		setContentView(R.layout.comentario_detail_layout);
		api = new FutbolAPI();
		URL url = null;
		try {
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		(new FetchComentarioTask()).execute(url);
	}
	
	private void loadComentario(Comentario comentario) {
		TextView tvDetailTiempo = (TextView) findViewById(R.id.tvDetailTiempo);
		TextView tvDetailTexto = (TextView) findViewById(R.id.tvDetailTexto);
		TextView tvDetailMedia = (TextView) findViewById(R.id.tvDetailMedia);
		TextView tvDetailPartido = (TextView) findViewById(R.id.tvDetailPartido);
	 
		tvDetailTiempo.setText(comentario.getTiempo());
		tvDetailTexto.setText(comentario.getTexto());
		tvDetailMedia.setText(comentario.getMedia());
		tvDetailPartido.setText(Integer.toString(comentario.getIdUsuario()));
		if (Integer.parseInt(iduser) == comentario.getIdUsuario()){
			aux = true;
		}
	}
	
	private class FetchComentarioTask extends AsyncTask<URL, Void, Comentario> {
		private ProgressDialog pd;
	 
		@Override
		protected Comentario doInBackground(URL... params) {
			Comentario comentario = api.getComentario(params[0]);
			return comentario;
		}
	 
		@Override
		protected void onPostExecute(Comentario result) {
			loadComentario(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ComentarioDetail.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
	
}
