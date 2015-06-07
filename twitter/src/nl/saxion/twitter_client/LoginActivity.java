package nl.saxion.twitter_client;

import java.net.URI;
import java.util.Observable;
import java.util.Observer;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity implements Observer {

	private WebView webview;
	private ConnectionHandler cHandler;
	private Model model;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		webview = (WebView) findViewById(R.id.webViewLogin);

		TweetApplication app = (TweetApplication) getApplicationContext();
		model = app.getModel();
		model.addObserver(this);
		webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(getBaseContext(), description,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.startsWith("http://grotekaartenkopen.nl")){
					Log.d("Override", "Doei nanda " + url);
					
					Uri uri = Uri.parse(url);
					
					Log.d("NANDA",uri.getQueryParameter("oauth_verifier"));
					
					
					Intent i = new Intent(LoginActivity.this,ProfileActivity.class);
					i.putExtra("verifier", uri.getQueryParameter("oauth_verifier"));
					startActivity(i);
					return true;
				} else {
					Log.d("Override", "NANDA");
					return false;
				}

			}
		});

		cHandler =model.getcHandler();
		cHandler.getVerifyCodeinit(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
		webview.loadUrl(model.getAuthoriseURL());
		setContentView(webview);
		webview.loadUrl(model.getAuthoriseURL());
		setContentView(webview);
	}

}
