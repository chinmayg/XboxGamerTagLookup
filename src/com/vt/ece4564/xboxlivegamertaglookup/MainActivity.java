package com.vt.ece4564.xboxlivegamertaglookup;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private static final String TAG = "XBOX";
	static final int MAX_QUEUE_SIZE = 4;
	private EditText gamerTagText_;
	public Button entryButton_;
	public String gamerTag_;
	Timer t;

	ArrayBlockingQueue<String> q;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		//Creates the objects for the entry Button and Text Field
		entryButton_ = (Button) findViewById(R.id.SendGamerTag);
		gamerTagText_ = (EditText) findViewById(R.id.enterGamerTag);

		q = new ArrayBlockingQueue<String>(MAX_QUEUE_SIZE);
		
		//Click Listener for the entry button to start the network operation
		entryButton_.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
			    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			    mgr.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
				t = new Timer();
			    t.scheduleAtFixedRate(new TimerTask() {

			        public void run() {
						// Log to make sure I am getting the right string
						Log.d(TAG, gamerTagText_.getText().toString());
						gamerTag_ = gamerTagText_.getText().toString();
						final NetworkTask netTask_ = new NetworkTask(q);
						final ParsingTask parTask = new ParsingTask(q, MainActivity.this);
						
						netTask_.execute(gamerTag_);
						parTask.execute(); 
			        }
			      }, 0,60*1000); //Checks every minute

			}
		});
	}
}
