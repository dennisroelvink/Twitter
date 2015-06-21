package nl.saxion.twitter_client.objects;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * The Tweet class
 * @author Sharon and Dennis
 *
 */
public class Tweet extends Observable implements Observer {
	
	private ArrayList<Hashtag> hashtagList;
	private ArrayList<Url> urlList;
	private ArrayList<UserMention> mentionList;
	private Photo photo;
	private String text;
	private long tweetID;
	private User user;
	private String id;
	private boolean isFav = false;
	
	/**
	 * Constructor tweet
	 * @param id id
	 * @param text text
	 * @param user user
	 * @param list list hashtags
	 * @param urlList list urls
	 * @param mentionList list user mentions
	 * @param p photo
	 */
	public Tweet(String id,String text, User user, ArrayList<Hashtag> list, ArrayList<Url> urlList, ArrayList<UserMention> mentionList,Photo p) {
		this.text = text;
		this.user = user;
		hashtagList = list;
		this.urlList = urlList; 
		this.setId(id);
		this.mentionList = mentionList;
		this.photo = p;
		
	}
	
	/**
	 * Returns size of user mentionlist
	 * @return
	 */
	public int getMentionListSize() {
		return mentionList.size();
	}
	
	/**
	 * Returns user mention at given position
	 * @param pos position
	 * @return user mention object
	 */
	public UserMention getMentionAtPosition(int pos) {
		return mentionList.get(pos);
	}
	
	/**
	 * Returns url list size
	 * @return size url list
	 */
	public int getUrlListSize() {
		return urlList.size();
	}
	
	/**
	 * Returns url at given position
	 * @param pos position
	 * @return url object
	 */
	public Url getUrlAtPosition(int pos) {
		return urlList.get(pos);
	}

	/**
	 * Returns hashtag list size 
	 * @return size hashtag list
	 */
	public int getHashtagListSize() {
		return hashtagList.size();
	}
	
	/**
	 * Returns hashtag at given position
	 * @param pos position
	 * @return hashtag object
	 */
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
	
	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
		
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the isFav
	 */
	public boolean isFav() {
		return isFav;
	}

	/**
	 * @param isFav the isFav to set
	 */
	public void setFav(boolean isFav) {
		this.isFav = isFav;
		setChanged();
		notifyObservers();
	}


	
}
