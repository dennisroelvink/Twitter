package nl.saxion.twitter_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Observable;

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
import nl.saxion.twitter_client.objects.Tweet;
import nl.saxion.twitter_client.objects.User;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

/**
 * The ConnectionHandler activity
 * @author Sharon and Dennis
 *
 */
public class ConnectionHandler extends Observable {

	private static final String CONSUMER_KEY = "qmQ1kDhXeaBFIp5Hran1XZZ9M";
	private static final String CONSUMER_SECRET = "r9YIzFiKAruyes2xNAFBSiDPgkA3TlcNqYDBUblcuUvlbtB0Y6";

	private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	private static final String ACCESSTOKEN_URL = "https://api.twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String CALLBACK_URL = "http://grotekaartenkopen.nl/";
	
	private static final String PREFS = "LOGINTOKEN";
	
	private HttpRequestBase requestBase;
	private String responseString = "";
	private HttpRequestBase requestBaseTimeline ;
	private HttpRequestBase requestBasePostTweet;
	private HttpRequestBase requestBaseUserList;
	private OAuthProvider provider;
	private OAuthConsumer consumer;
	private boolean loggedIn = false;
	private Model model;
	private Activity activity;
	private String url;
	private String verifyCode;

	/**
	 * Constructor connectionhandler
	 * @param model model 
	 */
	public ConnectionHandler(Model model) {
		this.model = model;
		consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL,
				ACCESSTOKEN_URL, AUTHORIZE_URL);
	}
	/**
	 * Returns the oAuth consumer
	 * @return consumer
	 */
	public OAuthConsumer getConsumer(){
		return consumer;
		
	}

	/**
	 * Returns of the user has logged in or not
	 * @return logged in true or false
	 */
	public boolean isUserLoggedIn() {
		if (loggedIn) {
			return true;
		}
		return false;
	}

	/**
	 * Initializes the network handler
	 * @param activity activity
	 */
	public void getVerifyCodeinit(Activity activity) {
		this.activity = activity;
		TweetApplication app = (TweetApplication) activity
				.getApplicationContext();
		model = app.getModel();
		new NetworkHandler().execute();
		Log.d("INIT", "hallo");
	}

	/**
	 * Initializes the verify handler
	 * @param verifyCode verifyCode
	 * @param activity activity
	 */
	public void getConnectionInit(String verifyCode, Activity activity) {
		TweetApplication app = (TweetApplication) activity
				.getApplicationContext();
		model = app.getModel();
		this.verifyCode = verifyCode;
		new VerifyHandler().execute();
	}

	/**
	 * Handles the creation of a request token
	 * @author Sharon and Dennis
	 *
	 */
	private class NetworkHandler extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				url = provider.retrieveRequestToken(consumer, CALLBACK_URL);
			

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

	/**
	 * Handles  the creation of an access token
	 * @author Sharon and Dennis
	 *
	 */
	private class VerifyHandler extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				provider.retrieveAccessToken(consumer, verifyCode);
				loggedIn = true;
				Log.d("Verify code", verifyCode);
				Log.d("TOKEN consumer", consumer.getToken());


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
			model.setToken(consumer.getToken());
			model.setSecret(consumer.getTokenSecret());
			SharedPreferences prefs = activity.getSharedPreferences(PREFS, 0);
			Editor editor = prefs.edit();
			editor.putString("token", consumer.getToken());
			editor.putString("tokenSecret", consumer.getTokenSecret());
			editor.commit();
		}
	}

	/**
	 * Signs the request and executes ReceiveCredentials
	 * @param request httpRequest
	 * @throws OAuthMessageSignerException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthCommunicationException
	 */
	public void signWithUserTokenCredentials(HttpRequestBase request)throws OAuthMessageSignerException,OAuthExpectationFailedException, OAuthCommunicationException {
		assert loggedIn : "User not logged in";
		requestBase = request;
		new ReceiveCredentials().execute();
	
	}
	
	/**
	 * Signs the request and executes ReceiveTweetList
	 * @param request httpRequest
	 * @throws OAuthMessageSignerException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthCommunicationException
	 */
	public void signWithUserTokenGetTweetList(HttpRequestBase request)throws OAuthMessageSignerException,OAuthExpectationFailedException, OAuthCommunicationException {
		assert loggedIn : "User not logged in";
		requestBaseTimeline = request;
		new ReceiveTweetList().execute();
		
	}
	
	/**
	 * Signs the request and executes PostRequestHandler
	 * @param request httpRequest
	 * @throws OAuthMessageSignerException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthCommunicationException
	 */
	public void signWithUserTokenPostRequest(HttpRequestBase request)throws OAuthMessageSignerException,OAuthExpectationFailedException, OAuthCommunicationException {
		assert loggedIn : "User not logged in";
		requestBasePostTweet = request;
		new PostRequestHandler().execute();
		
	}
	/**
	 * Signs the request and executes GetUserList
	 * @param request
	 * @throws OAuthMessageSignerException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthCommunicationException
	 */
	public void signWithUserTokenUserList(HttpRequestBase request)throws OAuthMessageSignerException,OAuthExpectationFailedException, OAuthCommunicationException {
		assert loggedIn : "User not logged in";
		requestBaseUserList = request;
		new ReceiveUserList().execute();
	}
	
	/**
	 * Updates the model with an userlist
	 * @author Sharon and Dennis
	 *
	 */
	private class ReceiveUserList extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				consumer.sign(requestBaseUserList);
			} catch (OAuthMessageSignerException e1) {
				Log.d("SResponse","OAuthMessagerSigner");
				e1.printStackTrace();
			} catch (OAuthExpectationFailedException e1) {
				Log.d("SResponse","OAuthExpectationFailed");
				e1.printStackTrace();
			} catch (OAuthCommunicationException e1) {
				Log.d("SResponse","OAuthCommunication error");
				e1.printStackTrace();
			}
			HttpClient client = new DefaultHttpClient();
			try {
			
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            responseString = client.execute(requestBaseUserList, responseHandler);
	            
			} catch (ClientProtocolException e) {
				Log.d("CResponse","Client protocol Exception");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("CResponse","IO Exception");
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			Log.d("Onpost",""+responseString);
			JSONHandler handler = new JSONHandler(model.getMainActivity());
			handler.jsonToUserList(responseString);
			setChanged();
			notifyObservers();
		}
		
	}
	
	/**
	 * Updates the model with user credentials
	 * @author Sharon and Dennis
	 *
	 */
	private class ReceiveCredentials extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				consumer.sign(requestBase);
			} catch (OAuthMessageSignerException e1) {
				Log.d("SResponse","OAuthMessagerSigner");
				e1.printStackTrace();
			} catch (OAuthExpectationFailedException e1) {
				Log.d("SResponse","OAuthExpectationFailed");
				e1.printStackTrace();
			} catch (OAuthCommunicationException e1) {
				Log.d("SResponse","OAuthCommunication error");
				e1.printStackTrace();
			}
			HttpClient client = new DefaultHttpClient();
			try {
			
				
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            responseString = client.execute(requestBase, responseHandler);
	           
			} catch (ClientProtocolException e) {
				Log.d("CResponse","Client protocol Exception");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("CResponse","IO Exception");
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			Log.d("Onpost",""+responseString);
			JSONHandler handler = new JSONHandler(model.getMainActivity());
			User user = handler.getUserFromJSON(responseString);
			model.setAccount(user);
			model.setFinishedMakingUser(true);
			setChanged();
			notifyObservers();
		}
		
	}
	
	/**
	 * Updates the model with the tweetlist
	 * @author Sharon and Dennis
	 *
	 */
	private class ReceiveTweetList extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				consumer.sign(requestBaseTimeline);
			} catch (OAuthMessageSignerException e1) {
				Log.d("SResponse","OAuthMessagerSigner");
				e1.printStackTrace();
			} catch (OAuthExpectationFailedException e1) {
				Log.d("SResponse","OAuthExpectationFailed");
				e1.printStackTrace();
			} catch (OAuthCommunicationException e1) {
				Log.d("SResponse","OAuthCommunication error");
				e1.printStackTrace();
			}
			HttpClient client = new DefaultHttpClient();
			try {
			
				
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            responseString = client.execute(requestBaseTimeline, responseHandler);
	            
			} catch (ClientProtocolException e) {
				Log.d("CResponse","Client protocol Exception");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("CResponse","IO Exception");
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			Log.d("Onpost2",""+responseString);
			JSONHandler handler = new JSONHandler(model.getMainActivity());
			handler.jsonToTweetList(responseString);
			
			setChanged();
			notifyObservers();
		}
		
	}
	
	/**
	 * Posts the request
	 * @author Sharon and Dennis
	 *
	 */
	private class PostRequestHandler extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				consumer.sign(requestBasePostTweet);
			} catch (OAuthMessageSignerException e1) {
				Log.d("SResponse","OAuthMessagerSigner");
				e1.printStackTrace();
			} catch (OAuthExpectationFailedException e1) {
				Log.d("SResponse","OAuthExpectationFailed");
				e1.printStackTrace();
			} catch (OAuthCommunicationException e1) {
				Log.d("SResponse","OAuthCommunication error");
				e1.printStackTrace();
			}
			HttpClient client = new DefaultHttpClient();
			try {
			
				
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            responseString = client.execute(requestBasePostTweet, responseHandler);
	            
			} catch (ClientProtocolException e) {
				Log.d("CResponse","Client protocol Exception");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("CResponse","IO Exception");
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			Log.d("Onpost finished",""+responseString);
			
		}
		
	}
	
	/**
	 * returns the response string
	 * @return responseString
	 */
	public String getResponseString() {
		return this.responseString;
	}

}
