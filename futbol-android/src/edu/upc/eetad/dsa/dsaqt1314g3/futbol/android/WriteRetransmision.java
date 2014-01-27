package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Retransmision;

public class WriteRetransmision extends Activity {
	private final static String TAG = WriteRetransmision.class.toString();
 
	private class PostRetransmisionTask extends AsyncTask<String, Void, Retransmision> {
		private URL url;
		private ProgressDialog pd;
 
		public PostRetransmisionTask(URL url) {
			super();
			this.url = url;
		}
 
		@Override
		protected Retransmision doInBackground(String... params) {
			FutbolAPI api = new FutbolAPI();
			Retransmision retransmision = api.createRetransmision(url, params[0], params[1]);
			return retransmision;
		}
 
		@Override
		protected void onPostExecute(Retransmision result) {
			showRetransmisiones();
			if (pd != null) {
				pd.dismiss();
			}
		}
 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(WriteRetransmision.this);
 
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
 
	}
 
	private URL url;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_retransmision_layout);
 
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
		if (content.length() > 0 && content.length() < 500 && 
				titulo.length() > 0 && titulo.length() < 45){
			(new PostRetransmisionTask(url)).execute(titulo, content);
		}
		else if (content.length() >= 500){
			AlertDialog.Builder dialog = new AlertDialog.Builder(WriteRetransmision.this);
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
			AlertDialog.Builder dialog = new AlertDialog.Builder(WriteRetransmision.this);
        	dialog.setMessage("Content no puede estar vacio");
        	dialog.setCancelable(false);
        	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	 
        	  @Override
        	  public void onClick(DialogInterface dialog, int which) {
        	     //Login.this.finish();
        	  }
        	});
        	dialog.show();
		}
		else{
			AlertDialog.Builder dialog = new AlertDialog.Builder(WriteRetransmision.this);
        	dialog.setMessage("Formato del tiempo incorrecto");
        	dialog.setCancelable(false);
        	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        	 
        	  @Override
        	  public void onClick(DialogInterface dialog, int which) {
        	     //Login.this.finish();
        	  }
        	});
        	dialog.show();
		}
		//(new PostRetransmisionTask(url)).execute(titulo, content);
	}
	
	private void showRetransmisiones(){
		Intent intent = new Intent(this, CampeonatosActivity.class);
		startActivity(intent);
	}
}
