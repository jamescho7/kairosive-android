package com.kairosive.kairosive;

import android.app.Activity;
import android.os.Bundle;

import com.kairosive.kairosive.database.ActivityPojo;

public class DetailsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		ActivityPojo activity = (ActivityPojo) getIntent().getSerializableExtra("SELECTED_ACTIVITY");
		
		System.out.println(activity.toString());
	}
}
