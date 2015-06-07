package nl.saxion.twitter_client;

import android.os.AsyncTask;

public class UserRequestTool {
	
	public void initUserRequests() {
		new UserRequestHandler().execute();
	}
	
	
	private class UserRequestHandler extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
