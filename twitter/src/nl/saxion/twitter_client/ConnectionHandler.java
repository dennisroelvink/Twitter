package nl.saxion.twitter_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import nl.saxion.twitter_client.objects.User;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpRequest;

public class ConnectionHandler {

	private static final String CONSUMER_KEY = "qmQ1kDhXeaBFIp5Hran1XZZ9M";
	private static final String CONSUMER_SECRET = "r9YIzFiKAruyes2xNAFBSiDPgkA3TlcNqYDBUblcuUvlbtB0Y6";

	private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	private static final String ACCESSTOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String CALLBACK_URL = "http://grotekaartenkopen.nl/";
	
	private HttpRequestBase requestBase;
	private String responseString;

	private OAuthProvider provider;
	private OAuthConsumer consumer;
	private boolean loggedIn = false;
	private Model model;
	private Activity activity;
	private String url;
	private String verifyCode;

	public ConnectionHandler() {
		consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL,
				ACCESSTOKEN_URL, AUTHORIZE_URL);
	}

	public boolean isUserLoggedIn() {
		if (loggedIn) {
			return true;
		}
		return false;
	}

	public void getVerifyCodeinit(Activity activity) {
		this.activity = activity;
		TweetApplication app = (TweetApplication) activity
				.getApplicationContext();
		model = app.getModel();
		new NetworkHandler().execute();
		Log.d("INIT", "hallo");
	}

	public void getConnectionInit(String verifyCode, Activity activity) {
		TweetApplication app = (TweetApplication) activity
				.getApplicationContext();
		model = app.getModel();
		this.verifyCode = verifyCode;
		new VerifyHandler().execute();
	}

	private class NetworkHandler extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				url = provider.retrieveRequestToken(consumer, CALLBACK_URL);
				Log.d("URL", url);

			} catch (OAuthMessageSignerException e) {
				Log.d("Retrieve Error", "OAuthMessageSignerException");
			} catch (OAuthNotAuthorizedException e) {
				Log.d("Retrieve Error", "OAuthNotAuthorizedException");
			} catch (OAuthExpectationFailedException e) {
				Log.d("Retrieve Error", "OAuthExpectationFailedException");
			} catch (OAuthCommunicationException e) {
				Log.d("Retrieve Error", "OAuthCommunicationException");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			model.setAuthoriseURL(url);
		}
	}

	private class VerifyHandler extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Log.d("TOKEN", verifyCode);
				provider.retrieveAccessToken(consumer, verifyCode);
				loggedIn = true;
				Log.d("TOKEN2", verifyCode);
				Log.d("TOKEN3", consumer.getToken());
			} catch (OAuthMessageSignerException e) {
				Log.d("Retrieve Error", "OAuthMessageSignerException");
			} catch (OAuthNotAuthorizedException e) {
				Log.d("Retrieve Error", "OAuthNotAuthorizedException");
			} catch (OAuthExpectationFailedException e) {
				Log.d("Retrieve Error", "OAuthExpectationFailedException");
			} catch (OAuthCommunicationException e) {
				Log.d("Retrieve Error", "OAuthCommunicationException");
			}
			return null;
		}

	}

	public void signWithUserToken(HttpRequestBase request)throws OAuthMessageSignerException,OAuthExpectationFailedException, OAuthCommunicationException {
		assert loggedIn : "User not logged in";
		requestBase = request;
		new ReceiveCredentials().execute();
	
	

	}
	public class ReceiveCredentials extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				consumer.sign(requestBase);
			} catch (OAuthMessageSignerException e1) {
				Log.d("Response","OAuthMessagerSigner");
				e1.printStackTrace();
			} catch (OAuthExpectationFailedException e1) {
				Log.d("Response","OAuthExpectationFailed");
				e1.printStackTrace();
			} catch (OAuthCommunicationException e1) {
				Log.d("Response","OAuthCommunication error");
				e1.printStackTrace();
			}
			HttpClient client = new DefaultHttpClient();
			try {
				//HttpResponse response = client.execute(request);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            responseString = client.execute(requestBase, responseHandler);
	            
	            Log.d("Response",responseString);
			} catch (ClientProtocolException e) {
				Log.d("Response","Client protocol Exception");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("Response","IO Exception");
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			JSONHandler handler = new JSONHandler(model.getMainActivity());
			User user = handler.getUserFromJSON(responseString);
			model.setAccount(user);
			model.setFinishedMakingUser(true);
		}
		
	}

}
