package nl.saxion.twitter_client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import nl.saxion.twitter_client.ConnectionHandler;
import nl.saxion.twitter_client.objects.Tweet;
import nl.saxion.twitter_client.objects.User;

/**
 * The Model Class
 * @author Sharon and Dennis
 *
 */
public class Model extends Observable implements Observer {
	
	private String authoriseURL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&autoplay=1";
	private ConnectionHandler cHandler;
	private User account = new User("", "", "");
	private Activity mainActivity;
	private boolean isFinishedMakingUser = false;
	private boolean goBackToMain = false;
	private String token;
	private String secret;
	private ArrayList<Tweet> tweetList = new ArrayList<Tweet>();
	private ArrayList<Tweet> timelineList = new ArrayList<Tweet>();
	private ArrayList<User> userList = new ArrayList<User>();
	
	public Model() {
		setcHandler(new ConnectionHandler(this));
	}
	
	/**
	 * Adds user to userlist
	 * @param user
	 */
	public void addUser(User user) {
		user.addObserver(this);
		userList.add(user);
		setChanged();
		notifyObservers();
	}

	/**
	 * Clears userlist
	 */
	public void clearUserList(){
		userList.clear();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns userlist
	 * @return
	 */
	public List<User> getUserList() {
		return userList;
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
	
	/**
	 * Clears the tweetlist
	 */
	public void deleteAllTweets() {
		tweetList.clear();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Adds a tweet to the timeline arraylist
	 * @param t tweet object
	 */
	public void addTimelineTweet(Tweet t){
		timelineList.add(t);
		t.addObserver(this);
		setChanged();
		notifyObservers();
	}
	/**
	 * Clears the timline arraylist
	 */
	public void deleteTimeLine() {
		timelineList.clear();
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
	
	/**
	 * Returns tweet at given position
	 * @param pos position
	 * @return tweet object
	 */
	public Tweet getTweetAtPos(int pos) {
		return timelineList.get(pos);
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
	
	/**
	 * @return the account
	 */
	public User getAccount() {
		return account;
	}
	
	/**
	 * @param account the account to set
	 */
	public void setAccount(User account) {
		this.account = account;
	}
	
	/**
	 * @return the mainActivity
	 */
	public Activity getMainActivity() {
		return mainActivity;
	}
	
	/**
	 * @param mainActivity the mainActivity to set
	 */
	public void setMainActivity(Activity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	/**
	 * @return the isFinishedMakingUser
	 */
	public boolean isFinishedMakingUser() {
		return isFinishedMakingUser;
	}
	
	/**
	 * @param isFinishedMakingUser the isFinishedMakingUser to set
	 */
	public void setFinishedMakingUser(boolean isFinishedMakingUser) {
		this.isFinishedMakingUser = isFinishedMakingUser;
	}
	
	/**
	 * @return the goBackToMain
	 */
	public boolean isGoBackToMain() {
		return goBackToMain;
	}
	
	/**
	 * @param goBackToMain the goBackToMain to set
	 */
	public void setGoBackToMain(boolean goBackToMain) {
		this.goBackToMain = goBackToMain;
	}
	
	/**
	 * Returns an arraylist with tweet object for the timeline
	 * @return timlineList
	 */
	public List<Tweet> getTimeLine() {
		return timelineList;

	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

}
