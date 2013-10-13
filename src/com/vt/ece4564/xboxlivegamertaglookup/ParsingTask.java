package com.vt.ece4564.xboxlivegamertaglookup;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ParsingTask extends AsyncTask<Void, Void, Void> {

	ArrayBlockingQueue<String> q_;
	Activity activity_;
	private static final String TAG = "XBOX";
	String HTMLcode_ = "";
	String profileString = "";
	ArrayList<Game> recentTable = new ArrayList<Game>();
	boolean doesExist = true;

	public ParsingTask(ArrayBlockingQueue<String> queue, Activity act) {
		q_ = queue;
		activity_ = act;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			HTMLcode_ = q_.take();
			Document doc = Jsoup.parse(HTMLcode_);

			Element title = doc.select("title").first();
			Log.d(TAG, title.text());
			if (title.text().contains("hasn't")) {
				doesExist = false;
			} else {
				// Parse the user Profile
				Element profileDiv = doc.select("div.lowerPart").first();
				Log.d(TAG, profileDiv.text());
				profileString = profileDiv.text();

				// Parse the Recent Games Table
				Element tableDiv = doc.select("div[id=recentgames").first();
				Element table = tableDiv.select("table").first();
				Element tableBody = table.select("tbody").first();
				Elements tableRows = tableBody.select("tr");

				for (Element element : tableRows) { // Used to iterate through
													// the
													// table rows
					Game tempGame = new Game();
					Element internalTD = element.select("td").get(1);

					// Gets the Game Title
					Element pTag = internalTD.select("p").get(0);
					Element aTag = pTag.select("a").first();
					// Log.d(TAG, "aTag Text " + aTag.text()); // Debug code for
					// getting the Game
					// Title
					// Log.d(TAG, internalTD.select("p").get(1).text()); //
					// Debug
					// code
					// for
					// getting
					// the last
					// played
					// date
					tempGame.setNameOfGame(aTag.text());
					tempGame.setDatePlayed(internalTD.select("p").get(1).text());
					recentTable.add(tempGame);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Log.d(TAG, "In Post Code");
		Log.d(TAG, "ArrayList length: " + recentTable.size());
		if (!doesExist) {
			Context context = activity_.getApplicationContext();
			CharSequence text = "The Gamertag that you entered doesn't exist!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {
			// Sets the profile in a Table Layout
			TableLayout table = (TableLayout) activity_
					.findViewById(R.id.myTable);
			TextView tempView;
			String[] need = profileString.split("\\|\\|");
			for (int i = 0; i < table.getChildCount(); i++) {
				tempView = (TextView) table.getChildAt(i);
				tempView.setText(need[i]);
			}

			TableLayout table2 = (TableLayout) activity_
					.findViewById(R.id.RecentGamesTable);

			// Creates a view for the recent games
			for (int i = 0; i < recentTable.size(); i++) {
				// try {
				Game temp = recentTable.get(i);
				TableRow row = new TableRow(activity_);
				TextView GameName = new TextView(activity_);
				TextView DatePlayed = new TextView(activity_);
				String date = temp.getDatePlayed();
				date = date.substring(14, date.length());
				// Date newDate = new
				// SimpleDateFormat(" EEE, FF MMM yyyy kk:mm:ss zzz",
				// Locale.US).parse(date);
				// SimpleDateFormat desDf = new
				// SimpleDateFormat("MMM FF yy hh:mm a", Locale.US);
				// date = desDf.format(newDate);
				GameName.setText(temp.getNameOfGame());
				DatePlayed.setText(date);
				DatePlayed.setGravity(Gravity.RIGHT);
				GameName.setGravity(Gravity.LEFT);
				row.addView(GameName);
				row.addView(DatePlayed);
				row.setPadding(20, 0, 0, 0);
				table2.addView(row);
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}
	}

}
