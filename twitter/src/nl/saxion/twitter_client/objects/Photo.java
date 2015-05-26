package nl.saxion.twitter_client.objects;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * The photo class
 * @author Sharon en Dennis
 *
 */
public class Photo {

	private String photoURL;
	private int beginPhotoURL;
	private int endPhotoURL;
	private Bitmap bitmap;
	
	public Photo(String photoURL, int begin, int end){
		this.setPhotoURL(photoURL);
		this.setBeginPhotoURL(begin);
		this.setEndPhotoURL(end);
		new URLHandler().execute();
	}

	/**
	 * @return the photoURL
	 */
	public String getPhotoURL() {
		return photoURL;
	}

	/**
	 * @param photoURL the photoURL to set
	 */
	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	/**
	 * @return the beginPhotoURL
	 */
	public int getBeginPhotoURL() {
		return beginPhotoURL;
	}

	/**
	 * @param beginPhotoURL the beginPhotoURL to set
	 */
	public void setBeginPhotoURL(int beginPhotoURL) {
		this.beginPhotoURL = beginPhotoURL;
	}

	/**
	 * @return the endPhotoURL
	 */
	public int getEndPhotoURL() {
		return endPhotoURL;
	}

	/**
	 * @param endPhotoURL the endPhotoURL to set
	 */
	public void setEndPhotoURL(int endPhotoURL) {
		this.endPhotoURL = endPhotoURL;
	}

	/**
	 * @return the photo
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	/**
	 * The URLHandler class
	 * @author Sharon and Dennis
	 * 
	 * Handles the exception which occurred when converting the url into an imageview
	 */
	private final class URLHandler extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
			
				URL Imageurl = new URL(getPhotoURL());
				setBitmap(BitmapFactory.decodeStream(Imageurl.openConnection().getInputStream()));

				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException r) {
				r.printStackTrace();
			}
			
			return null;
		}
		
	}
	
}
