package edu.upc.eetad.dsa.dsaqt1314g3.futbol.android;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {
	private final static String TAG = Login.class.toString();
	private String serverAddress = null;
	private String serverPort = null;
	String username = null;
	String password = null;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        
        SharedPreferences prefs = getSharedPreferences("futbol-profile",
    				Context.MODE_PRIVATE);

        username = prefs.getString("username", null);
    	password = prefs.getString("password", null);
    		
    	// Uncomment the next two lines to test the application without login each time
   		//username = "admin";
   		//password = "admin";
   		/*
   		if ((username != null) && (password != null)) {
   			Intent intent = new Intent(this, FutbolMainActivity.class);
   			startActivity(intent);
   			finish();
   		}*/

        AssetManager assetManager = getAssets();
        Properties config = new Properties();
        try {
        	config.load(assetManager.open("config.properties"));
            serverAddress = config.getProperty("server.address");
            serverPort = config.getProperty("server.port");
            Log.d(TAG, "Configured server " + serverAddress + ":" + serverPort);
            
            } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    finish();
            }

            setContentView(R.layout.login_layout);

    }

    public void signIn(View v) {
    	EditText etUsername = (EditText) findViewById(R.id.etUsername);
		EditText etPassword = (EditText) findViewById(R.id.etPassword);
 
		username = etUsername.getText().toString();
		password = etPassword.getText().toString();

            (new LoginTask()).execute();

    }

    private void startFutbolActivity() {
            Intent intent = new Intent(this, FutbolMainActivity.class);
            startActivity(intent);
            finish();
    }

    private class LoginTask extends AsyncTask<URL, Integer, String> {
            String response = null;

            @Override
            protected String doInBackground(URL... params) {

                    HttpURLConnection urlConnection = null;

                    URL url = null;
                    try {
                            url = new URL("http://" + serverAddress + ":" + serverPort
                                            + "/futbol-auth/ServletLogin?username=" + username
                                            + "&password=" + password);

                    } catch (MalformedURLException e) {
                            Log.d(TAG, e.getMessage(), e);
                            finish();
                    }
                    try {

                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setDoInput(true);
                            urlConnection.setDoOutput(true);
                            urlConnection.connect();
                            
                            PrintWriter writer = new PrintWriter(
                					urlConnection.getOutputStream());
                            JSONObject result = new JSONObject();
                			writer.println(result.toString());
                			writer.close();
                			
                			BufferedReader reader = new BufferedReader(new InputStreamReader(
                					urlConnection.getInputStream()));
                			StringBuilder sb = new StringBuilder();
                			String line = null;
                			while ((line = reader.readLine()) != null) {
                				sb.append(line);
                			}
                			response = sb.toString();

                    } catch (ClientProtocolException e) {
                            e.printStackTrace();
                    } catch (IOException e) {
                            e.printStackTrace();
                    } finally {
            			if (urlConnection != null)
            				urlConnection.disconnect();
            		}
                    
                    return response;
            }

            @Override
            protected void onPostExecute(String result) {
                    if (response.compareTo("successadmin") == 0) {

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

                    }else if (response.compareTo("successusuario") == 0) {

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

                    }else if (response.compareTo("wrongpass") == 0) {
                    	AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                    	dialog.setMessage(response);
                    	dialog.setCancelable(false);
                    	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    	 
                    	  @Override
                    	  public void onClick(DialogInterface dialog, int which) {
                    		  Login.this.finish();
                    	  }
                    	});
                    	dialog.show();
                    }else {
                    	AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                    	dialog.setMessage("Usuario no existe");
                    	dialog.setCancelable(false);
                    	dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    	 
                    	  @Override
                    	  public void onClick(DialogInterface dialog, int which) {
                    	     Login.this.finish();
                    	  }
                    	});
                    	dialog.show();
                    }

            }
    }
}

