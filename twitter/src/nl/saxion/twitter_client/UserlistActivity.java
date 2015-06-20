package nl.saxion.twitter_client;

import java.util.Observable;
import java.util.Observer;

import org.apache.http.client.methods.HttpGet;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class UserlistActivity extends Activity implements Observer{

	private ListView userlist;
	private Model model;
	private ConnectionHandler verify;
	private Button friends;
	private Button followers;
	private JSONHandler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userlist);
		handler = new JSONHandler(this);
		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();
		userlist = (ListView) findViewById(R.id.listViewUserlist);
		friends = (Button) findViewById(R.id.buttonFriendsUserlist);
		followers = (Button) findViewById(R.id.buttonFollowersUserlist);
		verify = model.getcHandler();
		UserAdapter adapter = new UserAdapter(this, R.layout.user, model.getUserList());
		
		userlist.setAdapter(adapter);
		model.addObserver(adapter);
		model.getcHandler().addObserver(this);
		
		friends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				model.clearUserList();
				HttpGet httpGetFriends = new HttpGet("https://api.twitter.com/1.1/friends/list.json");
				try {
					verify.signWithUserTokenUserList(httpGetFriends);
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
				
			}
		});
		
		followers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				model.clearUserList();
				HttpGet httpGetFollowers = new HttpGet("https://api.twitter.com/1.1/followers/list.json");
				try {
					verify.signWithUserTokenUserList(httpGetFollowers);
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
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.userlist, menu);
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
