package nl.saxion.twitter_client;

import java.util.Observable;
import java.util.Observer;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;







import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity extends ActionBarActivity implements Observer {
	
	private ConnectionHandler verify;
	private Model model;
	private static final String PREFS = "LOGINTOKEN";
	
	private TextView twittername;
	private TextView screenname;
	private ListView tweetList;
	private TweetAdapter adapter;
	private JSONHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		twittername = (TextView) findViewById(R.id.textViewTwitterName);
		screenname = (TextView) findViewById(R.id.textViewScreenName);
		tweetList = (ListView) findViewById(R.id.listViewProfileTweets);
		
		
		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();
		
		adapter = new TweetAdapter(this, R.layout.tweet, model.getTimeLine());
		tweetList.setAdapter(adapter);
		model.addObserver(adapter);
		
		OAuthConsumer c = model.getcHandler().getConsumer();
		SharedPreferences prefs = getSharedPreferences(PREFS, 0);
		Editor editor = prefs.edit();
		editor.putString("token", c.getToken());
		Log.d("Cindy2",c.getToken());
		editor.putString("tokenSecret", c.getTokenSecret());
		editor.commit();
		
		verify = model.getcHandler();
		model.getcHandler().addObserver(this);
		
		handler = new JSONHandler(this);
		//handler.JSONToTimeLine(JSONText);
		
		HttpGet httpGetUser = new HttpGet("https://api.twitter.com/1.1/account/verify_credentials.json");
		HttpGet httpGetTimeline = new HttpGet("https://api.twitter.com/1.1/statuses/home_timeline.json");
		
		try {
			verify.signWithUserToken(httpGetUser);
			verify.signWithUserTokenTimeline(httpGetTimeline);
		} catch (OAuthMessageSignerException e) {
			Log.d("Error"," Message Signer Profile Activity");
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			Log.d("Error"," Expectation Failed Profile Activity");
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			Log.d("Error"," Communication Exception Profile Activity");
			e.printStackTrace();
		}
		
		if(model.isFinishedMakingUser()) {
			twittername.setText(model.getAccount().getName());
			screenname.setText(model.getAccount().getUserName());
			
			
		}
			
		


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
		} else if (id == R.id.action_logout) {
			model.setGoBackToMain(true);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void update(Observable observable, Object data) {
		twittername.setText(model.getAccount().getName());
		screenname.setText(model.getAccount().getUserName());
		
	}
}
