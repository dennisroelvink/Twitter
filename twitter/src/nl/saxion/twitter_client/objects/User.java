
package nl.saxion.twitter_client.objects;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * The User class
 * @author Sharon and Dennis
 *
 */
public class User {
	
	private long userID;
	private String name;
	private String userName;
	private String profilePhotoUrl;
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
	}
	/**
	 * The URLHandler class
	 * @author Sharon and Dennis
	 * 
	 * Handles the exception which occurred when converting the url into an imageview
	 */
	private final class URLHandler extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				URL Imageurl = new URL(getProfilePhotoUrl());
				setBitmap(BitmapFactory.decodeStream(Imageurl.openConnection().getInputStream()));
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException r) {
				r.printStackTrace();
			}
			
			return null;
		}
		
	}

}
