package nl.saxion.twitter_client.objects;

/**
 * The user mention class
 * @author Sharon en Dennis
 *
 */
public class UserMention {
	private String screenName;
	private int beginMention;
	private int endMention;
	
	public UserMention(String screenName, int beginMention,int endMention){
		this.screenName = screenName;
		this.beginMention = beginMention;
		this.endMention = endMention;
	}
	
	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the beginMention
	 */
	public int getBeginMention() {
		return beginMention;
	}

	/**
	 * @param beginMention the beginMention to set
	 */
	public void setBeginMention(int beginMention) {
		this.beginMention = beginMention;
	}

	/**
	 * @return the endMention
	 */
	public int getEndMention() {
		return endMention;
	}

	/**
	 * @param endMention the endMention to set
	 */
	public void setEndMention(int endMention) {
		this.endMention = endMention;
	}

}
