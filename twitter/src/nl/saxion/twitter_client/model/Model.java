package nl.saxion.twitter_client.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nl.saxion.twitter_client.ConnectionHandler;
import nl.saxion.twitter_client.objects.Tweet;

/**
 * The Model Class
 * @author Sharon and Dennis
 *
 */
public class Model extends Observable implements Observer {
	
	private String authoriseURL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&autoplay=1";
	private ConnectionHandler cHandler;

	private ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
	
	public Model() {
		setcHandler(new ConnectionHandler());
	}
	/**
	 * Adds a tweet to the arraylist tweetList
	 * @param tweet
	 */
	public void addTweet(Tweet tweet){
		tweetList.add(tweet);
		tweet.addObserver(this);
		setChanged();
		notifyObservers();
	}
	public void deleteAllTweets() {
		tweetList.clear();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * 
	 * @return Returns the arraylist tweetList
	 */
	public ArrayList<Tweet> getTweetList(){
		return tweetList;
	}

	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
		
	}
	/**
	 * @return the authoriseURL
	 */
	public String getAuthoriseURL() {
		return authoriseURL;
	}
	/**
	 * @param authoriseURL the authoriseURL to set
	 */
	public void setAuthoriseURL(String authoriseURL) {
		this.authoriseURL = authoriseURL;
		setChanged();
		notifyObservers();
	}
	/**
	 * @return the cHandler
	 */
	public ConnectionHandler getcHandler() {
		return cHandler;
	}
	/**
	 * @param cHandler the cHandler to set
	 */
	public void setcHandler(ConnectionHandler cHandler) {
		this.cHandler = cHandler;
	}
}
