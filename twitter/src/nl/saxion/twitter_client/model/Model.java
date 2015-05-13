package nl.saxion.twitter_client.model;

import java.util.ArrayList;
import java.util.Observable;

import nl.saxion.twitter_client.objects.Tweet;
import nl.saxion.twitter_client.objects.User;
/**
 * The Model Class
 * @author Sharon and Dennis
 *
 */
public class Model extends Observable {

	private ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
	
	/**
	 * Adds a tweet to the arraylist tweetList
	 * @param tweet
	 */
	public void addTweet(Tweet tweet){
		tweetList.add(tweet);
	}
	
	/**
	 * 
	 * @return Returns the arraylist tweetList
	 */
	public ArrayList<Tweet> getTweetList(){
		return tweetList;
	}
}
