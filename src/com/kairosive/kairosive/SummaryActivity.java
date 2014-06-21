package com.kairosive.kairosive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kairosive.kairosive.database.ActivityPojo;
import com.kairosive.kairosive.database.DatabaseHandler;

public class SummaryActivity extends Activity {
	private DatabaseHandler db;
	private Category personal;
	private Category family;
	private Category faith;
	private Category work;
	private Category social;
	private Category misc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		db = DatabaseHandler.getInstance(this);
		List<ActivityPojo> activities = db.getAllActivities();
		Intent intent = getIntent();
		int year = intent.getIntExtra("com.jamescho.kairosive.year", 2011);
		int month = intent.getIntExtra("com.jamescho.kairosive.month", 10);
		int date = intent.getIntExtra("com.jamescho.kairosive.date", 11);
		summarize(activities, year, month, date);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void showGraph(View v) {
		Intent intent = new Intent(SummaryActivity.this, PieActivity.class);
		intent.putExtra("com.jamescho.kairosive.personalSeconds",
				personal.totalSeconds);
		intent.putExtra("com.jamescho.kairosive.familySeconds",
				family.totalSeconds);
		intent.putExtra("com.jamescho.kairosive.workSeconds", work.totalSeconds);
		intent.putExtra("com.jamescho.kairosive.socialSeconds",
				social.totalSeconds);
		intent.putExtra("com.jamescho.kairosive.faithSeconds",
				faith.totalSeconds);

		misc.totalSeconds = 86400 - personal.totalSeconds - family.totalSeconds
				- work.totalSeconds - social.totalSeconds - faith.totalSeconds;
		intent.putExtra("com.jamescho.kairosive.miscSeconds", misc.totalSeconds);
		startActivity(intent);

	}

	private void summarize(List<ActivityPojo> activities, int year, int month,
			int date) {

		List<ActivityPojo> personalList = new ArrayList<ActivityPojo>();
		List<ActivityPojo> familyList = new ArrayList<ActivityPojo>();
		List<ActivityPojo> faithList = new ArrayList<ActivityPojo>();
		List<ActivityPojo> workList = new ArrayList<ActivityPojo>();
		List<ActivityPojo> socialList = new ArrayList<ActivityPojo>();
		List<ActivityPojo> miscList = new ArrayList<ActivityPojo>();

		// 0-7: Personal
		// 8-11: Family
		// 12-13: Faith
		// 14-16: Work
		// 17 - 19: Social
		// 21: Misc

		for (ActivityPojo act : activities) {
			if (act.getStart_date().equals(getDate(year, month, date))) {

				String category = act.getCategory();
				if (category.equals("personal")) {
					personalList.add(act);
				} else if (category.equals("family")) {
					familyList.add(act);
				} else if (category.equals("faith")) {
					faithList.add(act);
				} else if (category.equals("work")) {
					workList.add(act);
				} else if (category.equals("social")) {
					socialList.add(act);
				} else if (category.equals("misc")) {
					miscList.add(act);
				}
			}
		}

		personal = new Category(personalList);
		family = new Category(familyList);
		faith = new Category(faithList);
		work = new Category(workList);
		social = new Category(socialList);
		misc = new Category(miscList);

		TextView tv = (TextView) findViewById(R.id.personalCount);
		TextView tv2 = (TextView) findViewById(R.id.personalTotal);
		changeText(tv, tv2, personal);

		tv = (TextView) findViewById(R.id.familyCount);
		tv2 = (TextView) findViewById(R.id.familyTotal);
		changeText(tv, tv2, family);

		tv = (TextView) findViewById(R.id.workCount);
		tv2 = (TextView) findViewById(R.id.workTotal);
		changeText(tv, tv2, work);

		tv = (TextView) findViewById(R.id.socialCount);
		tv2 = (TextView) findViewById(R.id.socialTotal);
		changeText(tv, tv2, social);

		tv = (TextView) findViewById(R.id.faithCount);
		tv2 = (TextView) findViewById(R.id.faithTotal);
		changeText(tv, tv2, faith);

		tv = (TextView) findViewById(R.id.miscCount);
		tv2 = (TextView) findViewById(R.id.miscTotal);
		misc.totalSeconds = 86400 - personal.totalSeconds - family.totalSeconds
				- work.totalSeconds - social.totalSeconds - faith.totalSeconds;
		changeText(tv, tv2, misc);

	}

	private void changeText(TextView count, TextView duration, Category category) {
		count.setText(category.getCount() + getActivities(category.getCount())
				+ " completed");
		duration.setText("Total: " + formatSeconds(category.getTotalSeconds()));

	}

	private String getActivities(int count) {
		if (count == 1) {
			return " activity";
		}
		return " activities";
	}

	private String formatSeconds(int totalSeconds) {
		int hour = 0;
		int minute = 0;

		while (totalSeconds >= 3600) {
			totalSeconds -= 3600;
			hour += 1;
		}

		while (totalSeconds >= 60) {
			totalSeconds -= 60;
			minute += 1;
		}

		return formatted(hour) + ":" + formatted(minute) + ":"
				+ formatted(totalSeconds);

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

	public class Category {

		public int count;
		public int totalSeconds;

		public Category(List<ActivityPojo> activities) {
			this.count = activities.size();

			for (ActivityPojo act : activities) {
				this.totalSeconds += calculateSeconds(act.getStart_date(),
						act.getStart_time(), act.getEnd_date(),
						act.getEnd_time());

			}

		}

		private int calculateSeconds(String startDate, String startTime,
				String endDate, String endTime) {

			startTime = formatTime(startTime);
			endTime = formatTime(endTime);

			// Custom date format
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss");

			String dateStart = startDate + " " + startTime;
			String dateStop = endDate + " " + endTime;

			Date d1 = null;
			Date d2 = null;
			try {
				d1 = format.parse(dateStart);
				d2 = format.parse(dateStop);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			long diff = (d2.getTime() - d1.getTime()) / 1000;

			return (int) diff;
		}

		private String formatTime(String startTime) {
			// startTime looks like: 10:32:20:am
			String[] parts = startTime.split(":");

			String hour = parts[0];
			String minute = parts[1];
			String seconds = parts[2];
			String meridiem = parts[3];

			if (hour.equals("12")) {
				if (meridiem.equals("am")) {
					hour = "00";
				}
			} else {
				if (meridiem.equals("pm")) {
					hour = "" + (Integer.parseInt(hour) + 12);
				}
			}

			return hour + ":" + minute + ":" + seconds;

		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getTotalSeconds() {
			return totalSeconds;
		}

		public void setTotalSeconds(int totalSeconds) {
			this.totalSeconds = totalSeconds;
		}
	}

}
