package com.kairosive.kairosive;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kairosive.kairosive.database.ActivityPojo;
import com.kairosive.kairosive.database.Constants;
import com.kairosive.kairosive.database.DatabaseHandler;

public class RecordActivity extends Activity {
	private boolean alreadyRecorded;
	private boolean canceled;
	private boolean finished;

	private Chronometer mChronometer;
	private String startDate;
	private String formattedStartDate;

	private String startTime;
	private String formattedStartTime;
	private DatabaseHandler db;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		alreadyRecorded = false;
		canceled = false;
		finished = false;

		// Show the Up button in the action bar.
		setupUserForm();

		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		startTime = getFormattedTime(1);
		startDate = getDate(1);

		formattedStartDate = getDate(0);
		formattedStartTime = getFormattedTime(0);

		Intent intent = getIntent();
		position = intent.getIntExtra("currentActivity", 0);

		TextView tv = (TextView) findViewById(R.id.currentActivity);
		tv.setText(Constants.category[position]);

		TextView tv2 = (TextView) findViewById(R.id.startTime);
		tv2.setText(startTime);

		mChronometer.start();

		setupButtons();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// If App is minimized or terminated
		if (!canceled && !finished) {
			insertActivity();
		}
	}

	public void insertActivity() {
		if (db == null) {
			db = MainActivity.db;
		}

		if (alreadyRecorded) {
			deleteActivity();
		}

		ActivityPojo currentAct = new ActivityPojo(position,
				formattedStartDate, formattedStartTime, getDate(0),
				getFormattedTime(0), getDetails());

		db.addActivity(currentAct);
		alreadyRecorded = true;
	}

	public void deleteActivity() {
		if (db == null) {
			db = new DatabaseHandler(getApplicationContext());
		}
		ActivityPojo currentAct = new ActivityPojo(position,
				formattedStartDate, formattedStartTime, getDate(0),
				getFormattedTime(0), getDetails());
		List<ActivityPojo> activities = db.getAllActivities();

		if (activities.size() > 0
				&& activities.get(activities.size() - 1).equals(currentAct)) {
			db.deleteActivity(activities.get(activities.size() - 1));
		} else {
			for (ActivityPojo activityPojo : activities) {
				if (activityPojo.equals(currentAct)) {
					db.deleteActivity(activityPojo);
					break;
				}
			}
		}
	}

	private String getDate(int selector) {
		Calendar calendar = Calendar.getInstance();
		int month;
		int date;
		int year;
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		date = calendar.get(Calendar.DATE);

		if (selector == 0) {
			return year + "/" + formatted(month) + "/" + formatted(date);
		} else {
			return formatted(month) + "/" + formatted(date) + "/" + year;
		}

	}

	private void setupButtons() {
		final Button button = (Button) findViewById(R.id.button_finish);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!finished) {
					insertActivity();
				}
				finished = true;

				Intent intent = new Intent(RecordActivity.this,
						MainActivity.class);
				startActivity(intent);

				CharSequence text = "Successfully added!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(getApplicationContext(), text,
						duration);
				toast.show();
				finish();
			}

		});

		final Button button2 = (Button) findViewById(R.id.button_share);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						generateMessage(getDetails()));
				sendIntent.setType("text/plain");
				startActivity(sendIntent);

			}
		});

		final Button button3 = (Button) findViewById(R.id.button_cancel);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(RecordActivity.this,
						MainActivity.class);
				startActivity(intent);
				canceled = true;

				if (alreadyRecorded) {
					deleteActivity();
				}

				CharSequence text = "Recording Canceled.\n No changes made.";
				int duration = Toast.LENGTH_SHORT;
				Context context = getApplicationContext();

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				finish();

			}
		});
	}

	// Debug Only
	private void logActivity(ActivityPojo cn) {
		String log = "Id: " + cn.getId() + " Cat_Id: " + cn.getCategory_id()
				+ " Start Date & Time: " + cn.getStart_date() + ","
				+ cn.getStart_time() + " End Date & Time: " + cn.getEnd_date()
				+ "," + cn.getEnd_time() + " Details: " + cn.getDetails();
		Log.d("Activity: ", log);
	}

	private String getDetails() {
		EditText detailsBox = (EditText) findViewById(R.id.detailsBox);
		return detailsBox.getText().toString();
	}

	private String generateMessage(String detail) {
		return startDate + ": " + startTime + ": " + "["
				+ Constants.category[position] + "] " + detail;
	}

	View.OnClickListener mStartListener = new OnClickListener() {
		public void onClick(View v) {
			mChronometer.start();
		}
	};

	View.OnClickListener mStopListener = new OnClickListener() {
		public void onClick(View v) {
			mChronometer.stop();
		}
	};

	View.OnClickListener mResetListener = new OnClickListener() {
		public void onClick(View v) {
			mChronometer.setBase(SystemClock.elapsedRealtime());
		}
	};

	private String getFormattedTime(int selector) {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		int ampm = c.get(Calendar.AM_PM);

		String meridiem;

		if (ampm == 0) {
			meridiem = "am";
		} else {
			meridiem = "pm";
		}

		if (hour == 0) {
			hour = 12;
		}

		if (selector == 0) {
			return formatted(hour) + ":" + formatted(min) + ":"
					+ formatted(sec) + ":" + meridiem;
		}
		return formatted(hour) + ":" + formatted(min) + " " + meridiem;

	}

	private String formatted(int min) {
		if (min < 10) {
			return "0" + min;
		} else {
			return "" + min;
		}

	}

	private void setupUserForm() {

	}

}
