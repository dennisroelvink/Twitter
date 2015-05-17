package nl.saxion.twitter_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import nl.saxion.twitter_client.objects.Hashtag;
import nl.saxion.twitter_client.objects.Tweet;
import nl.saxion.twitter_client.objects.Url;
import nl.saxion.twitter_client.objects.UserMention;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * The TweetAdapter class
 * @author Sharon and Dennis
 *
 */
public class TweetAdapter extends ArrayAdapter<Tweet> implements Observer{

	private LayoutInflater inflater;
	private Model model;
	private Bitmap bitmap;
	
	public TweetAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
		
		TweetApplication app = (TweetApplication) getContext().getApplicationContext();
		model = app.getModel();
		inflater = LayoutInflater.from(context);
		
	}
	
	/**
	 * Updates the listview for every position
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.tweet, parent,false);
		Tweet tweet  = getItem(position);

		TextView tweetmsg = (TextView)convertView.findViewById(R.id.textViewTweetMessage);
		TextView userName = (TextView)convertView.findViewById(R.id.textViewUserName);
		TextView name = (TextView) convertView.findViewById(R.id.textViewName);
		ImageView profilePhoto = (ImageView) convertView.findViewById(R.id.imageViewProfilePhoto);
		//ImageView photo = (ImageView) convertView.findViewById(R.id.imageViewPhoto);
		
		// makes the hashtag text blue
		SpannableString spannableTweet = new SpannableString(tweet.getText());
		for(int i = 0; i < tweet.getHashtagListSize(); i++){
			Hashtag tag = tweet.getHashtagAtPosition(i);
			spannableTweet.setSpan(new ForegroundColorSpan(Color.rgb(87, 186, 48)), tag.getBeginHash(), tag.getEndHash(), 0);
		}
		// makes url clickable
		for(int i = 0; i < tweet.getUrlListSize(); i++){
			Url u = tweet.getUrlAtPosition(i);
			spannableTweet.setSpan(new URLSpan(u.getUrl()), u.getBeginUrl(), u.getEndUrl(), 0);
		}
		// makes the user mention text orange
		for(int i = 0; i < tweet.getMentionListSize(); i++){
			UserMention UM = tweet.getMentionAtPosition(i);
			spannableTweet.setSpan(new URLSpan("http://www.twitter.com/"+UM.getScreenName()), UM.getBeginMention(), UM.getEndMention(), 0);
			spannableTweet.setSpan(new ForegroundColorSpan(Color.rgb(247, 149, 49)), UM.getBeginMention(),UM.getEndMention() , 0);
		}
		
		tweetmsg.setLinksClickable(true);
		tweetmsg.setMovementMethod(LinkMovementMethod.getInstance());
		
		
		tweetmsg.setText(spannableTweet, BufferType.SPANNABLE);
		userName.setText(tweet.getUser().getUserName());
		name.setText(" (" +tweet.getUser().getName() + ")");
		profilePhoto.setImageBitmap(tweet.getUser().getBitmap());
		//photo.setImageBitmap(tweet.getPhoto().getBitmap());
		return convertView;
		
	}
	

	/**
	 * Updates the class as soon as the observable is changed
	 */
	@Override
	public void update(Observable observable, Object data) {
		notifyDataSetChanged();
		
	}


}
