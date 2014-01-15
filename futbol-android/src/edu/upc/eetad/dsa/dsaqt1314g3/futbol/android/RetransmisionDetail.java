package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.FutbolAPI;
import edu.upc.eetac.dsa.dsaqt1314g3.futbol.android.api.Retransmision;

public class RetransmisionDetail extends Activity{
	public final static String Tag = RetransmisionDetail.class.toString();

	private FutbolAPI api;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.retransmision_detail_layout);
		api = new FutbolAPI();
		URL url = null;
		try {
			url = new URL((String) getIntent().getExtras().get("url"));
		} catch (MalformedURLException e) {
		}
		(new FetchRetransmisionTask()).execute(url);
	}
	
	private void loadRetransmision(Retransmision retra) {
		TextView tvDetailTiempo = (TextView) findViewById(R.id.tvDetailTiempo);
		TextView tvDetailTexto = (TextView) findViewById(R.id.tvDetailTexto);
	 
		tvDetailTiempo.setText(retra.getTiempo());
		tvDetailTexto.setText(retra.getTexto());
	}
	
	private class FetchRetransmisionTask extends AsyncTask<URL, Void, Retransmision> {
		private ProgressDialog pd;
	 
		@Override
		protected Retransmision doInBackground(URL... params) {
			Retransmision retra = api.getRetransmision(params[0]);
			return retra;
		}
	 
		@Override
		protected void onPostExecute(Retransmision result) {
			loadRetransmision(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(RetransmisionDetail.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
	
}
