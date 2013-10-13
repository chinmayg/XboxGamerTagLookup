package com.vt.ece4564.xboxlivegamertaglookup;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkTask extends AsyncTask<String, Void, Void>{
	
	String websiteURL_ = "http://www.xboxgamertag.com/search/";
	private static final String TAG = "XBOX";
	String HTMLcode = "";
	String gamerTag_ = "";
	ArrayBlockingQueue<String> q;
	
	public NetworkTask(ArrayBlockingQueue<String> queue){
		q = queue;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		gamerTag_ = params[0];
	     
        try{
        	String newURL = websiteURL_ + gamerTag_.trim();
        	//Log.d(TAG, "char at 48 and 49 "+ newURL.charAt(48) + newURL.charAt(49));
        	//Sets up the HTTP Client
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(newURL);
            
            //Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
	    	HTMLcode = EntityUtils.toString(response.getEntity());
            Log.i(TAG, response.getStatusLine().toString());
	    	//Log.d(TAG, HTMLcode);
	    	
	    	q.add(HTMLcode);
	    	
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	protected void onPostExecute(){

	}
}
