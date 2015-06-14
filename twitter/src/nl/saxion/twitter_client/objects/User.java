
package nl.saxion.twitter_client.objects;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * The User class
 * @author Sharon and Dennis
 *
 */
public class User extends Observable{
	
	private long userID;
	private String name;
	private String userName;
	private String profilePhotoUrl;
	private String tweetsSent = "";
	private String following = "";
	private String followers = "";
	private Bitmap bitmap;
	
	/**
	 * Constructor of the User class
	 * @param userName username of the twitteruser
	 * @param name name of the twitteruser
	 * @param photo profile picture of the twitteruser
	 */
	public User (String userName, String name, String photo) {
		this.name = name;
		this.userName = userName;
		this.setProfilePhotoUrl(photo);
		new URLHandler().execute();
	}
	
	/**
	 * Constructor of the User class
	 * @param userName username of the twitteruser
	 * @param name name of the twitteruser
	 * @param photo profile picture of the twitteruser
	 * @param followers amount of followers
	 * @param following amount of people who the user follows
	 * @param friends amount of friends
	 */
	public User (String userName, String name, String photo, String followers, String following, String tweets) {
		this.name = name;
		this.userName = userName;
		this.setProfilePhotoUrl(photo);
		this.tweetsSent = tweets;
		this.followers = followers;
		this.following = following;
		new URLHandler().execute();
	}

	/**
	 * @return the friends
	 */
	public String getTweetsSent() {
		return tweetsSent;
	}
	/**
	 * @return the following
	 */
	public String getFollowing() {
		return following;
	}
	/**
	 * @return the followers
	 */
	public String getFollowers() {
		return followers;
	}
	/**
	 * @return the userID
	 */
	public long getUserID() {
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(long userID) {
		this.userID = userID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the profilePhotoUrl
	 */
	public String getProfilePhotoUrl() {
		return profilePhotoUrl;
	}

	/**
	 * @param set the profilePhotoUrl
	 */
	public void setProfilePhotoUrl(String profilePhotoUrl) {
		this.profilePhotoUrl = profilePhotoUrl;
	}
	/**
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}


	/**
	 * @param bitmap the bitmap to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * The URLHandler class
	 * @author Sharon and Dennis
	 * 
	 * Handles the exception which occurred when converting the url into an imageview
	 */
	private final class URLHandler extends AsyncTask<Void,Void,Void> {
		URL imageurl = null;
		Bitmap bitmap = null;
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				imageurl = new URL(getProfilePhotoUrl());
				bitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());

				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			setBitmap(bitmap);
			setChanged();
			notifyObservers();
			super.onPostExecute(result);
		}
	}

}
