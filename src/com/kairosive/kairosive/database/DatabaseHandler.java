package com.kairosive.kairosive.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static DatabaseHandler db;
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "activityManager";

	// Contacts table name
	private static final String TABLE_ACTIVITIES = "activities";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_CATEGORY_ID = "category_id";
	private static final String KEY_CATEGORY_STRING = "category_str";
	private static final String KEY_START_DATE = "start_date";
	private static final String KEY_START_TIME = "start_time";
	private static final String KEY_END_DATE = "end_date";
	private static final String KEY_END_TIME = "end_time";
	private static final String KEY_DETAILS = "details";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DatabaseHandler getInstance(Context context) {
		if (db == null) {
			db = new DatabaseHandler(context);
		}
		return db;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ACTIVITIES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY_ID
				+ " INTEGER," + KEY_CATEGORY_STRING + " TEXT," + KEY_START_DATE
				+ " TEXT," + KEY_START_TIME + " TEXT," + KEY_END_DATE
				+ " TEXT," + KEY_END_TIME + " TEXT," + KEY_DETAILS + " TEXT"
				+ ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
		// Create tables again
		onCreate(db);
	}

	public void addActivity(ActivityPojo activity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CATEGORY_ID, activity.getCategory_id());
		values.put(KEY_CATEGORY_STRING, getString(activity.getCategory_id()));
		values.put(KEY_START_DATE, activity.getStart_date());
		values.put(KEY_START_TIME, activity.getStart_time());
		values.put(KEY_END_DATE, activity.getEnd_date());
		values.put(KEY_END_TIME, activity.getEnd_time());
		values.put(KEY_DETAILS, activity.getDetails());

		// Inserting Row
		db.insert(TABLE_ACTIVITIES, null, values);
		db.close(); // Closing database connection
	}

	private String getString(int category_id) {
		return Constants.category[category_id];
	}

	public ActivityPojo getActivity(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ACTIVITIES, new String[] { KEY_ID,
				KEY_CATEGORY_ID, KEY_START_DATE, KEY_START_TIME, KEY_END_DATE,
				KEY_END_TIME, KEY_DETAILS }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		ActivityPojo activity = new ActivityPojo(Integer.parseInt(cursor
				.getString(0)), Integer.parseInt(cursor.getString(1)),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6));

		return activity;
	}

	public List<ActivityPojo> getAllActivities() {
		List<ActivityPojo> activityList = new ArrayList<ActivityPojo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITIES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ActivityPojo activity = new ActivityPojo();
				activity.setId(Integer.parseInt(cursor.getString(0)));
				activity.setCategory_id(Integer.parseInt(cursor.getString(1)));
				activity.setCategory_str(cursor.getString(2));
				activity.setStart_date(cursor.getString(3));
				activity.setStart_time(cursor.getString(4));
				activity.setEnd_date(cursor.getString(5));
				activity.setEnd_time(cursor.getString(6));
				activity.setDetails(cursor.getString(7));
				activityList.add(activity);
			} while (cursor.moveToNext());
		}

		// return contact list
		return activityList;
	}

	public int getActivitiesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ACTIVITIES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public void updateActivity(ActivityPojo activity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CATEGORY_ID, activity.getCategory_id());
		values.put(KEY_CATEGORY_STRING, activity.getCategory_str());
		values.put(KEY_START_DATE, activity.getStart_date());
		values.put(KEY_START_TIME, activity.getStart_time());
		values.put(KEY_END_DATE, activity.getEnd_date());
		values.put(KEY_END_TIME, activity.getEnd_time());
		values.put(KEY_DETAILS, activity.getDetails());

		// Inserting Row
		db.update(TABLE_ACTIVITIES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(activity.getId()) });
		db.close(); // Closing database connection
	}

	public void deleteActivity(ActivityPojo activity) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ACTIVITIES, KEY_ID + " = ?",
				new String[] { String.valueOf(activity.getId()) });
		db.close();
	}

}
