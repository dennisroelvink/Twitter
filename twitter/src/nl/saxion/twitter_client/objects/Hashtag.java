package nl.saxion.twitter_client.objects;

/**
 * The Hashtag class
 * @author Sharon and Dennis
 *
 */
public class Hashtag {

	private String hashText;
	private int beginHash;
	private int endHash;
	
	/**
	 * The hashtag constructor
	 * @param hashText hashtag text
	 * @param beginHash beginning of the hashtag
	 * @param endHash end of the hashtag
	 */
	public Hashtag(String hashText, int beginHash, int endHash){
		this.hashText = hashText;
		this.beginHash = beginHash;
		this.endHash = endHash;
	}
	

	/**
	 * @return the hashText
	 */
	public String getHashText() {
		return hashText;
	}

	/**
	 * @param hashText the hashText to set
	 */
	public void setHashText(String hashText) {
		this.hashText = hashText;
	}

	/**
	 * @return the beginHash
	 */
	public int getBeginHash() {
		return beginHash;
	}

	/**
	 * @param beginHash the beginHash to set
	 */
	public void setBeginHash(int beginHash) {
		this.beginHash = beginHash;
	}

	/**
	 * @return the endHash
	 */
	public int getEndHash() {
		return endHash;
	}

	/**
	 * @param endHash the endHash to set
	 */
	public void setEndHash(int endHash) {
		this.endHash = endHash;
	}

	
	
	
}
