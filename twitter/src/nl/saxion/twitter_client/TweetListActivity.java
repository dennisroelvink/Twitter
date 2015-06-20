package nl.saxion.twitter_client;

import java.util.Observable;
import java.util.Observer;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import nl.saxion.twitter_client.objects.Tweet;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TweetListActivity extends Activity implements Observer {

	private ConnectionHandler verify;
	private Model model;
	private TweetAdapter adapter;
	private JSONHandler handler;
	private static final String PREFS = "LOGINTOKEN";
	
	private ListView tweetlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		tweetlist = (ListView) findViewById(R.id.listViewTweetlistTweet);
		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();

		Intent i = getIntent();
		int choice = i.getIntExtra("choice", -1);
		adapter = new TweetAdapter(this, R.layout.tweet,model.getTimeLine());
		model.addObserver(this);

		verify = model.getcHandler();
		model.getcHandler().addObserver(this);
		handler = new JSONHandler(this);
		tweetlist.setAdapter(adapter);
		model.addObserver(adapter);

		if (choice == 0) {
			model.deleteTimeLine();
			HttpGet httpGetTimeline = new HttpGet(
					"https://api.twitter.com/1.1/statuses/home_timeline.json");
			try {
				verify.signWithUserTokenTimeline(httpGetTimeline);
			} catch (OAuthMessageSignerException e) {
				Log.d("Error", " Message Signer Profile Activity");
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				Log.d("Error", " Expectation Failed Profile Activity");
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				Log.d("Error", " Communication Exception Profile Activity");
				e.printStackTrace();
			}
		} else {
			model.deleteTimeLine();
			HttpGet httpGetFavorites = new HttpGet(
					"https://api.twitter.com/1.1/favorites/list.json");
			try {
				verify.signWithUserTokenTimeline(httpGetFavorites);
			} catch (OAuthMessageSignerException e) {
				Log.d("Error", " Message Signer Profile Activity");
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				Log.d("Error", " Expectation Failed Profile Activity");
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				Log.d("Error", " Communication Exception Profile Activity");
				e.printStackTrace();
			}
		}
		tweetlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Log.d("Poke",""+position);
				Tweet t = model.getTweetAtPos(position);
				String idnumber = t.getId();
				if(t.isFav()) {
					HttpPost httpPostDeleteFav = new HttpPost("https://api.twitter.com/1.1/favorites/destroy.json?id="+idnumber);
					try {
						verify.signWithUserTokenPostRequest(httpPostDeleteFav);
					} catch (OAuthMessageSignerException e) {
						Log.d("Error", " Message Signer Profile Activity");
						e.printStackTrace();
					} catch (OAuthExpectationFailedException e) {
						Log.d("Error", " Expectation Failed Profile Activity");
						e.printStackTrace();
					} catch (OAuthCommunicationException e) {
						Log.d("Error", " Communication Exception Profile Activity");
						e.printStackTrace();
					}
					t.setFav(false);
				} else {
					HttpPost httpPostMakeFav = new HttpPost(
							"https://api.twitter.com/1.1/favorites/create.json?id="+idnumber);
					try {
						verify.signWithUserTokenPostRequest(httpPostMakeFav);
					} catch (OAuthMessageSignerException e) {
						Log.d("Error", " Message Signer Profile Activity");
						e.printStackTrace();
					} catch (OAuthExpectationFailedException e) {
						Log.d("Error", " Expectation Failed Profile Activity");
						e.printStackTrace();
					} catch (OAuthCommunicationException e) {
						Log.d("Error", " Communication Exception Profile Activity");
						e.printStackTrace();
					}
					t.setFav(true);
				}
				
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
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

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
}
