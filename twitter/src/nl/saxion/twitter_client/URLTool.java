package nl.saxion.twitter_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class URLTool {

	private static final String API_KEY = "qmQ1kDhXeaBFIp5Hran1XZZ9M";
	private static final String API_SECRET = "r9YIzFiKAruyes2xNAFBSiDPgkA3TlcNqYDBUblcuUvlbtB0Y6";
	private String authString = API_KEY + ":" + API_SECRET;
	private String base64 = Base64.encodeToString(authString.getBytes(),
			Base64.NO_WRAP);

	private String JSONText;
	private JSONHandler handler;
	private String url;

	private boolean searching = true;

	public URLTool(Activity activity) {
		Log.d("Conchita", "Execute URLHandler ofzo");
		handler = new JSONHandler(activity);
	}

	public void ExecuteHandler() {
		new URLHandler().execute();
	}

	/**
	 * @return the jSONText
	 */
	public String getJSONText() {
		return JSONText;
	}

	/**
	 * @param jSONText
	 *            the jSONText to set
	 */
	public void setJSONText(String jSONText) {
		JSONText = jSONText;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the searching
	 */
	public boolean isSearching() {
		return searching;
	}

	/**
	 * @param searching
	 *            the searching to set
	 */
	public void setSearching(boolean searching) {
		this.searching = searching;
	}

	/**
	 * Gets Twitter messages given a certain word and puts them into the
	 * listview on the mainscreen.
	 * 
	 * @author Sharon and Dennis
	 *
	 */
	private final class URLHandler extends AsyncTask<Void, Void, Void> {

		/**
		 * Gets Twitter messages given a certain word, this method runs in the
		 * background of the application
		 */
		@Override
		protected Void doInBackground(Void... params) {
			setSearching(false);
			String token = "";

			HttpPost request = new HttpPost(
					"https://api.twitter.com/oauth2/token");

			request.setHeader("Authorization", "Basic " + base64);
			request.setHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");

			try {
				request.setEntity(new StringEntity(
						"grant_type=client_credentials"));
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				StringBuilder requestBuilder = new StringBuilder();
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					requestBuilder.append(line);
				}

				Log.d("Michael Jackson", requestBuilder.toString());
				token = handler.getToken(requestBuilder.toString());

			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			Log.d("URL", url);
			HttpGet httpGet = new HttpGet(url);
			try {
				httpGet.setHeader("Authorization", "Bearer " + token);
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;

				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				setJSONText(builder.toString());
				Log.d("Carrie", builder.toString());

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

		}

		/**
		 * Waits until the doInBackground method is finished
		 */
		@Override
		protected void onPostExecute(Void result) {
			handler.JSONToTweet(getJSONText());
			super.onPostExecute(result);
			setSearching(true);
		}

	}
}
