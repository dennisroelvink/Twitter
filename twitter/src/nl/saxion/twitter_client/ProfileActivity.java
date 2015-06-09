package nl.saxion.twitter_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends ActionBarActivity {
	
	private ConnectionHandler verify;
	private Model model;
	private Button refresh;
	
	private TextView twittername;
	private TextView screenname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		twittername = (TextView) findViewById(R.id.textViewTwitterName);
		screenname = (TextView) findViewById(R.id.textViewScreenName);
		refresh = (Button) findViewById(R.id.buttonRefresh);
		
		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();
		
		Intent i = getIntent();
		String code = i.getStringExtra("verifier");
		verify = model.getcHandler();
		verify.getConnectionInit(code,this);
		HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/account/verify_credentials.json");
		
		try {
			verify.signWithUserToken(httpGet);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(model.isFinishedMakingUser()) {
			twittername.setText(model.getAccount().getName());
			screenname.setText(model.getAccount().getUserName());
		}
		
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				recreate();
				
			}
			
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
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
