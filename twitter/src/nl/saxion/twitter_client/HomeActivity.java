package nl.saxion.twitter_client;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.signature.OAuthMessageSigner;
import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends ActionBarActivity {

	private Button login;
	private Button guest;
	private Model model;
	private static final String PREFS = "LOGINTOKEN";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		TweetApplication app = (TweetApplication) getApplication();
		model = app.getModel();
		model.setMainActivity(this);
		OAuthConsumer consumer = model.getcHandler().getConsumer();
		login = (Button) findViewById(R.id.buttonLogin);
		guest = (Button) findViewById(R.id.buttonGuest);
		
		SharedPreferences prefs = getSharedPreferences(PREFS, 0);
		String token = prefs.getString("token", "");
		String secret = prefs.getString("tokenSecret", "");
		Log.d("Putt",prefs.getString("token", ""));
		Log.d("Putt",prefs.getString("tokenSecret", ""));
		//TODO '=' veranderen in !
		if(token.length() == 0 && secret.length() !=0) {
			
			consumer.setTokenWithSecret(token,	secret);
			Log.d("Putt",consumer.getToken());
			Intent i = new Intent(HomeActivity.this,ProfileActivity.class);
			startActivity(i);
		}
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(i);
				
			}
		});
		
		guest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeActivity.this, MainActivity.class);
				startActivity(i);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
