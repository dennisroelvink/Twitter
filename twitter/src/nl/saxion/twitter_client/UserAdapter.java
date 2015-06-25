package nl.saxion.twitter_client;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import nl.saxion.twitter_client.objects.Tweet;
import nl.saxion.twitter_client.objects.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The Useradapter class
 * @author Sharon and Dennis
 *
 */
public class UserAdapter extends ArrayAdapter<User> implements Observer {

	private LayoutInflater inflater;
	private Model model;
	
	public UserAdapter(Context context, int resource, List<User> objects) {
		super(context, resource, objects);

		TweetApplication app = (TweetApplication) getContext().getApplicationContext();
		model = app.getModel();
		inflater = LayoutInflater.from(context);
		model.addObserver(this);
	}
	
	/**
	 * Updates the listview for every position
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.user, parent,false);
		
		User user = getItem(position);
		
		TextView screenName = (TextView) convertView.findViewById(R.id.textViewScreennameUser);
		TextView realName = (TextView) convertView.findViewById(R.id.textViewRealNameUser);
		ImageView profilePic = (ImageView) convertView.findViewById(R.id.imageViewPicUser);
		if(user.getBitmap() != null) {
			profilePic.setImageBitmap(user.getBitmap());
		}

		screenName.setText(user.getUserName());
		realName.setText(user.getName());
		
		return convertView;
	}

	
	@Override
	public void update(Observable observable, Object data) {
		notifyDataSetChanged();
		
	}

}
