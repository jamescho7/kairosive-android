package com.kairosive.kairosive;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kairosive.kairosive.database.ActivityPojo;
import com.kairosive.kairosive.database.DatabaseHandler;

public class LogActivity extends FragmentActivity {

	private String[] actsList;
	private ListView actsListView;
	private ArrayAdapter<String> arrayAdapter;
	private DatabaseHandler db;
	private TextView tv;
	private Calendar c;

	private int currentYear;
	private int currentMonth;
	private int currentDate;

	private List<ActivityPojo> allActivities = new ArrayList<ActivityPojo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_log);

		db = DatabaseHandler.getInstance(this);
		c = Calendar.getInstance();
		onUpdate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DATE));
		DatePickerFragment.firstTime = true;
	}

	private void onUpdate(final int year, final int month, final int date) {
		currentYear = year;
		currentMonth = month;
		currentDate = date;

		// 1. Retrieve all activities from date.
		actsListView = (ListView) findViewById(R.id.activities_list);
		actsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LogActivity.this,
						DetailsActivity.class);
				intent.putExtra("SELECTED_POSITION", position);
				intent.putExtra("SELECTED_ACTIVITY",
						allActivities.get(position));
				startActivityForResult(intent, 0);
			}

		});

		actsList = getActivities(getDate(year, month, date));
		arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item, actsList);
		actsListView.setAdapter(arrayAdapter);

		// 2. Set TextView to display correct date.
		c.set(year, month, date);
		tv = (TextView) findViewById(R.id.date);
		tv.setText(getDayAsString(month, date, c.get(Calendar.DAY_OF_WEEK)));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		switch (resultCode) {
		case DetailsActivity.RESULT_UPDATE:
			ActivityPojo updatedActivity = (ActivityPojo) intent
					.getSerializableExtra("UPDATED_ACTIVITY");
			db.updateActivity(updatedActivity);
			onUpdate(currentYear, currentMonth, currentDate);

			Toast.makeText(getApplicationContext(), "Updated 1 Activity",
					Toast.LENGTH_SHORT).show();
			break;
		case DetailsActivity.RESULT_DELETE:
			int position = intent.getIntExtra("SELECTED_POSITION", -1);
			ActivityPojo act = allActivities.remove(position);
			db.deleteActivity(act);
			onUpdate(currentYear, currentMonth, currentDate);

			Toast.makeText(getApplicationContext(), "Deleted 1 Activity",
					Toast.LENGTH_SHORT).show();
			break;
		}

	}

	public void summary(View v) {
		Intent intent = new Intent(LogActivity.this, SummaryActivity.class);
		intent.putExtra("com.jamescho.kairosive.year", currentYear);
		intent.putExtra("com.jamescho.kairosive.month", currentMonth);
		intent.putExtra("com.jamescho.kairosive.date", currentDate);
		startActivity(intent);
	}

	private String[] getActivities(String desiredDate) {
		List<ActivityPojo> activities = db.getAllActivities();
		List<String> acts = new ArrayList<String>();
		for (ActivityPojo act : activities) {
			if (desiredDate.equals(act.getStart_date())) {
				acts.add(act.toString());
				allActivities.add(act);
			}
		}
		String[] result = new String[acts.size()];
		result = acts.toArray(result);
		return result;
	}

	private String getDate(int year, int month, int date) {
		return year + "/" + formatted(month + 1) + "/" + formatted(date);
	}

	private String formatted(int min) {
		if (min < 10) {
			return "0" + min;
		} else {
			return "" + min;
		}

	}

	private String getDayAsString(int month, int date, int day) {

		String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday" };

		String[] months = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };

		return days[day - 1] + ", " + months[month] + " " + date;
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
		((DatePickerFragment) newFragment).setLogActivity(this);

	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		public static int getYear() {
			return mYear;
		}

		public static int mYear;
		public static int mMonth;
		public static int mDay;
		public static boolean firstTime = true;
		private LogActivity logActivity;

		public void setLogActivity(LogActivity logAct) {
			logActivity = logAct;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			if (firstTime) {
				final Calendar c = Calendar.getInstance();
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);
				firstTime = false;
			}

			return new DatePickerDialog(getActivity(), this, mYear, mMonth,
					mDay);
		}

		public void onDateSet(DatePicker view, int year, int month, int date) {
			mYear = year;
			mMonth = month;
			mDay = date;
			logActivity.onUpdate(year, month, date);
		}
	}

}
