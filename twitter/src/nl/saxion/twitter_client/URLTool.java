package nl.saxion.twitter_client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;


public class URLTool {
	
	private static final String API_KEY = "qmQ1kDhXeaBFIp5Hran1XZZ9M" ;
	private static final String API_SECRET = "r9YIzFiKAruyes2xNAFBSiDPgkA3TlcNqYDBUblcuUvlbtB0Y6";
	private String authString = API_KEY + ":" + API_SECRET;
	private String base64 = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
	
	private InputStream is;
	private String JSONText;
	
	public URLTool(Activity activity) {
		Log.d("Conchita","Execute URLHandler ofzo");
		new URLHandler().execute();
	}

	
	/**
	 * @return the jSONText
	 */
	public String getJSONText() {
		return JSONText;
	}


	/**
	 * @param jSONText the jSONText to set
	 */
	public void setJSONText(String jSONText) {
		JSONText = jSONText;
	}


	private final class URLHandler extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			String token = "";
			
			HttpPost request = new HttpPost("https://api.twitter.com/oauth2/token");
			
			request.setHeader("Authorization", "Basic " + base64);
			request.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			
			try {
				request.setEntity(new StringEntity("grant_type=client_credentials"));
				 HttpClient client = new DefaultHttpClient();
				 HttpResponse response = client.execute(request);
				 StringBuilder requestBuilder = new StringBuilder();
				 HttpEntity entity = response.getEntity();
			       InputStream content = entity.getContent();
			       BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			       String line;
			       while((line = reader.readLine()) != null){
			    	   requestBuilder.append(line);
			       }
			       //JSONHandler handler = new JSONHandler(null);
			       //handler.getToken(requestBuilder.toString());
			       Log.d("Michael Jackson",requestBuilder.toString());
			       
			       
			       
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String url = "https://api.twitter.com/1.1/search/tweets.json?q=%23aanhetprogrammerenmetdennis";
			     StringBuilder builder = new StringBuilder();
			     HttpClient client = new DefaultHttpClient();
			     HttpGet httpGet = new HttpGet(url);
			     try{
			      HttpResponse response = client.execute(httpGet);
			      StatusLine statusLine = response.getStatusLine();
			      int statusCode = statusLine.getStatusCode();
			       HttpEntity entity = response.getEntity();
			       InputStream content = entity.getContent();
			       BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			       String line;
			       while((line = reader.readLine()) != null){
			    	   builder.append(line);
			       }
			       
			       setJSONText(builder.toString());
			       Log.d("Carrie",builder.toString());
			       
			     } catch(ClientProtocolException e){
			      e.printStackTrace();
			     } catch (IOException e){
			      e.printStackTrace();
			     }
			
			
//			String url = "https://api.twitter.com/1.1/search/tweets.json?q=%23aanhetprogrammerenmetdennis";
//			HttpClient client = new DefaultHttpClient();
//			HttpGet httpGet = new HttpGet(url);
//			
//			httpGet.setHeader("Authorization", "Bearer " + token);
//			ResponseHandler<String> handler = new BasicResponseHandler();
//			try {
//				String result = client.execute(httpGet, handler);
//				Log.d("Rihanna",result);
//			} catch (ClientProtocolException e) {
//				Log.d("Rihanna","dut nie1");
//				e.printStackTrace();
//			} catch (IOException e) {
//				Log.d("Rihanna","dut nie2");
//				e.printStackTrace();
//			}
			
			return null;
			
			
		}
		
	}
}
