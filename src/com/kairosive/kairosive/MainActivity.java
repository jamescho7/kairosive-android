package com.kairosive.kairosive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.kairosive.database.ActivityPojo;
import com.kairosive.database.DatabaseHandler;

/**
 * The Starting Activity of the Kairosive Android Application.
 * @author James Cho
 */
public class MainActivity extends Activity {
	public DatabaseHandler db;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_main);

		// Sets up a Grid of Icons representing various categories.
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(getApplicationContext()));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this,
						RecordActivity.class);
				intent.putExtra("currentActivity", position);
				startActivity(intent);
			}
		});

		db = DatabaseHandler.getInstance(this);
	}

	/**
	 * Logs all ActivityPojo objects stored currently in the database. Used for
	 * debugging purposes.
	 */
	@SuppressWarnings("unused")
	private void databaseTest() {
		Log.d("Reading: ", "Reading all activities...");
		List<ActivityPojo> activities = db.getAllActivities();

		for (ActivityPojo cn : activities) {
			String log = "Id: " + cn.getId() + " Cat_Id: "
					+ cn.getCategory_id() + " Start Date & Time: "
					+ cn.getStart_date() + "," + cn.getStart_time()
					+ " End Date & Time: " + cn.getEnd_date() + ","
					+ cn.getEnd_time() + " Details: " + cn.getDetails();
			Log.d("Activity: ", log);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_today:
			Intent intent = new Intent(this, LogActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_settings:
			downloadDatabase();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Exports database sqlite3 file to the File System.
	 */
	@SuppressWarnings("resource")
	private void downloadDatabase() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//com.kairosive.kairosive//databases//activityManager";
				String backupDBPath = "KairosiveDatabase.sqlite3";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB)
							.getChannel();
					FileChannel dst = new FileOutputStream(backupDB)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();

					CharSequence text = "Success. Exported file to SD card!";
					Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_SHORT).show();

					Uri u1 = null;
					u1 = Uri.fromFile(backupDB);

					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_SUBJECT,
							"Kairosive Database Backup");
					sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
					sendIntent.setType("text/html");
					startActivity(sendIntent);

				} else {
					CharSequence text = "No database file found!";
					Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_SHORT).show();

				}
			}
		} catch (Exception e) {
			CharSequence text = "Export failed!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(getApplicationContext(), text,
					duration);
			toast.show();

		}

	}

}
