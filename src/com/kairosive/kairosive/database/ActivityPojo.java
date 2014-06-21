package com.kairosive.kairosive.database;

import java.io.Serializable;

public class ActivityPojo implements Serializable, Comparable<ActivityPojo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5362382541309833581L;
	private int mId;
	private int mCategory_id;
	private String mCategory_str;
	private String mStart_date; // "yyyy/mm/dd"
	private String mStart_time; // "hh:mm:ss:am"
	private String mEnd_date;
	private String mEnd_time;
	private String mDetails;

	public ActivityPojo() {

	}

	public ActivityPojo(int id, int categoryId, String start_date,
			String start_time, String end_date, String end_time, String details) {
		this.mId = id;
		this.mCategory_id = categoryId;
		this.mCategory_str = catToString(categoryId);
		this.mStart_date = start_date;
		this.mStart_time = start_time;
		this.mEnd_date = end_date;
		this.mEnd_time = end_time;
		this.mDetails = details;
	}

	private String catToString(int categoryId) {
		return Constants.category[categoryId];
	}

	public boolean equals(ActivityPojo o) {
		return this.mCategory_id == o.getCategory_id()
				&& this.mStart_date.equals(o.getStart_date())
				&& this.getStart_time().equals(o.getStart_time());
	};

	public ActivityPojo(int categoryId, String start_date, String start_time,
			String end_date, String end_time, String details) {
		this.mCategory_id = categoryId;
		this.mCategory_str = catToString(categoryId);
		this.mStart_date = start_date;
		this.mStart_time = start_time;
		this.mEnd_date = end_date;
		this.mEnd_time = end_time;
		this.mDetails = details;
	}

	public int getId() {
		return mId;
	}

	public void setId(int mId) {
		this.mId = mId;
	}

	public int getCategory_id() {
		return mCategory_id;
	}

	public void setCategory_id(int mCategory_id) {
		this.mCategory_id = mCategory_id;
	}

	public String getStart_date() {
		return mStart_date;
	}

	public void setStart_date(String mStart_date) {
		this.mStart_date = mStart_date;
	}

	public String getStart_time() {
		return mStart_time;
	}

	public void setStart_time(String mStart_time) {
		this.mStart_time = mStart_time;
	}

	public String getEnd_date() {
		return mEnd_date;
	}

	public void setEnd_date(String mEnd_date) {
		this.mEnd_date = mEnd_date;
	}

	public String getEnd_time() {
		return mEnd_time;
	}

	public void setEnd_time(String mEnd_time) {
		this.mEnd_time = mEnd_time;
	}

	public String getDetails() {
		return mDetails;
	}

	public void setDetails(String mDetails) {
		this.mDetails = mDetails;
	}

	@Override
	public String toString() {
		String data = Constants.category[getCategory_id()];
		if (getDetails().length() > 0) {
			data += " - \'" + getDetails() + "\'";
		}
		data += "\n";
		data += getStart_time() + " - " + getEnd_time();

		return data;
	}

	public String getCategory() {

		if (getCategory_id() >= 0 && getCategory_id() < 8) {
			return "personal";
		} else if (getCategory_id() >= 8 && getCategory_id() < 12) {
			return "family";
		} else if (getCategory_id() >= 12 && getCategory_id() < 15) {
			return "work";
		} else if (getCategory_id() >= 15 && getCategory_id() < 18) {
			return "social";
		} else if (getCategory_id() >= 18 && getCategory_id() < 20) {
			return "faith";
		} else {
			return "misc";
		}
	}

	public String getCategory_str() {
		return mCategory_str;
	}

	public void setCategory_str(String mCategory_str) {
		this.mCategory_str = mCategory_str;
	}

	@Override
	public int compareTo(ActivityPojo another) {

		if (this.equals(another)) {
			return 0;
		}

		int mHour = Integer.parseInt(getStart_time().substring(0, 2));
		int mMinutes = Integer.parseInt(getStart_time().substring(3, 5));
		int mSeconds = Integer.parseInt(getStart_time().substring(6, 8));
		boolean mIsAM = getStart_time().substring(9, 11).equals("am") ? true
				: false;

		int anotherHour = Integer.parseInt(another.getStart_time().substring(0,
				2));
		int anotherMinutes = Integer.parseInt(another.getStart_time()
				.substring(3, 5));
		int anotherSeconds = Integer.parseInt(another.getStart_time()
				.substring(6, 8));
		boolean anotherIsAM = another.getStart_time().substring(9, 11)
				.equals("am") ? true : false;

		if (mIsAM != anotherIsAM) {
			return mIsAM ? -1 : 1;
		}

		if (mHour != anotherHour) {
			return mHour - anotherHour;
		}

		if (mMinutes != anotherMinutes) {
			return mMinutes - anotherMinutes;
		}

		if (mSeconds != anotherSeconds) {
			return mSeconds - anotherSeconds;
		}

		return 0;

	}

	public ActivityPojo clone() {
		ActivityPojo activity = new ActivityPojo(this.mCategory_id,
				this.mStart_date, this.mStart_time, this.mEnd_date,
				this.mEnd_time, this.mDetails);
		activity.setId(getId());
		return activity;
	}
}
