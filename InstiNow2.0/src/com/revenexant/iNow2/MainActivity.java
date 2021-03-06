package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
    private EditText username1,password1;
    private TextView loginerror;
    private static int test;
	private Button login;
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static boolean check;
	JsonParser jsonparser=new JsonParser();
	 private static final String url = "http://students.iitm.ac.in/mobops_testing/login.php";
	 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.loginpage);
			username1=(EditText) findViewById(R.id.editText1);
			password1=(EditText) findViewById(R.id.editText2);
			loginerror = (TextView) findViewById(R.id.errorText);
			login=(Button) findViewById(R.id.button1);
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try{NetCheck();}
					catch(Exception e){e.printStackTrace();
					loginerror.setText("NetCheck error.");
					Intent i = new Intent(MainActivity.this, UserChoices.class);
                    startActivity(i);}
					
				}}
				);}
		    private void NetCheck() 
	    {
		    	if (DetectConnection
		    		      .checkInternetConnection(MainActivity.this)) {
		    		
		    		     new AttemptLogin().execute();
		    		    } else {
		    		     Toast.makeText(MainActivity.this,
		    		       "You Do not have Internet Connection",
		    		       Toast.LENGTH_LONG).show();
		    		 
		    		     MainActivity.this.startActivity(new Intent(
		    		       Settings.ACTION_WIRELESS_SETTINGS));
		    		    }
	        
			
			
	    }
			class AttemptLogin extends AsyncTask<String, String, String> {
		        boolean failure = false;
				private ProgressDialog pDialog;
		        @Override
		        protected void onPreExecute() {
		        	super.onPreExecute();
		            pDialog = new ProgressDialog(MainActivity.this);
		            pDialog.setMessage("Logging in ...");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
		        }

		        @Override
		        protected String doInBackground(String... params) {
		        	int success;
		        	String username2 = username1.getText().toString();
		        	String password2= password1.getText().toString();
		        	try {
		        		List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
		        		users.add(new BasicNameValuePair("username3", username2));
		        		users.add(new BasicNameValuePair("password3", password2));
		        		
		        		Log.d("request!", "starting");
		        		JSONObject json = jsonparser.makeHttpRequest(
		        				url, "POST", users);
		        		success = json.getInt(TAG_SUCCESS);
		        		if (success == 1) {
		        			Log.d("Login Successful!", json.toString());
		        			test=1;
		        			return json.getString(TAG_MESSAGE);
		        		}else{
		        			Log.d("Login Failure!", json.getString(TAG_MESSAGE));
		        			return json.getString(TAG_MESSAGE);      
		        		}
		        	} catch (JSONException e) {
		        		e.printStackTrace();
		        	}				return null;
		        }
				protected void onPostExecute (
						String file_url){
					pDialog.dismiss();
					if(test==1){
						Toast.makeText(getApplicationContext(), "logged in", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(MainActivity.this, UserChoices.class);
	                    startActivity(i);
					}
					else{
						Toast.makeText(getApplicationContext(), "Invalid Credentials" , Toast.LENGTH_LONG).show();
						Intent i = new Intent(MainActivity.this,MainActivity.class);
	                    finish();
	                    startActivity(i);
					}
					
				}

		    }
	    


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        if (id == R.id.action_settings) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	}

