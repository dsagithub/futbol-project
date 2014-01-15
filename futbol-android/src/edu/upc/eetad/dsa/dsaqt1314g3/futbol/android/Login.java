package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class Login extends Activity {
	private final static String TAG = Login.class.toString();
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
 
		SharedPreferences prefs = getSharedPreferences("futbol-profile",
				Context.MODE_PRIVATE);
		String username = prefs.getString("username", null);
		String password = prefs.getString("password", null);
		
		// Uncomment the next two lines to test the application without login each time
		 //username = "admin";
		 //password = "admin";
		if ((username != null) && (password != null)) {
			Intent intent = new Intent(this, FutbolMainActivity.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.login_layout);
	}
 
	public void signIn(View v) {
		EditText etUsername = (EditText) findViewById(R.id.etUsername);
		EditText etPassword = (EditText) findViewById(R.id.etPassword);
 
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();
 
 
		// I'll suppose that u/p are correct:
		SharedPreferences prefs = getSharedPreferences("futbol-profile",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.putString("username", username);
		editor.putString("password", password);
		boolean done = editor.commit();
		if (done)
			Log.d(TAG, "preferences set");
		else
			Log.d(TAG, "preferences not set. THIS A SEVERE PROBLEM");
 
		startFutbolActivity();
	}
 
	private void startFutbolActivity() {
		Intent intent = new Intent(this, FutbolMainActivity.class);
		startActivity(intent);
		finish();
	}
 
}
