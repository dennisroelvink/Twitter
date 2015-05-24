package nl.saxion.twitter_client.objects;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * The Tweet class
 * @author Sharon and Dennis
 *
 */
public class Tweet extends Observable {
	
	private ArrayList<Hashtag> hashtagList;
	private ArrayList<Url> urlList;
	private ArrayList<UserMention> mentionList;
	private Photo photo;
	private String text;
	private long tweetID;
	private User user;
	
	public Tweet(String text, User user, ArrayList<Hashtag> list, ArrayList<Url> urlList, ArrayList<UserMention> mentionList,Photo p) {
		this.text = text;
		this.user = user;
		hashtagList = list;
		this.urlList = urlList; 
		this.mentionList = mentionList;
		this.photo = p;
		
	}
	public int getMentionListSize() {
		return mentionList.size();
	}
	public UserMention getMentionAtPosition(int pos) {
		return mentionList.get(pos);
	}
	public int getUrlListSize() {
		return urlList.size();
	}
	public Url getUrlAtPosition(int pos) {
		return urlList.get(pos);
	}

	public int getHashtagListSize() {
		return hashtagList.size();
	}
	public Hashtag getHashtagAtPosition(int pos) {
		return hashtagList.get(pos);
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
		setChanged();
		notifyObservers();
	}

	/**
	 * @return the tweetID
	 */
	public long getTweetID() {
		return tweetID;
	}

	/**
	 * @param tweetID the tweetID to set
	 */
	public void setTweetID(long tweetID) {
		this.tweetID = tweetID;
		setChanged();
		notifyObservers();
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
		setChanged();
		notifyObservers();
	}
	/**
	 * @return the photo
	 */
	public Photo getPhoto() {
		return photo;
	}
	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(Photo photo) {
		this.photo = photo;
		setChanged();
		notifyObservers();
	}


	
}
