package nl.saxion.twitter_client.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import nl.saxion.twitter_client.objects.Tweet;

/**
 * The Model Class
 * @author Sharon and Dennis
 *
 */
public class Model extends Observable implements Observer {

	private ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
	
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
}
