package nl.saxion.twitter_client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import android.support.v7.app.ActionBarActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The SearchActivity class
 * 
 * @author Sharon and Dennis
 *
 */
public class SearchActivity extends ActionBarActivity {

	private Model model;
	private ListView listview;
	private TweetAdapter adapter;
	private SearchView searcher;
	private URLTool urlTool;
	
	private static final String PREFS = "LOGINTOKEN";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();
		urlTool = new URLTool(this);
		
		searcher = (SearchView) findViewById(R.id.searchViewTweetSearch);
		listview = (ListView) findViewById(R.id.listViewTweet);
		
		/**
		 * Method that will be called when pressing enter
		 */
		searcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				if(!urlTool.isSearching()) return true;
				
				model.deleteAllTweets();
				Log.d("JSONTEXT", "" + urlTool.getJSONText());
				if(query.length() != 0) {
					try {
						String input = URLEncoder.encode(query, "UTF-8");
						String HTTPSrequest = "https://api.twitter.com/1.1/search/tweets.json?q="+ input+"&count=100";
						urlTool.setUrl(HTTPSrequest);
						urlTool.executeHandler();
						
						searcher.clearFocus();
					} catch (UnsupportedEncodingException e) {
						Log.d("Error httprequest","request is niet geldig");
						e.printStackTrace();
					}
				}


				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				return false;
			}
		});

		adapter = new TweetAdapter(this, R.layout.tweet, model.getTweetList());
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
