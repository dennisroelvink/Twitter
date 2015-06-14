package nl.saxion.twitter_client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.apache.http.client.methods.HttpPost;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The profile activity class
 * @author Sharon
 *
 */
public class ProfileActivity extends ActionBarActivity implements Observer {
	
	private ConnectionHandler verify;
	private Model model;
	private static final String PREFS = "LOGINTOKEN";
	
	private TextView twittername;
	private TextView screenname;
	private ListView tweetList;
	private ImageView profilePic;
	private TweetAdapter adapter;
	private JSONHandler handler;
	private TextView tweetsSent;
	private TextView following;
	private TextView followers;
	private Button postTweet;
	private EditText tweetText;
	private Button refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		twittername = (TextView) findViewById(R.id.textViewTwitterName);
		screenname = (TextView) findViewById(R.id.textViewScreenName);
		tweetList = (ListView) findViewById(R.id.listViewProfileTweets);
		profilePic = (ImageView) findViewById(R.id.imageViewProfilePicProfile);
		tweetsSent = (TextView) findViewById(R.id.textViewTweetsSent);
		following = (TextView) findViewById(R.id.textViewFollowing);
		followers = (TextView) findViewById(R.id.textViewFollowers);
		postTweet = (Button) findViewById(R.id.buttonPostTweet);
		tweetText = (EditText) findViewById(R.id.editTextNewTweet);
		refresh = (Button) findViewById(R.id.buttonRefresh);
		
		
		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();
		
		adapter = new TweetAdapter(this, R.layout.tweet, model.getTimeLine());
		tweetList.setAdapter(adapter);
		model.addObserver(adapter);
		model.addObserver(this);
		
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

		
		HttpGet httpGetUser = new HttpGet("https://api.twitter.com/1.1/account/verify_credentials.json");
		HttpGet httpGetTimeline = new HttpGet("https://api.twitter.com/1.1/statuses/home_timeline.json");
		
		postTweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String query = tweetText.getText().toString();
				String post = "";
				if(query.length() != 0) {
					try {
						post = URLEncoder.encode(query, "UTF-8");
					} catch (UnsupportedEncodingException e1) {
						Log.d("Encoded error","couldn't encode tweet text");
					}
					HttpPost httpPostTweet = new HttpPost("https://api.twitter.com/1.1/statuses/update.json?status=" + post);
					try {
						verify.signWithUserTokenNewTweet(httpPostTweet);
						tweetText.setText("");
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
				} else {
					Toast.makeText(getApplicationContext(), "Invalid tweet input", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		
		
		try {
			verify.signWithUserTokenCredentials(httpGetUser);
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

		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				model.deleteTimeLine();
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
		} else if (id == R.id.action_logout) {
			model.getcHandler().getConsumer().setTokenWithSecret(null, null);
			model.setGoBackToMain(true);
			model.deleteTimeLine();
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		model.getcHandler().getConsumer().setTokenWithSecret(null, null);
		model.setGoBackToMain(true);
		model.deleteTimeLine();
		finish();
		super.onBackPressed();
	}

	@Override
	public void update(Observable observable, Object data) {
		if(model.getAccount().getName().length() == 0) {
			Toast.makeText(getApplicationContext(), "There was an Error", Toast.LENGTH_SHORT).show();
			model.getcHandler().getConsumer().setTokenWithSecret(null, null);
			model.setGoBackToMain(true);
			model.deleteTimeLine();
			finish();
		} else {
			twittername.setText(model.getAccount().getName());
			screenname.setText(model.getAccount().getUserName());
			profilePic.setImageBitmap(model.getAccount().getBitmap());
			tweetsSent.setText(model.getAccount().getTweetsSent());
			followers.setText(model.getAccount().getFollowers());
			following.setText(model.getAccount().getFollowing());
		}

		
	}
}
