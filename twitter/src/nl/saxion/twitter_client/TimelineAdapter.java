package nl.saxion.twitter_client;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nl.saxion.twitter_client.model.Model;
import nl.saxion.twitter_client.model.TweetApplication;
import nl.saxion.twitter_client.objects.Tweet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TimelineAdapter extends ArrayAdapter<Tweet> implements Observer{
	
	private Model model;
	private LayoutInflater inflater;
	
	public TimelineAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
		
		TweetApplication app = (TweetApplication) getContext().getApplicationContext();
		model = app.getModel();
		inflater = LayoutInflater.from(context);
		model.addObserver(this);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		
		return super.getView(position, convertView, parent);
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

}
