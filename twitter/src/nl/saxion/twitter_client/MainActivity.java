package nl.saxion.twitter_client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import nl.saxion.twitter_client.objects.Tweet;
import nl.saxion.twitter_client.objects.User;
import android.support.v7.app.ActionBarActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

/**
 * The MainActivity class
 * @author Sharon and Dennis
 *
 */
public class MainActivity extends ActionBarActivity {
	
	private Model model;
	private ListView listview;
	private TweetAdapter adapter;
	private JSONHandler handler;
	private SearchView searcher;
	private URLTool urlTool;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();
		urlTool = new URLTool(this);
		
		
		
		
		
		searcher = (SearchView) findViewById(R.id.searchViewTweetSearch);
		listview = (ListView) findViewById(R.id.listViewTweet);
		handler = new JSONHandler(this);
		
		searcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		
			@Override
			public boolean onQueryTextSubmit(String query) {
				Log.d("Conchita",""+urlTool.getJSONText());
				String HTTPSrequest = "https://api.twitter.com/1.1/search/tweets.json?q=" +query;
				model.deleteAllTweets();
				Toast.makeText(getApplicationContext(), "TWEETS LEFT :  "+ ""+model.getTweetList().size(), Toast.LENGTH_SHORT).show();
				handler.JSONToTweet("searchresult.json");
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				
				return false;
			}
		});
		
		
		
		adapter = new TweetAdapter(this,R.layout.tweet, model.getTweetList());
		listview.setAdapter(adapter);
		model.addObserver(adapter);
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
