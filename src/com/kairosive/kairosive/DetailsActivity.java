package com.kairosive.kairosive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kairosive.kairosive.database.ActivityPojo;

public class DetailsActivity extends Activity {

	public static final int RESULT_DELETE = 7;
	public static final int RESULT_UPDATE = 8;

	private int receivedPosition;
	private ActivityPojo receivedActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		receivedPosition = getIntent().getIntExtra("SELECTED_POSITION", -1);
		receivedActivity = (ActivityPojo) getIntent().getSerializableExtra(
				"SELECTED_ACTIVITY");
		populateTextViews();

	}

	private void populateTextViews() {
		TextView activityLabel = (TextView) findViewById(R.id.activity_details_label_textView);
		TextView activityName = (TextView) findViewById(R.id.activity_name_textView);
		TextView startLabel = (TextView) findViewById(R.id.start_time_label_textView);
		TextView startDate = (TextView) findViewById(R.id.start_date_textView);
		TextView startTime = (TextView) findViewById(R.id.start_time_textView);
		TextView endLabel = (TextView) findViewById(R.id.end_time_label_textView);
		TextView endDate = (TextView) findViewById(R.id.end_date_textView);
		TextView endTime = (TextView) findViewById(R.id.end_time_textView);
		TextView detailsLabel = (TextView) findViewById(R.id.details_label_textView);
		TextView details = (TextView) findViewById(R.id.details_textView);

		activityName.setText(receivedActivity.getCategory());
		startDate.setText(receivedActivity.getStart_date());
		startTime.setText(receivedActivity.getStart_time());
		endDate.setText(receivedActivity.getEnd_date());
		endTime.setText(receivedActivity.getEnd_time());
		details.setText(receivedActivity.getDetails().equals("") ? "none"
				: receivedActivity.getDetails());

	}

	public void onCancel(View v) {
		finish();
	}

	public void onUpdate(View v) {
		receivedActivity.setCategory_id(5);
		Intent intent = new Intent();
		intent.putExtra("UPDATED_ACTIVITY", receivedActivity);
		setResult(RESULT_UPDATE, intent);
		finish();
	}

	public void onDelete(View v) {
		Intent intent = new Intent();
		intent.putExtra("SELECTED_POSITION", receivedPosition);
		setResult(RESULT_DELETE, intent);
		finish();
	}
}
