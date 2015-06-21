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
 * 
 * @author Sharon and Dennis
 *
 */
public class JSONHandler {
	private Activity modelActivity;
	private Model model;

	public JSONHandler(Activity activity) {
		modelActivity = activity;

		TweetApplication app = (TweetApplication) modelActivity.getApplicationContext();
		model = app.getModel();
	}

	/**
	 * Turns a JSON object into a tweet object and puts it into the tweetList
	 * from the model class
	 * 
	 * @param filename
	 */
	public void jsonToTweet(String test) {
		JSONObject obj = getObject(test);
		JSONArray tweets = getArray(obj, "statuses");
		for(int i = 0; i < tweets.length();i++) {
			try {
				JSONObject tweet = tweets.getJSONObject(i);
				JSONObject user = getUserObject(tweet);
				JSONObject entity = getEntityObject(tweet);
				JSONArray user_mentions = getArray(entity, "user_mentions");
				JSONArray urls = getArray(entity, "urls");
				JSONArray hashtags = getArray(entity, "hashtags");
				
				// makes a photo if possible
				Photo p = new Photo("", 0, 0);
				try{
					JSONArray media = entity.getJSONArray("media");
					p = getPhotoFromMedia(media);
				} catch(JSONException e) {
					
				}
				
				
				ArrayList<Hashtag> hashtagList = getHashtagList(hashtags);
				ArrayList<Url> urlList = getUrlList(urls);
				ArrayList<UserMention> usermentionList = getUserMentionList(user_mentions);
				
				String tweetText = getTweetText(tweet);
				User un = new User(user.getString("screen_name"),user.getString("name"),user.getString("profile_image_url"));
				Tweet tweetmsg = new Tweet(tweet.getString("id_str"),tweetText, un, hashtagList, urlList, usermentionList, p);
				un.addObserver(tweetmsg);
				model.addTweet(tweetmsg);
				
			} catch (JSONException e) {
				Log.d("JSON Error","Couldn't get tweet from tweetsObject");
			}
		}
			
	}

	/**
	 * Returns the access token as string
	 * @param text jsontext
	 * @return access token
	 */
	public String getToken(String text) {
		try {
			JSONObject token = new JSONObject(text);
			Log.d("Get Acces token", token.getString("access_token"));
			String access_token = token.getString("access_token");
			return access_token;
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("JSON", "token error");
		}
		return null;

	}

	/**
	 * Get user object from jsontext
	 * @param JSONcode  jsontext
	 * @return user object
	 */
	public User getUserFromJSON(String JSONcode) {
		User user = new User("", "", "");
		try {
			JSONObject obj = new JSONObject(JSONcode);
			obj.getString("name");
			Log.d("naam", obj.getString("name"));

			user = new User(obj.getString("screen_name"),
					obj.getString("name"), obj.getString("profile_image_url"), obj.getString("followers_count"), obj.getString("friends_count"),obj.getString("statuses_count"));
			
			user.addObserver(model);
		} catch (JSONException e) {
			Log.d("JSON Error", "Couldn't retrieve user jsoncode");
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Adds an user object to the userlist
	 * @param JSONText
	 */
	public void jsonToUserList(String JSONText) {
		try{
			JSONObject obj = getObject(JSONText);
			JSONArray users = obj.getJSONArray("users");
			for(int i = 0 ; i < users.length() ; i++) {
				JSONObject user = users.getJSONObject(i);
				
				User userObject = new User(user.getString("screen_name"), user.getString("name"), user.getString("profile_image_url"));
				model.addUser(userObject);
				Log.d("Json User","Gelukt");
			}
		} catch(JSONException e) {
			Log.d("JSON Error","JSON Exception userList");
		}
		
		
	}
	/**
	 * Reading json for the profile timeline/favorites
	 * @param JSONText
	 */
	public void jsonToTweetList(String JSONText) {
		
		try {
			JSONArray tweets = getArrayFromJSONText(JSONText);
			for (int i = 0; i < tweets.length(); i++) {
				JSONObject tweet = tweets.getJSONObject(i);
				JSONObject user = getUserObject(tweet);
				JSONObject entity = getEntityObject(tweet);
				JSONArray user_mentions = getArray(entity, "user_mentions");
				JSONArray urls = getArray(entity, "urls");
				JSONArray hashtags = getArray(entity, "hashtags");

				ArrayList<Hashtag> hashtagList = getHashtagList(hashtags);
				ArrayList<Url> urlList = getUrlList(urls);
				ArrayList<UserMention> usermentionList = getUserMentionList(user_mentions);
				Photo p = new Photo("", 0, 0);
				try{
					JSONArray media = entity.getJSONArray("media");
					p = getPhotoFromMedia(media);
				} catch(JSONException e) {
					
				}

				Log.d("Dimitri", getTweetText(tweet));
				
				User un = new User(user.getString("screen_name"), user.getString("name"),user.getString("profile_image_url"));
				Tweet t = new Tweet(tweet.getString("id_str"),tweet.getString("text"), un, hashtagList, urlList, usermentionList, p);
				if(tweet.getBoolean("favorited") == true) {
					t.setFav(true);
				}
				un.addObserver(t);
				model.addTimelineTweet(t);
				
			}

		} catch (JSONException e) {
			Log.d("JSON Error", "Couldn't retrieve timeline array");
		}
	}

	/**
	 * Making a json object from jsontext
	 * @param JSONText
	 * @return json object
	 */
	private JSONObject getObject(String JSONText) {

		JSONObject obj = null;
		try {
			obj = new JSONObject(JSONText);
		} catch (JSONException e) {
			Log.d("JSON error", "Couldn't create json object");
		}
		return obj;
	}
	
	/**
	 * Making an user object from json text
	 * @param tweet tweet object
	 * @return json user object
	 */
	private JSONObject getUserObject(JSONObject tweet) {

		JSONObject obj = null;
		try {
			obj = tweet.getJSONObject("user");
		} catch (JSONException e) {
			Log.d("JSON error", "Couldn't create json object");
		}
		return obj;
	}

	/**
	 * Makes an arrayobject from json text
	 * @param obj jsonobject
	 * @param JSONText jsontext
	 * @return jsonarray object
	 */
	private JSONArray getArray(JSONObject obj, String JSONText)  {

		JSONArray a = null;

		try {
			a = obj.getJSONArray(JSONText);
			return a;
		} catch (JSONException e) {
			Log.d("JSON error handler", "couldn't get an array object");
			e.printStackTrace();
		}

		return a;

	}

	/**
	 * Returns a jsonarray with tweets
	 * @param JSONText jsontext
	 * @return tweets
	 */
	private JSONArray getArrayFromJSONText(String JSONText) {

		JSONArray obj = null;
		try {
			obj = new JSONArray(JSONText);
			return obj;
		} catch (JSONException e) {
			Log.d("JSON error", "Couldn't create json array");
		}
		return obj;

	}

	/**
	 * Returns a string with the tweet text
	 * @param obj jsonobject
	 * @return tweet text
	 */
	private String getTweetText(JSONObject obj) {
		String text = null;
		try {
			text = obj.getString("text");
		} catch (JSONException e) {
			Log.d("JSON Error", "Can't get Tweet Text");
		}
		return text;
	}

	/**
	 * Returns an entity object
	 * @param tweet jsonObject tweet
	 * @return entity jsonobject
	 */
	private JSONObject getEntityObject(JSONObject tweet) {
		JSONObject obj = null;
		try {
			obj = tweet.getJSONObject("entities");
		} catch (JSONException e) {
			Log.d("JSON Error", "Can't get Entity object");
		}
		return obj;
	}

	/**
	 * Returns arraylist with hashtags
	 * @param hashtags jsonarray with hastags
	 * @return list with hastags
	 */
	private ArrayList<Hashtag> getHashtagList(JSONArray hashtags) {
		ArrayList<Hashtag> hashTaglist = new ArrayList<Hashtag>();

		for (int i = 0; i < hashtags.length(); i++) {
			try {
				JSONObject hashtag = hashtags.getJSONObject(i);
				JSONArray indices = hashtag.getJSONArray("indices");

				if (indices.length() != 0) {
					Hashtag tag = new Hashtag(hashtag.getString("text"),
							indices.getInt(0), indices.getInt(1));
					hashTaglist.add(tag);
				}
			} catch (JSONException h) {
				Log.d("JSON Error", "Can't get hashtag from JSON");
			}
		}
		return hashTaglist;
	}

	/**
	 * Returns arraylist with urls
	 * @param urls jsonarray with urls
	 * @return arraylist with urls
	 */
	private ArrayList<Url> getUrlList(JSONArray urls) {
		ArrayList<Url> urlList = new ArrayList<Url>();

		for (int k = 0; k < urls.length(); k++) {
			try {
				JSONObject url = urls.getJSONObject(k);
				JSONArray indices = url.getJSONArray("indices");

				if (indices.length() != 0) {
					Url u = new Url(url.getString("url"), indices.getInt(0),
							indices.getInt(1));
					urlList.add(u);
				}
			} catch (JSONException e) {
				Log.d("JSON Error", "Can't get url from JSON");
				e.printStackTrace();
			}

		}
		return urlList;
	}
	
	/**
	 * Returns arraylist with user mentions
	 * @param userMentions jsonarray with user mentions
	 * @return usermentions in an arraylist
	 */
	private ArrayList<UserMention> getUserMentionList(JSONArray userMentions){
		ArrayList<UserMention> mentionList = new ArrayList<UserMention>();
		
		for (int l = 0; l < userMentions.length(); l++) {
			try {
				JSONObject userMention = userMentions.getJSONObject(l);
				JSONArray indices = userMention.getJSONArray("indices");
				if (indices.length() != 0) {
					UserMention UM = new UserMention(
							userMention.getString("screen_name"),
							indices.getInt(0), indices.getInt(1));
					mentionList.add(UM);
				}
			} catch (JSONException e) {
				Log.d("JSON Error", "Can't get user_mention from JSON");
				e.printStackTrace();
			}

		}
		return mentionList;
	}
	
	/**
	 * returns a photo from a tweet
	 * @param media jsonarray with media
	 * @return a photo
	 */
	private Photo getPhotoFromMedia(JSONArray media) {
		Photo p = new Photo("", 0, 0);
		try {
			for (int a = 0; a < media.length(); a++) {
				JSONObject med = media.getJSONObject(a);
				JSONArray indices = med.getJSONArray("indices");
				if (indices.length() != 0) {
					p.setPhotoURL(med.getString("media_url"));
					p.setBeginPhotoURL(indices.getInt(0));
					p.setEndPhotoURL(indices.getInt(1));

				}
				Log.d("MEDIA", med.getString("media_url"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("JSON Error", "Couldn't get Media");
		}
		return p;
	}

}
