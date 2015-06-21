package nl.saxion.twitter_client.objects;

import java.util.ArrayList;
/**
 * The Url class
 * @author Sharon and Dennis
 *
 */
public class Url {
	
	private String url;
	private int beginUrl;
	private int endUrl;
	
	private ArrayList<Integer> indices = new ArrayList<Integer>();
	
	/**
	 * Constructor url
	 * @param url url string
	 * @param beginUrl beginning of the url
	 * @param endUrl end of the url
	 */
	public Url(String url, int beginUrl, int endUrl){
		this.url = url;
		this.setBeginUrl(beginUrl);
		this.setEndUrl(endUrl);
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the indices
	 */
	public ArrayList<Integer> getIndices() {
		return indices;
	}
	/**
	 * @param indices the indices to set
	 */
	public void setIndices(ArrayList<Integer> indices) {
		this.indices = indices;
	}
	/**
	 * @return the beginUrl
	 */
	public int getBeginUrl() {
		return beginUrl;
	}
	/**
	 * @param beginUrl the beginUrl to set
	 */
	public void setBeginUrl(int beginUrl) {
		this.beginUrl = beginUrl;
	}
	/**
	 * @return the endUrl
	 */
	public int getEndUrl() {
		return endUrl;
	}
	/**
	 * @param endUrl the endUrl to set
	 */
	public void setEndUrl(int endUrl) {
		this.endUrl = endUrl;
	}
	
	

}
