package nl.saxion.twitter_client;


import java.util.ArrayList;
import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import nl.saxion.twitter_client.objects.Hashtag;
import nl.saxion.twitter_client.objects.Photo;
import nl.saxion.twitter_client.objects.Tweet;
import nl.saxion.twitter_client.objects.Url;
import nl.saxion.twitter_client.objects.User;
import nl.saxion.twitter_client.objects.UserMention;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.util.Log;

/**
 * The JSONHandler class
 * @author Sharon and Dennis
 *
 */
public class JSONHandler {
	private Activity mainActivity;
	private Model model;
	
	public JSONHandler(Activity activity) {
		mainActivity = activity;
		
		TweetApplication app = (TweetApplication) mainActivity.getApplicationContext();
		model = app.getModel();
	}
	
	/**
	 * Turns a JSON object into a tweet object and puts it into the tweetList from the model class
	 * @param filename
	 */
	public void JSONToTweet(String test) {
		try {
		
			JSONObject obj = new JSONObject(test);
			Log.d("Test","Test");
			
			JSONArray tweets = obj.getJSONArray("statuses");
			Log.d("Check", "Test1");
			for(int i = 0; i < tweets.length() ; i ++) {
				JSONObject tweet = tweets.getJSONObject(i);
				Log.d("tweet",tweet.toString());
				JSONObject user = tweet.getJSONObject("user");
				Log.d("user",user.toString());
				JSONObject entity = tweet.getJSONObject("entities");
				Log.d("entity",entity.toString());
				JSONArray hashtags = entity.getJSONArray("hashtags");
				Log.d("hashtags",hashtags.toString());
				JSONArray urls = entity.getJSONArray("urls");
				Log.d("urls",urls.toString());
				JSONArray userMentions = entity.getJSONArray("user_mentions");
				Log.d("usermentions",userMentions.toString());
				Photo p = new Photo("",0,0);
				try {
					JSONArray media = entity.getJSONArray("media");
					
					for(int a = 0 ; a < media.length(); a++) {
						JSONObject med = media.getJSONObject(a);
						JSONArray indices = med.getJSONArray("indices");
						if(indices.length() != 0) {
							p.setPhotoURL(med.getString("media_url"));
							p.setBeginPhotoURL(indices.getInt(0));
							p.setEndPhotoURL(indices.getInt(1));
							
						}
						Log.d("MEDIA",med.getString("media_url"));
					}
				}catch(JSONException e){
					e.printStackTrace();
					Log.d("JSON","Media Error");
				}			
			
				
				ArrayList<Hashtag> list = new ArrayList<Hashtag>();
				ArrayList<Url>urlList = new ArrayList<Url>();
				ArrayList<UserMention> mentionList = new ArrayList<UserMention>();
				
				
				// makes a hashtag object
				for(int j = 0 ; j < hashtags.length() ; j ++ ) {
					JSONObject hashtag = hashtags.getJSONObject(j);	
					JSONArray indices = hashtag.getJSONArray("indices");
					
					if(indices.length() != 0) {
						Hashtag tag = new Hashtag(hashtag.getString("text"), indices.getInt(0), indices.getInt(1));
						list.add(tag);
					}
					
				}
				// makes an url object
				for(int k = 0; k < urls.length(); k++){
					try {
						JSONObject url = urls.getJSONObject(k);
						JSONArray indices = url.getJSONArray("indices");
						
						if(indices.length() != 0){
							Url u = new Url(url.getString("url"), indices.getInt(0), indices.getInt(1));
							urlList.add(u);
						}
					} catch (JSONException e) {
						Log.d("JSON","URL Error");
						e.printStackTrace();
					}

				}
				// makes an user mention object
				for(int l = 0; l < userMentions.length() ; l++) {
					try {
						JSONObject userMention = userMentions.getJSONObject(l);
						JSONArray indices = userMention.getJSONArray("indices");
						if(indices.length() != 0){
							UserMention UM = new UserMention(userMention.getString("screen_name"), indices.getInt(0), indices.getInt(1));
							mentionList.add(UM);
						}
					} catch (JSONException e) {
						Log.d("JSON","User Mention Error");
						e.printStackTrace();
					}

				}
				
				Log.d("Check", "Test2");
				String tweetText = tweet.getString("text");
				User un = new User(user.getString("screen_name"), user.getString("name"), user.getString("profile_image_url"));
				Tweet tweetmsg = new Tweet(tweetText,un, list, urlList, mentionList,p);
				model.addTweet(tweetmsg);
				
			}
			

		} catch (JSONException e1) {
			Log.d("error", "Uitlees error");
			e1.printStackTrace();
		}
	}
//	/**
//	 * Reads the JSON file and turns it into one string and returns this string
//	 * @param filename
//	 * @return The JSON file string
//	 * @throws IOException
//	 */
//    @SuppressWarnings("unused")
//	private String readAssetIntoString(String filename) throws IOException {
//		BufferedReader br = null;
//		StringBuilder sb = new StringBuilder();
// 
//		String line;
//		try {
//			InputStream is = mainActivity.getAssets().open(filename, AssetManager.ACCESS_BUFFER);
//			br = new BufferedReader(new InputStreamReader(is));
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//            throw e;
//		} finally {
//			if (br != null) {
//				try {
//					br.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return sb.toString();		
//	}
    
    public String getToken(String text) {
		try {
			JSONObject token = new JSONObject(text);
			Log.d("Pikachu",token.getString("access_token"));
			String access_token = token.getString("access_token");
			return access_token;
		} catch(JSONException e){
			e.printStackTrace();
			Log.d("JSON","Media Error");
		}
		return null;	

    }

}
