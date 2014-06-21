package com.kairosive.utils;

import com.kairosive.database.ActivityPojo;


 /**
 * @author James Cho
 * A convenience utility class whose static methods provide a way to manipulate time information. Relies heavily on the format "hh:mm:ss:am".
 */
public class KSTimeUtils {
	 
	/**
	 * An enum used to specify how a String should be formatted. Should be used
	 * in conjunction with {@link KSTimeUtils#formatTime(Format, String) }
	 */
	public enum Format {
		REMOVE_SECONDS;
	}

	/**
	 * 
	 */
	private KSTimeUtils() {
	}

	/**
	 * @param hoursIn24
	 *            An integer representing an hour in a 24-hour clock
	 * @return Integer The value of the hour in a 12-hour clock. Should be used
	 *         in conjunction with {@link #isAM(int)}.
	 * @see #isAM(int)
	 */
	public static int to12HourClock(int hoursIn24) {

		if (hoursIn24 == 0) {
			return 12;
		} else if (hoursIn24 == 12) {
			return 12;
		}

		return isAM(hoursIn24) ? hoursIn24 : hoursIn24 - 12;
	}

	/**
	 * @param hoursIn12
	 *            An integer representing an hour in a 12-hour clock
	 * @param isAM
	 *            Whether the hour is an AM or PM time
	 * @return Intent The value of the hour in a 24-hour clock.
	 */
	public static int to24HourClock(int hoursIn12, boolean isAM) {

		if (!isAM) {
			if (hoursIn12 != 12) {
				hoursIn12 += 12;
			}
		} else {
			if (hoursIn12 == 12) {
				hoursIn12 -= 12;
			}
		}

		return hoursIn12;
	}

	/**
	 * Converts an integer to a String of length 2. Used for formatting time.
	 * 
	 * @param num
	 *            An integer.
	 * @return The same integer formatted into a String of length 2.
	 */
	public static String numToTwoCharString(int num) {
		if (num < 10) {
			return "0" + num;
		} else {
			return "" + num;
		}
	}

	// Formats a String in "hh:mm:ss:am" form.
	/**
	 * Formats a String in "hh:mm:ss:am" form into a desired format. Should be
	 * used with {@link Format} enum.
	 * 
	 * @param type
	 *            A Format enum value specifying how to format the time.
	 * @param time
	 *            A time in "hh:mm:ss:am" format to be reformatted.
	 * @return String The reformatted String.
	 */
	public static String formatTime(Format type, String time) {
		if (type == Format.REMOVE_SECONDS) {
			// Returns in "hh:mm am" form
			return String.format("%s:%s %s", time.substring(0, 2),
					time.substring(3, 5), time.substring(9, 11));
		}
		return time;
	}

	/**
	 * Determines whether a String in "hh:mm:ss:am" form is AM or PM.
	 * 
	 * @param time
	 *            A time in "hh:mm:ss:am" format.
	 * @return boolean Whether the time is AM.
	 */
	public static boolean isAM(String time) {
		return parseAMPM(time).equals("am");
	}

	/**
	 * Determines whether an hour in a 24-hour clock is AM or PM.
	 * 
	 * @param hoursIn24
	 *            An hour in a 24-hour clock.
	 * @return boolean Whether the hour is AM.
	 */
	public static boolean isAM(int hoursIn24) {
		return hoursIn24 < 12;
	}

	/**
	 * Returns given time (provided in hour12, minute and isAm) as String in
	 * this format: "hh:mm:00:am"
	 * 
	 * @param hour12
	 *            An hour in a 12-hour clock.
	 * @param minute
	 *            The corresponding minute.
	 * @param isAM
	 *            Whether corresponding hour12 is AM.
	 * @return String The same time in "hh:mm:00:am" form. Note that the "ss"
	 *         value is set to "00".
	 * @see #time12ToString(int, int, int, boolean)
	 */
	public static String time12ToString(int hour12, int minute, boolean isAM) {
		return time12ToString(hour12, minute, 0, isAM);
	}

	/**
	 * Returns given time (provided in hour12, minute, second and isAm) as
	 * String in this format: "hh:mm:ss:am"
	 * 
	 * @param hour12
	 *            An hour in a 12-hour clock.
	 * @param minute
	 *            The corresponding minute.
	 * @param second
	 *            The corresponding second.
	 * @param isAM
	 *            Whether corresponding hour12 is AM.
	 * @return String The same time in "hh:mm:ss:am" form.
	 * @see #time12ToString(int, int, boolean)
	 * @see #time24ToString(int, int)
	 */
	public static String time12ToString(int hour12, int minute, int second,
			boolean isAM) {
		String hourStr = numToTwoCharString(hour12);
		String minuteStr = numToTwoCharString(minute);
		String secondStr = numToTwoCharString(second);
		String amPm = isAM ? "am" : "pm";
		return String
				.format("%s:%s:%s:%s", hourStr, minuteStr, secondStr, amPm);
	}

	/**
	 * Returns given time (provided in hour24, minute) as a String in this
	 * format: "hh:mm:00:am"
	 * 
	 * @param hour24
	 *            An hour in a 24-hour clock.
	 * @param minute
	 *            The corresponding minute.
	 * @return String The same time in "hh:mm:00:am" form.
	 * @see #time24ToString(int, int, int)
	 * @see #time12ToString(int, int, boolean)
	 */
	public static String time24ToString(int hour24, int minute) {
		return time12ToString(to12HourClock(hour24), minute, isAM(hour24));
	}

	/**
	 * Returns given time (provided in hour24, minute) as a String in this
	 * format: "hh:mm:ss:am"
	 * 
	 * @param hour24
	 *            An hour in a 24-hour clock.
	 * @param minute
	 *            The corresponding minute.
	 * @param second
	 *            The corresponding second.
	 * @return String The same time in "hh:mm:00:am" form.
	 * @see #time24ToString(int, int)
	 * @see #time12ToString(int, int, boolean)
	 */
	public static String time24ToString(int hour24, int minute, int second) {
		return time12ToString(to12HourClock(hour24), minute, second,
				isAM(hour24));
	}

	/**
	 * Given a String time in "hh:mm:ss:am" format, returns an integer
	 * representing the hour
	 * 
	 * @param time
	 * @return integer An integer representing the hour.
	 */
	public static int parseHours(String time) {
		return Integer.parseInt(time.substring(0, 2));
	}

	/**
	 * Given a String time in "hh:mm:ss:am" format, returns an integer
	 * representing the minutes
	 * 
	 * @param time
	 * @return integer An integer representing the hour.
	 */
	public static int parseMinutes(String time) {
		return Integer.parseInt(time.substring(3, 5));
	}

	/**
	 * Given a String time in "hh:mm:ss:am" format, returns an integer
	 * representing the seconds
	 * 
	 * @param time
	 * @return integer An integer representing the hour.
	 */
	public static int parseSeconds(String time) {
		return Integer.parseInt(time.substring(6, 8));
	}

	/**
	 * Given a String time in "hh:mm:ss:am" format, returns a String "am" or
	 * "pm"
	 * 
	 * @param time
	 * @return String "am" or "pm" depending on the provided time.
	 */
	public static String parseAMPM(String time) {
		return time.substring(9, 11);
	}

	/*
	 * Determines whether the provided start time and end time are valid. End
	 * time should always be after start time.
	 */

	/**
	 * @param startTime
	 *            A time in "hh:mm:ss:am" format.
	 * @param endTime
	 *            A time in "hh:mm:ss:am" format.
	 * @return boolean Whether startTime is earlier than endTime
	 */
	public static boolean isValid(String startTime, String endTime) {
		System.out.println(compareTimes(startTime, endTime));
		return compareTimes(startTime, endTime) < 0;
	}

	/**
	 * Compares two times provided in "hh:mm:ss:am" format. Uses
	 * {@link java.lang.Comparable#compareTo(Object)} logic.
	 * 
	 * @param mTime
	 *            A time in "hh:mm:ss:am" format.
	 * @param anotherTime
	 *            A time in "hh:mm:ss:am" format.
	 * @return int 0 if mTime and anotherTime are equal, 1 if mTime is more
	 *         recent, -1 if anotherTime is more recent.
	 */
	public static int compareTimes(String mTime, String anotherTime) {

		int mHour = parseHours(mTime);
		int mMinutes = parseMinutes(mTime);
		int mSeconds = parseSeconds(mTime);
		boolean mIsAM = isAM(mTime);

		int anotherHour = parseHours(anotherTime);
		int anotherMinutes = parseMinutes(anotherTime);
		int anotherSeconds = parseSeconds(anotherTime);
		boolean anotherIsAM = isAM(anotherTime);

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

	/**
	 * Compares two provided {@link ActivityPojo} instances. Uses
	 * {@link #compareTimes(String, String)}. Useful for sorting. See {@link ActivityPojo#compareTo(ActivityPojo)}. 
	 * 
	 * @param current An instance of ActivityPojo.
	 * @param another Another instance of ActivityPojo.
	 * @return int 0 if current and another are equal, 1 if current is more
	 *         recent, -1 if another is more recent.
	 * @see #compareTimes(String, String)
	 * @see ActivityPojo#compareTo(ActivityPojo)
	 */
	public static int compareActivities(ActivityPojo current,
			ActivityPojo another) {
		if (current.equals(another)) {
			return 0;
		}

		return compareTimes(current.getStart_time(), another.getStart_time());
	}

}
