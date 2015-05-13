package nl.saxion.twitter_client.model;

import android.app.Application;

/**
 * The TweetApplication class
 * @author Sharon and Dennis
 *
 */
public class TweetApplication extends Application{

	private Model model;
	
	public TweetApplication(){
		this.model = new Model();
	}

	/**
	 * 
	 * @return Returns the model
	 */
	public Model getModel(){
		return model;
	}
}
