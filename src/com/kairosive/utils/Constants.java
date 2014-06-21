package com.kairosive.utils;


/**
 * A convenience class that holds various constants used throughout the application.
 * @author James Cho
 *
 */
public class Constants {

	
	/**
	 * Provides easy access to all category names. The order of activities  below is used to arrange icons. Index value of each activity is used as the category ID of each {@link com.kairosive.database.ActivityPojo}
	 * @see com.kairosive.database.ActivityPojo#catToString(int)
	 */
	public static final String[] category = { "Time Planning", "New Habit",
			"Input", "Internalizing", "Output", "Health", "Sleep", "Hobbies",
			"Spouse", "Kids", "Relatives", "Home Finance", "Work Learning",
			"Core Work", "Future Work", "Core Relationships",
			"New Relationship", "Relationship Planning", "Worship", "Serving",
			"MISC." };

}
